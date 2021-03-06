package io.cucumber.core.runtime;

import gherkin.events.PickleEvent;
import io.cucumber.core.backend.ObjectFactoryServiceLoader;
import io.cucumber.core.event.EventHandler;
import io.cucumber.core.event.EventPublisher;
import io.cucumber.core.event.Result;
import io.cucumber.core.event.Status;
import io.cucumber.core.event.TestCaseFinished;
import io.cucumber.core.event.TestRunFinished;
import io.cucumber.core.event.TestRunStarted;
import io.cucumber.core.event.TestSourceRead;
import io.cucumber.core.eventbus.EventBus;
import io.cucumber.core.exception.CompositeCucumberException;
import io.cucumber.core.exception.CucumberException;
import io.cucumber.core.feature.CucumberFeature;
import io.cucumber.core.feature.FeatureLoader;
import io.cucumber.core.filter.Filters;
import io.cucumber.core.io.ClassFinder;
import io.cucumber.core.io.MultiLoader;
import io.cucumber.core.io.ResourceLoader;
import io.cucumber.core.io.ResourceLoaderClassFinder;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.core.options.RuntimeOptions;
import io.cucumber.core.order.PickleOrder;
import io.cucumber.core.plugin.ConcurrentEventListener;
import io.cucumber.core.plugin.Plugin;
import io.cucumber.core.plugin.PluginFactory;
import io.cucumber.core.plugin.Plugins;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Collections.emptyList;
import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.Comparator.comparing;

/**
 * This is the main entry point for running Cucumber features from the CLI.
 */
public final class Runtime {

    private static final Logger log = LoggerFactory.getLogger(Runtime.class);

    private final ExitStatus exitStatus;

    private final RunnerSupplier runnerSupplier;
    private final Filters filters;
    private final EventBus bus;
    private final FeatureSupplier featureSupplier;
    private final ExecutorService executor;
    private final PickleOrder pickleOrder;

    private Runtime(final ExitStatus exitStatus,
                    final EventBus bus,
                    final Filters filters,
                    final RunnerSupplier runnerSupplier,
                    final FeatureSupplier featureSupplier,
                    final ExecutorService executor,
                    final PickleOrder pickleOrder) {
        this.filters = filters;
        this.bus = bus;
        this.runnerSupplier = runnerSupplier;
        this.featureSupplier = featureSupplier;
        this.executor = executor;
        this.exitStatus = exitStatus;
        this.pickleOrder = pickleOrder;
    }

    public void run() {
        final List<CucumberFeature> features = featureSupplier.get();
        bus.send(new TestRunStarted(bus.getInstant()));
        for (CucumberFeature feature : features) {
            bus.send(new TestSourceRead(bus.getInstant(), feature.getUri().toString(), feature.getSource()));
        }

        final List<PickleEvent> filteredEvents = new ArrayList<>();
        for (CucumberFeature feature : features) {
            for (final PickleEvent pickleEvent : feature.getPickles()) {
                if (filters.matchesFilters(pickleEvent)) {
                    filteredEvents.add(pickleEvent);
                }
            }
        }

        final List<PickleEvent> orderedEvents = pickleOrder.orderPickleEvents(filteredEvents);
        final List<PickleEvent> limitedEvents = filters.limitPickleEvents(orderedEvents);

        final List<Future<?>> executingPickles = new ArrayList<>();
        for (final PickleEvent pickleEvent : limitedEvents) {
            executingPickles.add(executor.submit(new Runnable() {
                @Override
                public void run() {
                    runnerSupplier.get().runPickle(pickleEvent);
                }
            }));
        }

        executor.shutdown();

        List<Throwable> thrown = new ArrayList<>();
        for (Future executingPickle : executingPickles) {
            try {
                executingPickle.get();
            } catch (ExecutionException e) {
                log.error("Exception while executing pickle", e);
                thrown.add(e.getCause());
            } catch (InterruptedException e) {
                executor.shutdownNow();
                throw new CucumberException(e);
            }
        }
        if (thrown.size() == 1) {
            throw new CucumberException(thrown.get(0));
        } else if (thrown.size() > 1) {
            throw new CompositeCucumberException(thrown);
        }

        bus.send(new TestRunFinished(bus.getInstant()));
    }

