package com.github.burningrain.smash.api.elements;

import com.github.burningrain.smash.api.ProcessContext;

/**
 * @author burningrain on 18.05.2018.
 */
public interface SmashElement<PC extends ProcessContext, OUTPUT> {

    OUTPUT process(PC input);

}