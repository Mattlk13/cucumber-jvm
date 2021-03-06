package io.cucumber.java8;

import org.apiguardian.api.API;

@API(status = API.Status.STABLE)
public interface LambdaGlue {

    String EMPTY_TAG_EXPRESSIONS = "";
    long NO_TIMEOUT = 0;
    int DEFAULT_BEFORE_ORDER = 1000;
    int DEFAULT_AFTER_ORDER = 1000;

    /**
     * Defines an before hook.
     *
     * @param body lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void Before(final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_BEFORE_ORDER, NO_TIMEOUT, body));
    }

    /**
     * Defines an before hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param body          lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void Before(String tagExpression, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeHookDefinition(new Java8HookDefinition(tagExpression, DEFAULT_BEFORE_ORDER, NO_TIMEOUT, body));
    }

    /**
     * Defines an before hook.
     *
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param body          lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void Before(long timeoutMillis, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_BEFORE_ORDER, timeoutMillis, body));
    }

    /**
     * Defines an before hook.
     *
     * @param order the order in which this hook should run. Higher numbers are run first
     * @param body  lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void Before(int order, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, order, NO_TIMEOUT, body));
    }

    /**
     * Defines an before hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param order         the order in which this hook should run. Higher numbers are run first
     * @param body          lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void Before(String tagExpression, long timeoutMillis, int order, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeHookDefinition(new Java8HookDefinition(tagExpression, order, timeoutMillis, body));
    }

    /**
     * Defines an before hook.
     *
     * @param body lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void Before(final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_BEFORE_ORDER, NO_TIMEOUT, body));
    }

    /**
     * Defines an before hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param body          lambda to execute
     */
    default void Before(String tagExpression, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeHookDefinition(new Java8HookDefinition(tagExpression, DEFAULT_BEFORE_ORDER, NO_TIMEOUT, body));
    }

