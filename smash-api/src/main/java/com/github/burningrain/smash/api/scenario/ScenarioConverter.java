package com.github.burningrain.smash.api.scenario;

import com.github.burningrain.smash.api.scenario.data.ScenarioData;
import com.github.burningrain.smash.api.scenario.data.ScenarioDataBuilder;
import com.github.burningrain.smash.api.InternalContext;
import com.github.burningrain.smash.api.scenario.data.StringScenarioConverter;

/**
 * @author burningrain on 21.05.2018.
 */
public class ScenarioConverter {

    private InternalContext smashContext;

    private Class<ScenarioDataBuilder> visitorBuilderClass = ScenarioDataBuilder.class;
    private StringScenarioConverter stringScenarioConverter;

    public ScenarioConverter(InternalContext smashContext) {
        this.smashContext = smashContext;
        this.stringScenarioConverter = smashContext.getSingleton(StringScenarioConverter.class);
    }

    public Scenario toScenario(String scenario, String currentNodeId) {
        return toScenario(stringScenarioConverter.toScenarioData(scenario), currentNodeId);
    }

    public Scenario toScenario(ScenarioData scenarioData, String currentNodeId) {
        ScenarioBuilder scenarioBuilder = new ScenarioBuilder();
        scenarioData.accept(scenarioBuilder);
        scenarioBuilder.setCurrentNode(currentNodeId);
        scenarioBuilder.setScenarioData(scenarioData);
        return scenarioBuilder.build();
    }

    public String toString(ScenarioData scenarioData, String currentNodeId) {
        final ScenarioDataBuilder scenarioDataBuilder = smashContext.getPrototype(visitorBuilderClass);
        scenarioData.accept(scenarioDataBuilder);
        scenarioDataBuilder.setCurrentNode(currentNodeId);
        return scenarioDataBuilder.build();
    }


}