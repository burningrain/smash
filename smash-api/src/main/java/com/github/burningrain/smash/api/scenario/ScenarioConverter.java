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

    /**
     * использовать не в движке в рантайме, а исключительно во внешних редакторах
     */
    @Deprecated
    public Scenario toScenario(String scenario) {
        return toScenario(stringScenarioConverter.toScenarioData(scenario));
    }

    public Scenario toScenario(ScenarioData scenarioData) {
        Scenario.Builder scenarioBuilder = new Scenario.Builder();
        scenarioData.accept(scenarioBuilder);
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