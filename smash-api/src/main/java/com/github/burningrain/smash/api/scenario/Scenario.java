package com.github.burningrain.smash.api.scenario;

import com.github.burningrain.smash.api.elements.SmashPredicate;
import com.github.burningrain.smash.api.scenario.data.ScenarioData;
import com.github.burningrain.smash.api.scenario.nodes.StateNode;
import com.github.burningrain.smash.api.ProcessContext;
import com.github.burningrain.smash.api.elements.SmashElement;
import com.github.burningrain.smash.api.elements.SmashState;
import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;
import com.github.burningrain.smash.api.scenario.data.transitions.TransitionData;
import com.github.burningrain.smash.api.scenario.nodes.Node;
import com.github.burningrain.smash.api.scenario.nodes.PredicateNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author burningrain on 18.05.2018.
 */
public class Scenario {

    private Node currentNode;
    private ScenarioData scenarioData;

    private Scenario(Node currentNode, ScenarioData scenarioData) {
        this.currentNode = currentNode;
        this.scenarioData = scenarioData;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public ScenarioData getScenarioData() {
        return scenarioData;
    }

    public <PC extends ProcessContext> Node next(SmashElement scenarioElement, PC processContext) {
        currentNode.getNodeData().markPassed();
        switch (currentNode.getType()) {
            case PREDICATE:
                PredicateNode predicateNode = (PredicateNode) currentNode;
                SmashPredicate<ProcessContext> predicateElement = (SmashPredicate) scenarioElement;
                currentNode = predicateElement.process(processContext)? predicateNode.nextYes() : predicateNode.nextNo();
                break;
            case STATE:
                StateNode stateNode = (StateNode) currentNode;
                currentNode = stateNode.nextNode();
                break;
            default:
                throw new IllegalStateException(String.format("Некорректный тип ноды [%s] для обработки", currentNode.getType()));
        }
        return currentNode;
    }

    public static class Builder {

        private HashMap<String, Node> nodes = new HashMap<String, Node>();

        private Node startNode = null;
        private Node endNode = null;
        private Node currentNode = null;

        private ScenarioData scenarioData = null;
        private TransitionsWrapper transitionsWrapper = new TransitionsWrapper();


        private static class TransitionsWrapper {
            private HashMap<String, TransitionsDataWrapper> map = new HashMap<String, TransitionsDataWrapper>();

            public void addTransition(String sourceNodeId, TransitionData transitionData) {
                TransitionsDataWrapper transitionsDataWrapper = map.get(sourceNodeId);
                if(transitionsDataWrapper == null) {
                    transitionsDataWrapper = new TransitionsDataWrapper();
                    map.put(sourceNodeId, transitionsDataWrapper);
                }
                transitionsDataWrapper.addTransition(transitionData.getType(), transitionData);
            }

            public List<TransitionData> getTransitions(String sourceNodeId, TransitionData.Type type) {
                return map.get(sourceNodeId).getTransitions(type);
            }

        }

        private static class TransitionsDataWrapper {
            private HashMap<TransitionData.Type, List<TransitionData>> map = new HashMap<TransitionData.Type, List<TransitionData>>();

            private void addTransition(TransitionData.Type type, TransitionData transitionData) {
                List<TransitionData> dataList = map.get(type);
                if(dataList == null) {
                    dataList = new LinkedList<TransitionData>();
                    map.put(type, dataList);
                }
                dataList.add(transitionData);
            }

            private List<TransitionData> getTransitions(TransitionData.Type type) {
                return map.get(type);
            }

        }


        public void addNode(NodeData nodeData) {
            nodes.put(nodeData.getId(), createNode(nodeData));
        }

        public void addTransition(TransitionData transitionData) {
            transitionsWrapper.addTransition(transitionData.getSourceNodeId(), transitionData);
        }

        private void addStateTransition(TransitionData transitionData) {
            StateNode sourceNode = checkNodeExist(transitionData.getSourceNodeId());
            Node destNode = checkNodeExist(transitionData.getDestNodeId());
            sourceNode.setNextNode(destNode);
        }

        private void addPredicateTransition(String predicateId, String yesNodeId, String noNodeId) {
            PredicateNode nodePredicate = checkNodeExist(predicateId);
            final Node yesNode = checkNodeExist(yesNodeId);
            final Node noNode = checkNodeExist(noNodeId);
            nodePredicate.setYesNode(yesNode);
            nodePredicate.setNoNode(noNode);
        }

        public void setStartNode(String id) {
            this.startNode = checkNodeExist(id);
        }

        public void setEndNode(String id) {
            this.endNode = checkNodeExist(id);
        }

        public void setCurrentNode(String nodeId) {
            if(nodeId == null) return;
            this.currentNode = checkNodeExist(nodeId);
        }

        public void setScenarioData(ScenarioData scenarioData) {
            this.scenarioData = scenarioData;
        }

        public Scenario build() {
            for (Node node : nodes.values()) {
                switch (node.getType()) {
                    case STATE:
                        if(endNode.getId().equals(node.getId())) {
                            continue;
                        }
                        final List<TransitionData> transitions = transitionsWrapper.getTransitions(node.getId(), TransitionData.Type.SIMPLE);
                        addStateTransition(transitions.get(0));
                        break;
                    case PREDICATE:
                        final TransitionData yesData = transitionsWrapper.getTransitions(node.getId(), TransitionData.Type.PREDICATE_YES).get(0);
                        final TransitionData noData = transitionsWrapper.getTransitions(node.getId(), TransitionData.Type.PREDICATE_NO).get(0);
                        addPredicateTransition(yesData.getSourceNodeId(), yesData.getDestNodeId(), noData.getDestNodeId());
                        break;
                    default:
                        throw  new RuntimeException(String.format("Неизвестный тип ноды [%s]", node.getType()));
                }
            }


            if(startNode == null) throw new IllegalStateException("Начальная нода сценария обязательно должна быть выбрана");
            if(currentNode == null) {
                currentNode = startNode;
                //todo вывести в лог, что в качестве текущего состояния выбрано начальное
            }
            //todo всякие проверки
            return new Scenario(currentNode, scenarioData);
        }

        private <T extends Node> T checkNodeExist(String id) {
            final Node node = nodes.get(id);
            if(node == null) throw new IllegalStateException(String.format("Нода [%s] не добавлена к графу состояний", id));
            else return (T)node;
        }

        private static Node createNode(NodeData nodeData) {
            switch (nodeData.getType()) {
                case STATE:
                    return new StateNode(nodeData.getId(), (Class<SmashState>) getClass(nodeData.getElementClass()), nodeData);
                case PREDICATE:
                    return new PredicateNode(nodeData.getId(), (Class<SmashPredicate>) getClass(nodeData.getElementClass()), nodeData);
                default:
                    throw new IllegalArgumentException(String.format("Тип ноды [%s] не поддерживается", nodeData.getType()));
            }
        }

        private static Class<? extends SmashElement> getClass(String clazz) {
            try {
                return (Class<? extends SmashElement>) Class.forName(clazz);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

}