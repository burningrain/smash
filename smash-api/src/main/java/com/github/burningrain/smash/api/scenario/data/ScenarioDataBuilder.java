package com.github.burningrain.smash.api.scenario.data;

/**
 * @author burningrain on 24.05.2018.
 */
public interface ScenarioDataBuilder extends ScenarioDataVisitor {

    void setCurrentNode(String nodeDataId);

    String build();

}