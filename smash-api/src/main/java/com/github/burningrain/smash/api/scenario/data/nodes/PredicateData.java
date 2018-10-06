package com.github.burningrain.smash.api.scenario.data.nodes;

import com.github.burningrain.smash.api.elements.SmashElement;
import com.github.burningrain.smash.api.elements.SmashPredicate;

/**
 * @author burningrain on 11.09.2018.
 */
public class PredicateData extends NodeData {

    public PredicateData(String id, String elementClass) {
        super(Type.PREDICATE, id, elementClass);
    }

    public PredicateData(String id, Class<? extends SmashPredicate> elementClass) {
        super(Type.PREDICATE, id, elementClass);
    }

    public PredicateData(boolean passed, String id, String elementClass) {
        super(passed, Type.PREDICATE, id, elementClass);
    }

}