package com.github.burningrain.smash.api.scenario.data;

import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;
import com.github.burningrain.smash.api.scenario.data.transitions.TransitionData;

import java.util.Collection;

/**
 * @author burningrain on 24.05.2018.
 */
public interface ScenarioDataVisitor {

    void setScenarioTitle(String title);

    void visit(NodeData nodeData);

    void visit(TransitionData transitionData);

    void visitStartNode(String startNode);

    void visitEndNodes(Collection<String> endNodes);

    void visitFrontierNodes(Collection<String> frontierNodes);
}