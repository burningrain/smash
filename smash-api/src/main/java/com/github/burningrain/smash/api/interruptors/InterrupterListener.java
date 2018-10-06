package com.github.burningrain.smash.api.interruptors;

import com.github.burningrain.smash.api.ProcessContext;

/**
 * @author burningrain on 10.09.2018.
 */
public interface InterrupterListener <PC extends ProcessContext, R> {

    void execute(PC context, R object, ProcessChain processChain);

}