package com.github.burningrain.smash.api.scenario.nodes;

import com.github.burningrain.smash.api.scenario.data.nodes.NodeData;
import com.github.burningrain.smash.api.elements.SmashElement;

/**
 * @author burningrain on 21.05.2018.
 */
public abstract class Node<E extends SmashElement, D extends NodeData> {

    private final String id;
    private final Class<E> elementClass;
    private final D nodeData;

    public Node(final String id, final Class<E> elementClass, final D nodeData) {
        this.id = id;
        this.elementClass = elementClass;
        this.nodeData = nodeData;
    }

    public String getId() {
        return id;
    }

    public Class<E> getElementClass() {
        return elementClass;
    }

    public D getNodeData() {
        return nodeData;
    }

    public NodeData.Type getType() {
        return nodeData.getType();
    }
}
