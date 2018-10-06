package com.github.burningrain.smash.api.entity;

import com.github.burningrain.smash.api.ProcessStatus;

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

    void setStatus(ProcessStatus status);

    ProcessStatus getStatus();



    // -- для дебага --
    String getScenario();

    void setScenario(String scenario);
    // ----------------

}