    public byte exitStatus() {
        return exitStatus.exitStatus();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private EventBus eventBus = new TimeServiceEventBus(Clock.systemUTC());
        private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        private RuntimeOptions runtimeOptions = RuntimeOptions.defaultOptions();
        private BackendSupplier backendSupplier;
        private ResourceLoader resourceLoader;
        private FeatureSupplier featureSupplier;
        private List<Plugin> additionalPlugins = emptyList();

        private Builder() {
        }

        public Builder withRuntimeOptions(final RuntimeOptions runtimeOptions) {
            this.runtimeOptions = runtimeOptions;
            return this;
        }

        public Builder withClassLoader(final ClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }

        public Builder withResourceLoader(final ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
            return this;
        }

        public Builder withBackendSupplier(final BackendSupplier backendSupplier) {
            this.backendSupplier = backendSupplier;
            return this;
        }

        public Builder withFeatureSupplier(final FeatureSupplier featureSupplier) {
            this.featureSupplier = featureSupplier;
            return this;
        }

        public Builder withAdditionalPlugins(final Plugin... plugins) {
            this.additionalPlugins = Arrays.asList(plugins);
            return this;
        }

        public Builder withEventBus(final EventBus eventBus) {
            this.eventBus = eventBus;
            return this;
        }

        public Runtime build() {
            final ResourceLoader resourceLoader = this.resourceLoader != null
                ? this.resourceLoader
                : new MultiLoader(this.classLoader);

            final ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, this.classLoader);

            final ObjectFactoryServiceLoader objectFactoryServiceLoader = new ObjectFactoryServiceLoader(runtimeOptions);

            final ObjectFactorySupplier objectFactorySupplier = runtimeOptions.isMultiThreaded()
                ? new ThreadLocalObjectFactorySupplier(objectFactoryServiceLoader)
                : new SingletonObjectFactorySupplier(objectFactoryServiceLoader);

            final BackendSupplier backendSupplier = this.backendSupplier != null
                ? this.backendSupplier
                : new BackendServiceLoader(resourceLoader, objectFactorySupplier);

            final Plugins plugins = new Plugins(new PluginFactory(), runtimeOptions);
            for (final Plugin plugin : additionalPlugins) {
                plugins.addPlugin(plugin);
            }
            final ExitStatus exitStatus = new ExitStatus(runtimeOptions);
            plugins.addPlugin(exitStatus);
            if (runtimeOptions.isMultiThreaded()) {
                plugins.setSerialEventBusOnEventListenerPlugins(eventBus);
            } else {
                plugins.setEventBusOnEventListenerPlugins(eventBus);
            }

            final TypeRegistryConfigurerSupplier typeRegistryConfigurerSupplier = new ScanningTypeRegistryConfigurerSupplier(classFinder, runtimeOptions);

            final RunnerSupplier runnerSupplier = runtimeOptions.isMultiThreaded()
                ? new ThreadLocalRunnerSupplier(runtimeOptions, eventBus, backendSupplier, objectFactorySupplier, typeRegistryConfigurerSupplier)
                : new SingletonRunnerSupplier(runtimeOptions, eventBus, backendSupplier, objectFactorySupplier, typeRegistryConfigurerSupplier);

            final ExecutorService executor = runtimeOptions.isMultiThreaded()
                ? Executors.newFixedThreadPool(runtimeOptions.getThreads(), new CucumberThreadFactory())
                : new SameThreadExecutorService();


            final FeatureLoader featureLoader = new FeatureLoader(resourceLoader);

            final FeatureSupplier featureSupplier = this.featureSupplier != null
                ? this.featureSupplier
                : new FeaturePathFeatureSupplier(featureLoader, runtimeOptions);

            final Filters filters = new Filters(runtimeOptions);
            final PickleOrder pickleOrder = runtimeOptions.getPickleOrder();

            return new Runtime(exitStatus, eventBus, filters, runnerSupplier, featureSupplier, executor, pickleOrder);
        }
    }

    private static final class CucumberThreadFactory implements ThreadFactory {

        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        CucumberThreadFactory() {
            this.namePrefix = "cucumber-runner-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, namePrefix + this.threadNumber.getAndIncrement());
        }
    }

    private static final class SameThreadExecutorService extends AbstractExecutorService {

        @Override
        public void execute(Runnable command) {
            command.run();
        }

        @Override
        public void shutdown() {
            //no-op
        }

        @Override
        public List<Runnable> shutdownNow() {
            return Collections.emptyList();
        }

        @Override
        public boolean isShutdown() {
            return true;
        }

        @Override
        public boolean isTerminated() {
            return true;
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) {
            return true;
        }
    }

    static final class ExitStatus implements ConcurrentEventListener {
        private static final byte DEFAULT = 0x0;
        private static final byte ERRORS = 0x1;

        private final List<Result> results = new ArrayList<>();
        private final RuntimeOptions runtimeOptions;

        private final EventHandler<TestCaseFinished> testCaseFinishedHandler = new EventHandler<TestCaseFinished>() {
            @Override
            public void receive(TestCaseFinished event) {
                results.add(event.getResult());
            }
        };

        ExitStatus(RuntimeOptions runtimeOptions) {
            this.runtimeOptions = runtimeOptions;
        }

        @Override
        public void setEventPublisher(EventPublisher publisher) {
            publisher.registerHandlerFor(TestCaseFinished.class, testCaseFinishedHandler);
        }

        byte exitStatus() {
            if (results.isEmpty()) {
                return DEFAULT;
            }

            if (runtimeOptions.isWip()) {
                Result leastSeverResult = min(results, comparing(Result::getStatus));
                return leastSeverResult.getStatus().is(Status.PASSED) ? ERRORS : DEFAULT;
            } else {
                Result mostSevereResult = max(results, comparing(Result::getStatus));
                return mostSevereResult.getStatus().isOk(runtimeOptions.isStrict()) ? DEFAULT : ERRORS;
            }
        }
    }
}
