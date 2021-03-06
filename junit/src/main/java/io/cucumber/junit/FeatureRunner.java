package io.cucumber.junit;

import gherkin.ast.Feature;
import gherkin.events.PickleEvent;
import io.cucumber.core.exception.CucumberException;
import io.cucumber.core.feature.CucumberFeature;
import io.cucumber.core.filter.Filters;
import io.cucumber.core.runtime.RunnerSupplier;
import io.cucumber.junit.PickleRunners.PickleRunner;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static io.cucumber.junit.PickleRunners.withNoStepDescriptions;
import static io.cucumber.junit.PickleRunners.withStepDescriptions;

final class FeatureRunner extends ParentRunner<PickleRunner> {
    private final List<PickleRunner> children = new ArrayList<>();

    private final CucumberFeature cucumberFeature;
    private Description description;

    FeatureRunner(CucumberFeature cucumberFeature, Filters filters, RunnerSupplier runnerSupplier, JUnitOptions jUnitOptions) throws InitializationError {
        super(null);
        this.cucumberFeature = cucumberFeature;
        buildFeatureElementRunners(filters, runnerSupplier, jUnitOptions);
    }

    @Override
    protected String getName() {
        Feature feature = cucumberFeature.getGherkinFeature().getFeature();
        return feature.getKeyword() + ": " + feature.getName();
    }

    @Override
    public Description getDescription() {
        if (description == null) {
            description = Description.createSuiteDescription(getName(), new FeatureId(cucumberFeature));
            for (PickleRunner child : getChildren()) {
                description.addChild(describeChild(child));
            }
        }
        return description;
    }

    boolean isEmpty() {
        return children.isEmpty();
    }

    @Override
    protected List<PickleRunner> getChildren() {
        return children;
    }

    @Override
    protected Description describeChild(PickleRunner child) {
        return child.getDescription();
    }

    @Override
    protected void runChild(PickleRunner child, RunNotifier notifier) {
        notifier.fireTestStarted(getDescription());
        try {
            child.run(notifier);
        } catch (Throwable e) {
            notifier.fireTestFailure(new Failure(getDescription(), e));
            notifier.pleaseStop();
        } finally {
            notifier.fireTestFinished(getDescription());
        }
    }

    private void buildFeatureElementRunners(Filters filters, RunnerSupplier runnerSupplier, JUnitOptions jUnitOptions) {
        for (PickleEvent pickleEvent : cucumberFeature.getPickles()) {
            if (filters.matchesFilters(pickleEvent)) {
                try {
                    if (jUnitOptions.stepNotifications()) {
                        PickleRunner picklePickleRunner;
                        picklePickleRunner = withStepDescriptions(runnerSupplier, pickleEvent, jUnitOptions);
                        children.add(picklePickleRunner);
                    } else {
                        PickleRunner picklePickleRunner;
                        picklePickleRunner = withNoStepDescriptions(cucumberFeature.getName(), runnerSupplier, pickleEvent, jUnitOptions);
                        children.add(picklePickleRunner);
                    }
                } catch (InitializationError e) {
                    throw new CucumberException("Failed to create scenario runner", e);
                }
            }
        }
    }

    private static final class FeatureId implements Serializable {
        private static final long serialVersionUID = 1L;
        private final URI uri;

        FeatureId(CucumberFeature feature) {
            this.uri = feature.getUri();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FeatureId featureId = (FeatureId) o;
            return uri.equals(featureId.uri);

        }

        @Override
        public int hashCode() {
            return uri.hashCode();
        }

        @Override
        public String toString() {
            return uri.toString();
        }
    }

}
