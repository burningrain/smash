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

    private RoadType type = RoadType.CONTINUANCE;

    public Node(final String id, final Class<E> elementClass, final D nodeData) {
        this.id = id;
        this.elementClass = elementClass;
        this.nodeData = nodeData;
    }

    public enum RoadType {
        START,
        CONTINUANCE,
        END
    }

    public void setRoadType(RoadType type) {
        this.type = type;
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

    public boolean isStart() {
        return RoadType.START == type;
    }

    public boolean isEnd() {
        return RoadType.END == type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node<?, ?> node = (Node<?, ?>) o;

        return id.equals(node.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}