package com.github.burningrain.smash.api.scenario.data;

/**
 * @author burningrain on 24.05.2018.
 */
public abstract class ScenarioElement {

    private boolean passed;

    public ScenarioElement(boolean passed) {
        this.passed = passed;
    }

    public boolean isPassed() {
        return passed;
    }

    public void markPassed() {
        passed = true;
    }

    public abstract void accept(ScenarioDataVisitor visitor);

}
