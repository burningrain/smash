package com.github.burningrain.smash.api;

import com.github.burningrain.smash.api.elements.SmashElement;

/**
 * @author burningrain on 18.05.2018.
 */
public interface SmashElementContext {

    <T extends SmashElement> T getScenarioElement(Class<T> clazz);

}