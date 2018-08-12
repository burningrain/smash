package com.github.burningrain.smash.api;

import com.github.burningrain.smash.api.scenario.data.ScenarioData;

/**
 * @author burningrain on 18.05.2018.
 */
public class ScenarioManager {

    private final SmashData data;

    public ScenarioManager(final SmashData data) {
        this.data = data;
    }

    public ScenarioData generateDefaultScenario() {
        return data.getDefaultScenario();
    }

    public ScenarioData getScenario(String title) {
        return data.getScenario(title);
    }


}