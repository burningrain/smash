package com.github.burningrain.smash.api.scenario.data;

import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;
import com.github.burningrain.smash.api.scenario.data.transitions.TransitionData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author burningrain on 24.05.2018.
 */
public class ScenarioData {

    private final String title;
    private final Collection<NodeData> nodeData;
    private final Collection<TransitionData> transitionData;
    private final NodeData startNode;
    private final NodeData endNode;

    public ScenarioData(final String title, final Collection<NodeData> nodeData,
                        final Collection<TransitionData> transitionData, final NodeData startNode, final NodeData endNode) {
        this.title = title;
        this.nodeData = nodeData;
        this.transitionData = transitionData;
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public String getTitle() {
        return title;
    }

    public String getStartNode() {
        return startNode.getId();
    }

    public String getEndNode() {
        return endNode.getId();
    }

    public void accept(ScenarioDataVisitor scenarioDataVisitor) {
        scenarioDataVisitor.setScenarioTitle(title);
        for (NodeData nodeData : this.nodeData) {
            nodeData.accept(scenarioDataVisitor);
        }
        for (TransitionData transitionData : this.transitionData) {
            transitionData.accept(scenarioDataVisitor);
        }
        scenarioDataVisitor.setStartNode(startNode.getId());
        scenarioDataVisitor.setEndNode(endNode.getId());
    }

    public static class Builder {

        private String title;
        private HashMap<String, NodeData> nodeData = new HashMap<String, NodeData>();
        private ArrayList<TransitionData> transitionData = new ArrayList<TransitionData>();
        private String startNode;
        private String endNode;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder addNodeData(NodeData nodeData) {
            this.nodeData.put(nodeData.getId(), nodeData);
            return this;
        }

        public Builder addTransitionData(TransitionData transitionData) {
            this.transitionData.add(transitionData);
            return this;
        }

        public Builder setStartNode(String startNode) {
            this.startNode = startNode;
            return this;
        }

        public Builder setEndNode(String endNode) {
            this.endNode = endNode;
            return this;
        }

        public ScenarioData build() {
            return new ScenarioData(title, nodeData.values(), transitionData, nodeData.get(startNode), nodeData.get(endNode));
        }

    }

}