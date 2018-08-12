package com.github.burningrain.smash.api.scenario.data;

import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;
import com.github.burningrain.smash.api.scenario.data.transitions.TransitionData;

/**
 * @author burningrain on 24.05.2018.
 */
public interface ScenarioDataVisitor {

    void setScenarioTitle(String title);

    void visit(NodeData nodeData);

    void visit(TransitionData transitionData);

    void setStartNode(String nodeDataId);

    void setEndNode(String nodeDataId);

    void setCurrentNode(String nodeDataId); //todo вытащить отсюда в отдельный интерфейс

}