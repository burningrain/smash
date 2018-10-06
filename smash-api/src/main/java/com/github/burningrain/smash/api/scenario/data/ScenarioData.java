package com.github.burningrain.smash.api.scenario.data;

import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;
import com.github.burningrain.smash.api.scenario.data.transitions.TransitionData;

import java.util.*;

/**
 * @author burningrain on 24.05.2018.
 */
public class ScenarioData {

    private final String title;
    private final Collection<NodeData> nodeData;
    private final Collection<TransitionData> transitionData;
    private final String startNodeId;
    private final Collection<String> endNodes;
    private final Collection<String> frontierNodes;

    public ScenarioData(final String title,
                        final Collection<NodeData> nodeData,
                        final Collection<TransitionData> transitionData,
                        final String startNodeId,
                        final Collection<String> endNodes,
                        final Collection<String> frontierNodes
                        ) {
        this.title = title;
        this.nodeData = nodeData;
        this.transitionData = transitionData;
        this.startNodeId = startNodeId;
        this.endNodes = endNodes;
        this.frontierNodes = frontierNodes;
    }

    public String getTitle() {
        return title;
    }

    public String getStartNodeId() {
        return startNodeId;
    }

    public Collection<String> getEndNodes() {
        return Collections.unmodifiableCollection(endNodes);
    }

    public void accept(ScenarioDataVisitor scenarioDataVisitor) {
        scenarioDataVisitor.setScenarioTitle(title);
        for (NodeData nodeData : this.nodeData) {
            nodeData.accept(scenarioDataVisitor);
        }

        scenarioDataVisitor.visitStartNode(startNodeId);
        scenarioDataVisitor.visitEndNodes(endNodes);
        scenarioDataVisitor.visitFrontierNodes(frontierNodes);

        for (TransitionData transitionData : this.transitionData) {
            transitionData.accept(scenarioDataVisitor);
        }
    }

    public static class Builder {

        private String title;
        private HashMap<String, NodeData> nodeData = new HashMap<String, NodeData>();
        private ArrayList<TransitionData> transitionData = new ArrayList<TransitionData>();
        private String startNode;
        private Collection<String> endNodes;
        private Collection<String> frontierNodes = new HashSet<>();
        private Map<String, ScenarioData> scenarioData = new HashMap<>();

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder addNodeData(NodeData nodeData) {
            this.nodeData.put(nodeData.getId(), nodeData);
            return this;
        }

        public Builder addScenarioData(ScenarioData scenarioData) {
            this.scenarioData.put(scenarioData.getTitle(), scenarioData);
            return this;
        }

        public Builder addTransitionData(TransitionData transitionData) {
            this.transitionData.add(transitionData);
            if(TransitionData.Type.PREDICATE_NO == transitionData.getType() || TransitionData.Type.PREDICATE_YES == transitionData.getType()) {
                frontierNodes.add(transitionData.getSourceNodeId());
            }

            return this;
        }

        public Builder setStartNode(String startNode) {
            this.startNode = startNode;
            return this;
        }

        public Builder setEndNodes(Collection<String> endNodes) {
            this.endNodes = endNodes;
            return this;
        }

        public Builder setFrontierNodes(Collection<String> frontierNodes) {
            this.frontierNodes.addAll(frontierNodes);
            return this;
        }

        public ScenarioData build() {
            // мержим подсценарии в основной
            for (ScenarioData data : scenarioData.values()) {
                SubScenarioDataMerger dataMerger = new SubScenarioDataMerger(this);
                data.accept(dataMerger);
            }

            for (ScenarioData data : scenarioData.values()) {
                String subScenarioTitle = data.getTitle();

                int size = transitionData.size();
                int i = size - 1;

                //fixme очень неоптимально.
                while (i > -1) {
                    TransitionData transitionDatum = this.transitionData.get(i);

                    String sourceNodeId = transitionDatum.getSourceNodeId();
                    String destNodeId = transitionDatum.getDestNodeId();

                    if (sourceNodeId.equals(subScenarioTitle)) {
                        // переход из подсценария в основной процесс
                        this.transitionData.remove(transitionDatum);
                        for (String frontierNode : frontierNodes) {
                            this.transitionData.add(TransitionData.of(transitionDatum.getType(), frontierNode, destNodeId));
                        }
                    } else if (destNodeId.equals(subScenarioTitle)) {
                        // переход из сценария в подсценарий
                        this.transitionData.set(i, TransitionData.of(transitionDatum.getType(), sourceNodeId, data.getStartNodeId()));
                    }
                    i--;
                }
            }


            // todo что с валидацией?
            return new ScenarioData(title, nodeData.values(), transitionData, startNode, endNodes, this.frontierNodes);
        }

    }

    private static class SubScenarioDataMerger implements ScenarioDataVisitor {

        private Builder mainScenarioData;

        public SubScenarioDataMerger(Builder mainScenarioData) {
            this.mainScenarioData = mainScenarioData;
        }

        @Override
        public void setScenarioTitle(String title) {

        }

        @Override
        public void visit(NodeData nodeData) {
            mainScenarioData.nodeData.put(nodeData.getId(), nodeData);
        }

        @Override
        public void visitStartNode(String startNodeId) {

        }

        @Override
        public void visitEndNodes(Collection<String> endNodes) {
            mainScenarioData.endNodes.addAll(endNodes);
        }

        @Override
        public void visitFrontierNodes(Collection<String> frontierNodes) {

        }

        @Override
        public void visit(TransitionData transitionData) {
            mainScenarioData.transitionData.add(transitionData);
        }

    }

}