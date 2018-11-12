package com.songoda.epicbosses.container;

import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.IContainer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 12-Nov-18
 */
public class MinionEntityContainer implements IContainer<Map<String, MinionEntity>, String> {

    private Map<String, MinionEntity> container = new HashMap<>();

    @Override
    public Map<String, MinionEntity> getData() {
        return new HashMap<>(this.container);
    }

    public String getName(MinionEntity minionEntity) {
        for(Map.Entry<String, MinionEntity> entry : getData().entrySet()) {
            if(entry.getValue().equals(minionEntity)) return entry.getKey();
        }

        return null;
    }

    @Override
    public boolean saveData(Map<String, MinionEntity> stringMinionEntityMap) {
        StringBuilder stringBuilder = new StringBuilder();
        int completed = 0;
        int failed = 0;

        for(Map.Entry<String, MinionEntity> entry : stringMinionEntityMap.entrySet()) {
            if(getData().containsKey(entry.getKey())) {
                failed += 1;
                stringBuilder.append(entry.getKey()).append("; ");
                continue;
            }

            this.container.put(entry.getKey(), entry.getValue());
            completed++;
        }

        if(failed > 0) {
            Debug.MINION_CONTAINER_SAVE.debug(completed, failed, stringBuilder.toString());
        }

        return false;
    }

    public boolean saveData(String string, MinionEntity minionEntity) {
        return saveData(Collections.singletonMap(string, minionEntity));
    }

    @Override
    public void clearContainer() {
        this.container.clear();
    }

    @Override
    public boolean exists(String s) {
        return this.container.containsKey(s);
    }
}
