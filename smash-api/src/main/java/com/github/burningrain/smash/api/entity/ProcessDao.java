package com.github.burningrain.smash.api.entity;

/**
 * @author burningrain on 22.05.2018.
 */
public interface ProcessDao<ID, SE extends ScenarioEntity> {

    void createOrUpdateScenario(SE processEntity);

    SE getScenarioById(ID processId);
}