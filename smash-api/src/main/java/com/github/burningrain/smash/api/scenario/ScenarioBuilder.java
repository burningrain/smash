package com.github.burningrain.smash.api.scenario;

import com.github.burningrain.smash.api.scenario.data.ScenarioData;
import com.github.burningrain.smash.api.scenario.data.ScenarioDataVisitor;
import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;
import com.github.burningrain.smash.api.scenario.data.transitions.TransitionData;

/**
 * @author burningrain on 28.05.2018.
 */
//TODO вообще убрать нахрен эту обертку
public class ScenarioBuilder implements ScenarioDataVisitor {

    private Scenario.Builder builder = new Scenario.Builder();

    public void setScenarioTitle(String title) {
        // do nothing
    }

    public void visit(NodeData nodeData) {
        builder.addNode(nodeData);
    }

    public void visit(TransitionData data) {
        builder.addTransition(data);
    }

    public void setStartNode(String nodeDataId) {
        builder.setStartNode(nodeDataId);
    }

    public void setEndNode(String nodeDataId) {
        builder.setEndNode(nodeDataId);
    }

    public void setCurrentNode(String nodeDataId) {
        builder.setCurrentNode(nodeDataId);
    }

    public void setScenarioData(ScenarioData scenarioData) {
        builder.setScenarioData(scenarioData);
    }

    public Scenario build() {
        return builder.build();
    }

}