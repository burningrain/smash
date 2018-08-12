package com.github.burningrain.smash.api.scenario.data.nodes;

import com.github.burningrain.smash.api.scenario.data.ScenarioDataVisitor;
import com.github.burningrain.smash.api.scenario.data.ScenarioElement;
import com.github.burningrain.smash.api.elements.SmashElement;

/**
 * @author burningrain on 23.05.2018.
 */
public class NodeData extends ScenarioElement {

    private final String id;
    private final String elementClass;
    private final Type type;

    public enum Type {
        PREDICATE,
        STATE
    }

    private NodeData(final Type type, final String id, final String elementClass) {
        this(false, type, id, elementClass);
    }

    private NodeData(final boolean passed, final Type type, final String id, final String elementClass) {
        super(passed);
        this.id = id;
        this.elementClass = elementClass;
        this.type = type;
    }

    public static NodeData of(final Type type, final String id, final Class<? extends SmashElement> elementClass) {
        return of(type, id, elementClass.getName());
    }

    public static NodeData of(final Type type, final String id, final String elementClass) {
        return new NodeData(type, id, elementClass);
    }

    public static NodeData of(final boolean passed, final Type type, final String id, final String elementClass) {
        return new NodeData(passed, type, id, elementClass);
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
}
