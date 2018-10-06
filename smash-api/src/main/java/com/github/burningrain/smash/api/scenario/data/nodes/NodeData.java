package com.github.burningrain.smash.api.scenario.data.nodes;

import com.github.burningrain.smash.api.scenario.data.ScenarioDataVisitor;
import com.github.burningrain.smash.api.elements.SmashElement;

/**
 * @author burningrain on 23.05.2018.
 */
public abstract class NodeData {

    private final String id;
    private final String elementClass;
    private final Type type;

    public enum Type {
        PREDICATE,
        STATE
    }

    public NodeData(final Type type, final String id, final String elementClass) {
        this(false, type, id, elementClass);
    }

    public NodeData(final Type type, final String id, final Class<? extends SmashElement> elementClass) {
        this(type, id, elementClass.getName());
    }

    public NodeData(final boolean passed, final Type type, final String id, final String elementClass) {
        this.id = id;
        this.elementClass = elementClass;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getElementClass() {
        return elementClass;
    }

    public Type getType() {
        return type;
    }

    public void accept(ScenarioDataVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeData nodeData = (NodeData) o;

        return id.equals(nodeData.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}