    /**
     * Defines an before hook.
     *
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param body          lambda to execute
     */
    default void Before(long timeoutMillis, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_BEFORE_ORDER, timeoutMillis, body));
    }

    /**
     * Defines an before hook.
     *
     * @param order the order in which this hook should run. Higher numbers are run first
     * @param body  lambda to execute
     */
    default void Before(int order, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, order, NO_TIMEOUT, body));
    }

    /**
     * Defines an before hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param order         the order in which this hook should run. Higher numbers are run first
     * @param body          lambda to execute
     */
    default void Before(String tagExpression, long timeoutMillis, int order, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeHookDefinition(new Java8HookDefinition(tagExpression, order, timeoutMillis, body));
    }

    /**
     * Defines an before step hook.
     *
     * @param body lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void BeforeStep(final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeStepHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_BEFORE_ORDER, NO_TIMEOUT, body));
    }

    /**
     * Defines an before step hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param body          lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void BeforeStep(String tagExpression, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeStepHookDefinition(new Java8HookDefinition(tagExpression, DEFAULT_BEFORE_ORDER, NO_TIMEOUT, body));
    }

    /**
     * Defines an before step hook.
     *
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param body          lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void BeforeStep(long timeoutMillis, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeStepHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_BEFORE_ORDER, timeoutMillis, body));
    }

    /**
     * Defines an before step hook.
     *
     * @param order the order in which this hook should run. Higher numbers are run first
     * @param body  lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void BeforeStep(int order, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeStepHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, order, NO_TIMEOUT, body));
    }

    /**
     * Defines an before step hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param order         the order in which this hook should run. Higher numbers are run first
     * @param body          lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void BeforeStep(String tagExpression, long timeoutMillis, int order, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeStepHookDefinition(new Java8HookDefinition(tagExpression, order, timeoutMillis, body));
    }

    /**
     * Defines an before step hook.
     *
     * @param body lambda to execute
     */
    default void BeforeStep(final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeStepHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_BEFORE_ORDER, NO_TIMEOUT, body));
    }


    /**
     * Defines an before step hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param body          lambda to execute
     */
    default void BeforeStep(String tagExpression, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeStepHookDefinition(new Java8HookDefinition(tagExpression, DEFAULT_BEFORE_ORDER, NO_TIMEOUT, body));
    }


    /**
     * Defines an before step hook.
     *
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param body          lambda to execute
     */
    default void BeforeStep(long timeoutMillis, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeStepHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_BEFORE_ORDER, timeoutMillis, body));
    }

    /**
     * Defines an before step hook.
     *
     * @param order the order in which this hook should run. Higher numbers are run first
     * @param body  lambda to execute
     */
    default void BeforeStep(int order, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeStepHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, order, NO_TIMEOUT, body));
    }

    /**
     * Defines an before step hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param order         the order in which this hook should run. Higher numbers are run first
     * @param body          lambda to execute
     */
    default void BeforeStep(String tagExpression, long timeoutMillis, int order, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addBeforeStepHookDefinition(new Java8HookDefinition(tagExpression, order, timeoutMillis, body));
    }

    /**
     * Defines an after hook.
     *
     * @param body lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void After(final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_AFTER_ORDER, NO_TIMEOUT, body));
    }

    /**
     * Defines an after hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param body          lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void After(String tagExpression, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterHookDefinition(new Java8HookDefinition(tagExpression, DEFAULT_AFTER_ORDER, NO_TIMEOUT, body));
    }

    /**
     * Defines an after hook.
     *
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param body          lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void After(long timeoutMillis, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_AFTER_ORDER, timeoutMillis, body));
    }

    /**
     * Defines an after hook.
     *
     * @param order the order in which this hook should run. Higher numbers are run first
     * @param body  lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void After(int order, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, order, NO_TIMEOUT, body));
    }

    /**
     * Defines and after hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param order         the order in which this hook should run. Higher numbers are run first
     * @param body          lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void After(String tagExpression, long timeoutMillis, int order, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterHookDefinition(new Java8HookDefinition(tagExpression, order, timeoutMillis, body));
    }

    /**
     * Defines and after hook.
     *
     * @param body lambda to execute
     */
    default void After(final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_AFTER_ORDER, NO_TIMEOUT, body));
    }

    /**
     * Defines and after hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param body          lambda to execute
     */
    default void After(String tagExpression, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterHookDefinition(new Java8HookDefinition(tagExpression, DEFAULT_AFTER_ORDER, NO_TIMEOUT, body));
    }

    /**
     * Defines and after hook.
     *
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param body          lambda to execute
     */
    default void After(long timeoutMillis, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_AFTER_ORDER, timeoutMillis, body));
    }

    /**
     * Defines and after hook.
     *
     * @param order the order in which this hook should run. Higher numbers are run first
     * @param body  lambda to execute
     */
    default void After(int order, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, order, NO_TIMEOUT, body));
    }

    /**
     * Defines and after hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param order         the order in which this hook should run. Higher numbers are run first
     * @param body          lambda to execute
     */
    default void After(String tagExpression, long timeoutMillis, int order, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterHookDefinition(new Java8HookDefinition(tagExpression, order, timeoutMillis, body));
    }

    /**
     * Defines and after step hook.
     *
     * @param body lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void AfterStep(final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterStepHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_AFTER_ORDER, NO_TIMEOUT, body));
    }

    /**
     * Defines and after step hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param body          lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void AfterStep(String tagExpression, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterStepHookDefinition(new Java8HookDefinition(tagExpression, DEFAULT_AFTER_ORDER, NO_TIMEOUT, body));
    }

    /**
     * Defines and after step hook.
     *
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param body          lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void AfterStep(long timeoutMillis, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterStepHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_AFTER_ORDER, timeoutMillis, body));
    }

    /**
     * Defines and after step hook.
     *
     * @param order the order in which this hook should run. Higher numbers are run first
     * @param body  lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void AfterStep(int order, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterStepHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, order, NO_TIMEOUT, body));
    }

    /**
     * Defines and after step hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param order         the order in which this hook should run. Higher numbers are run first
     * @param body          lambda to execute, takes {@link io.cucumber.core.api.Scenario} as an argument
     */
    default void AfterStep(String tagExpression, long timeoutMillis, int order, final HookBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterStepHookDefinition(new Java8HookDefinition(tagExpression, order, timeoutMillis, body));
    }

    /**
     * Defines and after step hook.
     *
     * @param body lambda to execute
     */
    default void AfterStep(final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterStepHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_AFTER_ORDER, NO_TIMEOUT, body));
    }

    /**
     * Defines and after step hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param body          lambda to execute
     */
    default void AfterStep(String tagExpression, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterStepHookDefinition(new Java8HookDefinition(tagExpression, DEFAULT_AFTER_ORDER, NO_TIMEOUT, body));
    }

    /**
     * Defines and after step hook.
     *
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param body          lambda to execute
     */
    default void AfterStep(long timeoutMillis, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterStepHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, DEFAULT_AFTER_ORDER, timeoutMillis, body));
    }

    /**
     * Defines and after step hook.
     *
     * @param order the order in which this hook should run. Higher numbers are run first
     * @param body  lambda to execute
     */
    default void AfterStep(int order, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterStepHookDefinition(new Java8HookDefinition(EMPTY_TAG_EXPRESSIONS, order, NO_TIMEOUT, body));
    }

    /**
     * Defines and after step hook.
     *
     * @param tagExpression a tag expression, if the expression applies to the current scenario this hook will be executed
     * @param timeoutMillis max amount of milliseconds this is allowed to run for
     * @param order         the order in which this hook should run. Higher numbers are run first
     * @param body          lambda to execute
     */
    default void AfterStep(String tagExpression, long timeoutMillis, int order, final HookNoArgsBody body) {
        LambdaGlueRegistry.INSTANCE.get().addAfterStepHookDefinition(new Java8HookDefinition(tagExpression, order, timeoutMillis, body));
    }

}
