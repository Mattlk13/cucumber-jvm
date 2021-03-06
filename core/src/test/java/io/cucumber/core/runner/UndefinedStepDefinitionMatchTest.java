package io.cucumber.core.runner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.jupiter.api.function.Executable;

import gherkin.pickles.PickleStep;
import io.cucumber.core.api.Scenario;

public class UndefinedStepDefinitionMatchTest {

    public final UndefinedPickleStepDefinitionMatch match = new UndefinedPickleStepDefinitionMatch(mock(PickleStep.class));

    @Test
    public void throws_ambiguous_step_definitions_exception_when_run() {
        final Executable testMethod = () -> match.runStep(mock(Scenario.class));
        final UndefinedStepDefinitionException expectedThrown = assertThrows(UndefinedStepDefinitionException.class, testMethod);
        assertThat(expectedThrown.getMessage(), is(equalTo("No step definitions found")));
    }

    @Test
    public void throws_ambiguous_step_definitions_exception_when_dry_run() {
        final Executable testMethod = () -> match.dryRunStep(mock(Scenario.class));
        final UndefinedStepDefinitionException expectedThrown = assertThrows(UndefinedStepDefinitionException.class, testMethod);
        assertThat(expectedThrown.getMessage(), is(equalTo("No step definitions found")));
    }

}
