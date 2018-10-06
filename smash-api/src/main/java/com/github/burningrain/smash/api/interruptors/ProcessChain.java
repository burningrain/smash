package com.github.burningrain.smash.api.interruptors;

/**
 * @author burningrain on 10.09.2018.
 */
public class ProcessChain {

    private boolean interrupt;

    public void breakProcess() {
        interrupt = true;
    }

    public boolean isInterrupt() {
        return interrupt;
    }

}