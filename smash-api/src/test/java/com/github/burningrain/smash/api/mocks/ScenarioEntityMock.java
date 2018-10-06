package com.github.burningrain.smash.api.mocks;

import com.github.burningrain.smash.api.ProcessStatus;
import com.github.burningrain.smash.api.entity.ScenarioEntity;

/**
 * @author burningrain on 05.09.2018.
 */
public class ScenarioEntityMock implements ScenarioEntity<Long> {

    private Long processId;
    private String scenarioTitle;
    private String currentStateId;
    private String scenario;
    private ProcessStatus processStatus;

    @Override
    public Long getProcessId() {
        return processId;
    }

    @Override
    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    @Override
    public String getScenarioTitle() {
        return scenarioTitle;
    }

    @Override
    public void setScenarioTitle(String title) {
        this.scenarioTitle = title;
    }

    @Override
    public void setCurrentStateId(String stateId) {
        this.currentStateId = stateId;
    }

    @Override
    public String getCurrentStateId() {
        return currentStateId;
    }

    @Override
    public void setStatus(ProcessStatus status) {
        this.processStatus = status;
    }

    @Override
    public ProcessStatus getStatus() {
        return processStatus;
    }

    @Override
    public String getScenario() {
        return scenario;
    }

    @Override
    public void setScenario(String scenario) {
        this.scenario =scenario;
    }

}