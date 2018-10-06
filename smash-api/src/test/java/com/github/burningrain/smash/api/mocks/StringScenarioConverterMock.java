package com.github.burningrain.smash.api.mocks;

import com.github.burningrain.smash.api.scenario.data.ScenarioData;
import com.github.burningrain.smash.api.scenario.data.StringScenarioConverter;

/**
 * @author burningrain on 05.09.2018.
 */
public class StringScenarioConverterMock implements StringScenarioConverter {
    @Override
    public ScenarioData toScenarioData(String scenario) {
        return null;
    }
}