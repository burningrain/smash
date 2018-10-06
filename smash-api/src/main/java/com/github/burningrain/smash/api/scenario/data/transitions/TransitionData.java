package com.github.burningrain.smash.api.scenario.data.transitions;

import com.github.burningrain.smash.api.scenario.data.ScenarioDataVisitor;

/**
 * @author burningrain on 23.05.2018.
 */
public class TransitionData {

    public enum Type {
        SIMPLE,
        PREDICATE_YES,
        PREDICATE_NO
    }

    private final String sourceNodeId;
    private final String destNodeId;
    private final Type type;

    private TransitionData(Type type, final String sourceNodeId, final String destNodeId) {
        this.sourceNodeId = sourceNodeId;
        this.destNodeId = destNodeId;
        this.type = type;
    }

    public static TransitionData of(final Type type, final String sourceNodeId, final String destNodeId) {
        return new TransitionData(type, sourceNodeId, destNodeId);
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
