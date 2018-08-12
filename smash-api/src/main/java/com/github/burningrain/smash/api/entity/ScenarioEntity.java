package com.github.burningrain.smash.api.entity;

/**
 * @author burningrain on 10.10.2017.
 */
public interface ScenarioEntity<ID> {

    ID getProcessId();

    void setProcessId(ID processId);

    String getScenarioTitle();

    void setScenarioTitle(String title);

    void setCurrentStateId(String stateId);

    String getCurrentStateId();

    // -- для дебага --
    String getScenario();

    void setScenario(String scenario);
    // ----------------

}