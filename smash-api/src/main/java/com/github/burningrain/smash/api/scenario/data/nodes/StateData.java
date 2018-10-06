package com.github.burningrain.smash.api.scenario.data.nodes;

import com.github.burningrain.smash.api.elements.SmashState;

/**
 * @author burningrain on 11.09.2018.
 */
public class StateData extends NodeData {

    public StateData(String id, String elementClass) {
        super(Type.STATE, id, elementClass);
    }

    public StateData(String id, Class<? extends SmashState> elementClass) {
        super(Type.STATE, id, elementClass);
    }

    public StateData(boolean passed, String id, String elementClass) {
        super(passed, Type.STATE, id, elementClass);
    }

}