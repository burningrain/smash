package com.github.burningrain.smash.api.elements;

import com.github.burningrain.smash.api.ProcessContext;

/**
 * @author burningrain on 18.05.2018.
 */
public abstract class SmashState<PC extends ProcessContext, VIEW> implements SmashElement<PC, Void> {

    public final Void process(PC input) {
        processInput(input);
        return null;
    }

    public abstract void processInput(PC input);

    public abstract VIEW getView(PC processContext);

}