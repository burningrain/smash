package com.github.burningrain.smash.api.mocks;

import com.github.burningrain.smash.api.scenario.data.ScenarioDataBuilder;
import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;
import com.github.burningrain.smash.api.scenario.data.transitions.TransitionData;

import java.util.Collection;

/**
 * @author burningrain on 05.09.2018.
 */
public class ScenarioDataBuilderMock implements ScenarioDataBuilder {
    @Override
    public void setCurrentNode(String nodeDataId) {

    }

    @Override
    public String build() {
        return null;
    }

    @Override
    public void setScenarioTitle(String title) {

    }

    @Override
    public void visit(NodeData nodeData) {

    }

    @Override
    public void visit(TransitionData transitionData) {

    }

    @Override
    public void visitStartNode(String startNode) {

    }

    @Override
    public void visitEndNodes(Collection<String> endNodes) {

    }

    @Override
    public void visitFrontierNodes(Collection<String> goOnNodes) {

    }

}
