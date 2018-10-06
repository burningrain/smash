package com.github.burningrain.smash.api.interruptors;

import com.github.burningrain.smash.api.ProcessContext;

import java.util.ArrayList;

/**
 * @author burningrain on 10.09.2018.
 */
public class ProcessInterrupter<PC extends ProcessContext, R> {

    private ArrayList<Entry<PC, R>> listeners = new ArrayList<>();

    protected ProcessInterrupter(){}

    public ProcessInterrupter add(InterrupterListener<PC, R> listener, boolean isCanInterrupt) {
        listeners.add(new Entry<PC, R>(listener, isCanInterrupt));
        return this;
    }

    public void end(PC context, R object) {
        ProcessChain processChain = new ProcessChain();
        for (Entry<PC, R> entry : listeners) {
            InterrupterListener<PC, R> listener = entry.getListener();
            listener.execute(context, object, processChain);
            if(entry.isCanInterrupt() && processChain.isInterrupt()) {
                break;
            }
        }
    }

    private static class Entry<PC extends ProcessContext, R> {

        private InterrupterListener<PC, R> listener;
        private boolean canInterrupt;

        public Entry(InterrupterListener<PC, R> listener, boolean canInterrupt) {
            this.listener = listener;
            this.canInterrupt = canInterrupt;
        }

        public InterrupterListener<PC, R> getListener() {
            return listener;
        }

        public boolean isCanInterrupt() {
            return canInterrupt;
        }

    }

}