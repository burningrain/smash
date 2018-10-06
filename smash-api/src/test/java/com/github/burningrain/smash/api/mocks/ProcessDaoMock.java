package com.github.burningrain.smash.api.mocks;

import com.github.burningrain.smash.api.entity.ProcessDao;

import java.util.HashMap;

/**
 * @author burningrain on 05.09.2018.
 */
public class ProcessDaoMock implements ProcessDao<Long, ScenarioEntityMock> {

    private HashMap<Long, ScenarioEntityMock> map = new HashMap<>();

    @Override
    public void createOrUpdateScenario(ScenarioEntityMock processEntity) {
        map.put(processEntity.getProcessId(), processEntity);
    }

    @Override
    public ScenarioEntityMock getScenarioById(Long processId) {
        return map.get(processId);
    }

}