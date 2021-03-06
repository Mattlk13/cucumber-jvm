package io.cucumber.java;

import io.cucumber.core.api.Scenario;
import io.cucumber.core.backend.Lookup;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.quality.Strictness.STRICT_STUBS;

public class JavaHookDefinitionTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(STRICT_STUBS);

    private final Lookup lookup = new Lookup() {

        @Override
        @SuppressWarnings("unchecked")
        public <T> T getInstance(Class<T> glueClass) {
            return (T) JavaHookDefinitionTest.this;
        }
    };

    @Mock
    private Scenario scenario;

    private boolean invoked = false;

    @Test
    public void can_create_with_no_argument() throws Throwable {
        Method method = JavaHookDefinitionTest.class.getMethod("no_arguments");
        JavaHookDefinition definition = new JavaHookDefinition(method, "", 0, 0, lookup);
        definition.execute(scenario);
        assertTrue(invoked);
    }


    @Before
    public void no_arguments() {
        invoked = true;
    }

    @Test
    public void can_create_with_single_scenario_argument() throws Throwable {
        Method method = JavaHookDefinitionTest.class.getMethod("single_argument", Scenario.class);
        JavaHookDefinition definition = new JavaHookDefinition(method, "", 0, 0, lookup);
        definition.execute(scenario);
        assertTrue(invoked);
    }

    @Before
    public void single_argument(Scenario scenario) {
        invoked = true;
    }

    @Test
    public void fails_if_hook_argument_is_not_scenario_result() throws NoSuchMethodException {
        Method method = JavaHookDefinitionTest.class.getMethod("invalid_parameter", String.class);
        InvalidMethodSignatureException exception = assertThrows(
            InvalidMethodSignatureException.class,
            () -> new JavaHookDefinition(method, "", 0, 0, lookup)
        );
        assertThat(exception.getMessage(), startsWith("" +
            "A method annotated with Before, After, BeforeStep or AfterStep must have one of these signatures:\n" +
            " * public void before_or_after(Scenario scenario)\n" +
            " * public void before_or_after()\n" +
            "at io.cucumber.java.JavaHookDefinitionTest.invalid_parameter(String) in file:"));
    }


    public void invalid_parameter(String badType) {

    }

    @Test
    public void fails_if_generic_hook_argument_is_not_scenario_result() throws NoSuchMethodException {
        Method method = JavaHookDefinitionTest.class.getMethod("invalid_generic_parameter", List.class);
        assertThrows(
            InvalidMethodSignatureException.class,
            () -> new JavaHookDefinition(method, "", 0, 0, lookup)
        );
    }


    public void invalid_generic_parameter(List<String> badType) {

    }

    @Test
    public void fails_if_too_many_arguments() throws NoSuchMethodException {
        Method method = JavaHookDefinitionTest.class.getMethod("too_many_parameters", Scenario.class, String.class);
        assertThrows(
            InvalidMethodSignatureException.class,
            () -> new JavaHookDefinition(method, "", 0, 0, lookup)
        );
    }


    public void too_many_parameters(Scenario arg1, String arg2) {

    }


}
