package io.cucumber.java;

import io.cucumber.core.backend.Lookup;
import io.cucumber.core.backend.StepDefinition;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JavaStepDefinitionTransposeTest {

    public static class StepDefs {

        public void mapOfDoubleToDouble(Map<Double, Double> mapOfDoubleToDouble) {

        }

        public void transposedMapOfDoubleToListOfDouble(@Transpose Map<Double, List<Double>> mapOfDoubleToListOfDouble) {
        }
    }

    @Test
    public void transforms_to_map_of_double_to_double() throws Throwable {
        Method m = StepDefs.class.getMethod("mapOfDoubleToDouble", Map.class);
        assertFalse(isTransposed(m));
    }

    @Test
    public void transforms_transposed_to_map_of_double_to_double() throws Throwable {
        Method m = StepDefs.class.getMethod("transposedMapOfDoubleToListOfDouble", Map.class);
        assertTrue(isTransposed(m));
    }

    private boolean isTransposed(Method method) {
        StepDefs stepDefs = new StepDefs();
        Lookup lookup = new SingletonFactory(stepDefs);
        StepDefinition stepDefinition = new JavaStepDefinition(method, "some text", 0, lookup);

        return stepDefinition.parameterInfos().get(0).isTransposed();
    }
}
