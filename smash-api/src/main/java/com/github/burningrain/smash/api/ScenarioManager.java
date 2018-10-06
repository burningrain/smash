package com.github.burningrain.smash.api;

import com.github.burningrain.smash.api.scenario.Scenario;
import com.github.burningrain.smash.api.scenario.ScenarioConverter;
import com.github.burningrain.smash.api.scenario.data.ScenarioData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author burningrain on 18.05.2018.
 */
class ScenarioManager {

    private final String defaultScenario;
    private Map<String, Scenario> innerScenarios;

    private InternalContext smashContext;

    public ScenarioManager(InternalContext smashContext, final SmashData data) {
        this.smashContext = smashContext;
        this.defaultScenario = data.getDefaultScenario();
        this.innerScenarios = getScenarios(data);
    }

    String getDefaultScenario() {
        return defaultScenario;
    }

    Scenario getScenario(String title) {
        return innerScenarios.get(title);
    }

    private Map<String, Scenario> getScenarios(SmashData data) {
            final ScenarioConverter converter = smashContext.getSingleton(ScenarioConverter.class);

            final HashMap<String, Scenario> result = new HashMap<>();
            data.forEach(new SmashData.Callback() {
                @Override
                public void call(String title, ScenarioData scenario) {
                    //todo здесь надо компоновать сценарии из огрызков
                    result.put(title, converter.toScenario(scenario));
                }
            });
            return result;
    }

}