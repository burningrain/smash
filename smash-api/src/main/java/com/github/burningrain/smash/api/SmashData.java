package com.github.burningrain.smash.api;

import com.github.burningrain.smash.api.scenario.Scenario;
import com.github.burningrain.smash.api.scenario.data.ScenarioData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author burningrain on 02.06.2018.
 */
public class SmashData {

    private Map<String, ScenarioData> scenarios;
    private String defaultScenario;

    private SmashData(Map<String, ScenarioData> scenarios, String defaultScenario) {
        this.scenarios = scenarios;
        this.defaultScenario = defaultScenario;
    }

    public static Builder builder() {
        return new Builder();
    }

    public ScenarioData getScenario(String title) {
        return scenarios.get(title);
    }

    public String getDefaultScenario() {
        return defaultScenario;
    }

    void forEach(Callback callback) {
        for (Map.Entry<String, ScenarioData> dataEntry : scenarios.entrySet()) {
            callback.call(dataEntry.getKey(), dataEntry.getValue());
        }
    }

    interface Callback {
        void call(String title, ScenarioData scenario);
    }

    public static class Builder {

        private HashMap<String,ScenarioData> scenarios = new HashMap<String, ScenarioData>();
        private String defaultScenario;

        public Builder addScenario(ScenarioData scenarioData) {
            scenarios.put(scenarioData.getTitle(), scenarioData);
            return this;
        }

        public Builder setDefaultScenario(String defaultScenario) {
            this.defaultScenario = defaultScenario;
            return this;
        }

        public SmashData build() {
            return new SmashData(scenarios, defaultScenario);
        }

    }

}
