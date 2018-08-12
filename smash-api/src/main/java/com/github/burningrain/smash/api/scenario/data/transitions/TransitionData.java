package com.github.burningrain.smash.api.scenario.data.transitions;

import com.github.burningrain.smash.api.scenario.data.ScenarioDataVisitor;
import com.github.burningrain.smash.api.scenario.data.ScenarioElement;

/**
 * @author burningrain on 23.05.2018.
 */
public class TransitionData extends ScenarioElement {

    public enum Type {
        SIMPLE,
        PREDICATE_YES,
        PREDICATE_NO
    }

    private final String sourceNodeId;
    private final String destNodeId;
    private final Type type;

    private TransitionData(Type type, final String sourceNodeId, final String destNodeId) {
        this(type, sourceNodeId, destNodeId, false);
    }

    private TransitionData(Type type, final String sourceNodeId, final String destNodeId, final boolean passed) {
        super(passed);
        this.sourceNodeId = sourceNodeId;
        this.destNodeId = destNodeId;
        this.type = type;
    }

    public static TransitionData of(final Type type, final String sourceNodeId, final String destNodeId) {
        return new TransitionData(type, sourceNodeId, destNodeId);
    }

    public static TransitionData of(final Type type, final String sourceNodeId, final String destNodeId, final boolean passed) {
        return new TransitionData(type, sourceNodeId, destNodeId, passed);
    }

    public String getSourceNodeId() {
        return sourceNodeId;
    }

    public String getDestNodeId() {
        return destNodeId;
    }

    public Type getType() {
        return type;
    }

    public void accept(ScenarioDataVisitor visitor) {
        visitor.visit(this);
    }

}
