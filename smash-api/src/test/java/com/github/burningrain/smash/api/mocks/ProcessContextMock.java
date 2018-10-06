package com.github.burningrain.smash.api.mocks;

import com.github.burningrain.smash.api.ProcessContext;

import java.util.HashMap;

/**
 * @author burningrain on 05.09.2018.
 */
public class ProcessContextMock implements ProcessContext<Long> {

    private final long id;
    private HashMap<String, Object> map = new HashMap<>();

    public ProcessContextMock(long id) {
        this.id = id;
    }

    public <V> void put(String key, V value) {
        map.put(key, value);
    }

    public <V> V getByKey(String key) {
        return (V) map.get(key);
    }

    @Override
    public Long getProcessId() {
        return id;
    }


}