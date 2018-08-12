package com.github.burningrain.smash.api.scenario.nodes;

import com.github.burningrain.smash.api.elements.SmashPredicate;
import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;

/**
 * @author burningrain on 21.05.2018.
 */
public class PredicateNode extends Node<SmashPredicate, NodeData> {

    private Node yesNode;
    private Node noNode;

    public PredicateNode(final String id, final Class<SmashPredicate> elementClass, final NodeData predicateNodeData) {
        super(id, elementClass, predicateNodeData);
    }

    public void setYesNode(Node yesNode) {
        this.yesNode = yesNode;
    }

    public void setNoNode(Node noNode) {
        this.noNode = noNode;
    }

    public Node nextYes() {
        return yesNode;
    }

    public Node nextNo() {
        return noNode;
    }

}
