package com.github.burningrain.smash.api.mocks;

import com.github.burningrain.smash.api.SmashElementContext;
import com.github.burningrain.smash.api.elements.SmashElement;

import java.util.HashMap;
import java.util.Map;

/**
 * @author burningrain on 05.09.2018.
 */
public class SmashElementContextMock implements SmashElementContext {

    private Map<Class<? extends SmashElement>, SmashElement> context;

    private SmashElementContextMock(Map<Class<? extends SmashElement>, SmashElement> context) {
        this.context = context;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public <T extends SmashElement> T getScenarioElement(Class<T> clazz) {
        return (T) context.get(clazz);
    }


    public static class Builder {

        private HashMap<Class<? extends SmashElement>, SmashElement> context = new HashMap<>();

        public Builder add(SmashElement smashElement) {
            context.put(smashElement.getClass(), smashElement);
            return this;
        }

        public SmashElementContextMock build() {
            return new SmashElementContextMock(context);
        }

    }


}