package com.github.burningrain.smash.api.scenario.builder;

import com.github.burningrain.smash.api.scenario.data.ScenarioData;
import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;
import com.github.burningrain.smash.api.scenario.data.transitions.TransitionData;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author burningrain on 30.05.2018.
 */
public class SmashBuilder {

    private final String title;
    private LinkedHashMap<String, NodeData> nodes = new LinkedHashMap<String, NodeData>();
    private ArrayList<TransitionData> transitions = new ArrayList<TransitionData>();
    private NodeData startNode;
    private NodeData endNode;

    private NodeData prevNode;

    public SmashBuilder(String scenarioTitle) {
        this.title = scenarioTitle;
    }

    public StateBuilder state(NodeData nodeData) {
        if(nodes.isEmpty()) {
            startNode = nodeData;
        }
        nodes.put(nodeData.getId(), nodeData);

        createTransitionIfNeed(nodeData, TransitionData.Type.SIMPLE);
        return new StateBuilder(nodeData, this);
    }

    public PredicateBuilder predicate(NodeData nodeData) {
        nodes.put(nodeData.getId(), nodeData);
        createTransitionIfNeed(nodeData, TransitionData.Type.SIMPLE);
        setPrevNode(null);
        return new PredicateBuilder(nodeData, this);
    }

    public ScenarioData build() {
        return new ScenarioData(title, nodes.values(), transitions, startNode, endNode);
    }

    private void addTransition(TransitionData transitionData) {
        transitions.add(transitionData);
    }

    private void setPrevNode(NodeData prevNode) {
        this.prevNode = prevNode;
    }

    private void createTransitionIfNeed(NodeData nodeData, TransitionData.Type type) {
        if(prevNode != null) {
            addTransition(TransitionData.of(type, prevNode.getId(), nodeData.getId()));
        }
    }

    public static class StateBuilder {

        private SmashBuilder smashBuilder;
        private NodeData nodeData;

        public StateBuilder(NodeData nodeData, SmashBuilder smashBuilder) {
            this.smashBuilder = smashBuilder;
            this.nodeData = nodeData;
        }

        public SmashBuilder to() {
            smashBuilder.setPrevNode(nodeData);
            return smashBuilder;
        }

        public SmashBuilder alone() {
            smashBuilder.endNode = nodeData;
            return smashBuilder;
        }

    }

    public static class PredicateBuilder {

        private SmashBuilder smashBuilder;
        private NodeData predicateNode;
        private NodeData no;

        public PredicateBuilder(NodeData predicateNode, SmashBuilder smashBuilder) {
            this.predicateNode = predicateNode;
            this.smashBuilder = smashBuilder;
        }

        public PredicateBuilder no(NodeData nodeData) {
            smashBuilder.state(nodeData);
            no = nodeData;
            return this;
        }

        public SmashBuilder yes(NodeData yes) {
            smashBuilder.state(yes);
            smashBuilder.addTransition(TransitionData.of(TransitionData.Type.PREDICATE_YES, predicateNode.getId(), yes.getId()));
            smashBuilder.addTransition(TransitionData.of(TransitionData.Type.PREDICATE_NO, predicateNode.getId(), no.getId()));
            return smashBuilder;
        }

    }


}