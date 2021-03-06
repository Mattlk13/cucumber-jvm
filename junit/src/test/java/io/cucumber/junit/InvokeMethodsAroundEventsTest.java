package io.cucumber.junit;

import io.cucumber.core.plugin.ConcurrentEventListener;
import io.cucumber.core.event.EventPublisher;
import io.cucumber.core.event.TestRunFinished;
import io.cucumber.core.event.TestRunStarted;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;

public class InvokeMethodsAroundEventsTest {

    private static final List<String> events = new ArrayList<>();

    @AfterClass
    public static void afterClass() {
        events.clear();
    }

    @Test
    public void invoke_methods_around_events() throws InitializationError {
        Cucumber cucumber = new Cucumber(BeforeAfterClass.class);
        cucumber.run(new RunNotifier());
        assertThat(events, contains("BeforeClass", "TestRunStarted", "TestRunFinished", "AfterClass"));
    }

    @CucumberOptions(plugin = {"io.cucumber.junit.InvokeMethodsAroundEventsTest$TestRunStartedFinishedListener"})
    public static class BeforeAfterClass {

        @BeforeClass
        public static void beforeClass() {
            events.add("BeforeClass");
        }

        @AfterClass
        public static void afterClass() {
            events.add("AfterClass");
        }
    }

    @SuppressWarnings("unused") // Used as a plugin by BeforeAfterClass
    public static class TestRunStartedFinishedListener implements ConcurrentEventListener {

        @Override
        public void setEventPublisher(EventPublisher publisher) {
            publisher.registerHandlerFor(TestRunStarted.class, event -> events.add("TestRunStarted"));
            publisher.registerHandlerFor(TestRunFinished.class, event -> events.add("TestRunFinished"));
        }

    }
}
