package com.github.burningrain.smash.api.scenario.nodes;

import com.github.burningrain.smash.api.elements.SmashState;
import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;

/**
 * @author burningrain on 21.05.2018.
 */
public class StateNode extends Node<SmashState, NodeData> {

    private Node nexNode;

    public StateNode(final String id, final Class<SmashState> elementClass, final NodeData nodeData) {
        super(id, elementClass, nodeData);
    }

    public void setNextNode(Node nexNode) {
        this.nexNode = nexNode;
    }

    public Node nextNode() {
        return nexNode;
    }
}