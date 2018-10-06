package com.github.burningrain.smash.api.scenario.builder;

import com.github.burningrain.smash.api.scenario.data.ScenarioData;
import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;
import com.github.burningrain.smash.api.scenario.data.nodes.PredicateData;
import com.github.burningrain.smash.api.scenario.data.transitions.TransitionData;

import java.util.ArrayList;

/**
 * @author burningrain on 30.05.2018.
 */
public class SmashBuilder {

    private ScenarioData.Builder scenarioBuilder = new ScenarioData.Builder();

    private String startNode;
    private ArrayList<String> endNodes = new ArrayList<>();
    private ArrayList<String> goOnNodes = new ArrayList<>();

    private NodeData currentNode;

    private SmashBuilder(String scenarioTitle) {
        this.scenarioBuilder.setTitle(scenarioTitle);
    }

    public static SmashBuilder begin(String scenarioTitle) {
        return new SmashBuilder(scenarioTitle);
    }

    public ScenarioData end() {
        return scenarioBuilder
                .setStartNode(startNode)
                .setEndNodes(endNodes)
                .setFrontierNodes(goOnNodes)
                .build();
    }

    public SmashBuilder exit() {
        endNodes.add(currentNode.getId());
        return this;
    }

    public SmashBuilder goOn() {
        goOnNodes.add(currentNode.getId());
        return this;
    }
//
//    public SmashBuilder subScenario(String scenarioTitle) {
//
//    }
//
//    public SmashBuilder anyState() {
//
//    }

    public SmashBuilder state(NodeData nodeData) {
        setStartNodeIfNeed(nodeData);
        scenarioBuilder.addNodeData(nodeData);

        createTransitionIfCurrentNodeExist(nodeData, TransitionData.Type.SIMPLE);
        setCurrentNode(nodeData);
        return this;
    }

    public PredicateBuilder predicate(PredicateData nodeData) {
        setStartNodeIfNeed(nodeData);
        scenarioBuilder.addNodeData(nodeData);
        createTransitionIfCurrentNodeExist(nodeData, TransitionData.Type.SIMPLE);
        setCurrentNode(null);
        return new PredicateBuilder(nodeData, this);
    }

    private void addTransition(TransitionData transitionData) {
        scenarioBuilder.addTransitionData(transitionData);
    }

    private void setCurrentNode(NodeData currentNode) {
        this.currentNode = currentNode;
    }

    private void setStartNodeIfNeed(NodeData nodeData) {
        if(startNode == null) {
            startNode = nodeData.getId();
        }
    }

    private void createTransitionIfCurrentNodeExist(NodeData nodeData, TransitionData.Type type) {
        if(currentNode != null) {
            addTransition(TransitionData.of(type, currentNode.getId(), nodeData.getId()));
        }
    }

    public static class PredicateBuilder {

        private SmashBuilder smashBuilder;
        private NodeData predicateNode;
        private ScenarioData noData;

        public PredicateBuilder(NodeData predicateNode, SmashBuilder smashBuilder) {
            this.predicateNode = predicateNode;
            this.smashBuilder = smashBuilder;
        }

        public PredicateBuilder no(ScenarioData noData) {
            this.noData = noData;
            return this;
        }

        public SmashBuilder yes(ScenarioData yesData) {
            smashBuilder.scenarioBuilder.addScenarioData(noData);
            smashBuilder.scenarioBuilder.addScenarioData(yesData);

            smashBuilder.scenarioBuilder.addTransitionData(TransitionData.of(TransitionData.Type.PREDICATE_NO, predicateNode.getId(), noData.getTitle()));
            smashBuilder.scenarioBuilder.addTransitionData(TransitionData.of(TransitionData.Type.PREDICATE_YES, predicateNode.getId(), yesData.getTitle()));

            //fixme убрать это, не нужно smashBuilder.setCurrentNode(null);

            return smashBuilder;
        }

    }


}