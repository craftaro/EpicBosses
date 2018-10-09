package net.aminecraftdev.custombosses.container;

import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.utils.Debug;
import net.aminecraftdev.custombosses.utils.IContainer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Jul-18
 */
public class BossEntityContainer implements IContainer<Map<String, BossEntity>, String> {

    private Map<String, BossEntity> container = new HashMap<>();

    @Override
    public Map<String, BossEntity> getData() {
        return new HashMap<>(this.container);
    }

    @Override
    public boolean saveData(Map<String, BossEntity> stringBossEntityMap) {
        StringBuilder stringBuilder = new StringBuilder();
        int completed = 0;
        int failed = 0;

        for(Map.Entry<String, BossEntity> entry : stringBossEntityMap.entrySet()) {
            if(getData().containsKey(entry.getKey())) {
                failed += 1;
                stringBuilder.append(entry.getKey()).append("; ");
            }

            this.container.put(entry.getKey(), entry.getValue());
            completed++;
        }


        if(failed > 0) {
            Debug.BOSS_CONTAINER_SAVE.debug(completed, failed, stringBuilder.toString());
        }

        return true;
    }

    public boolean saveData(String string, BossEntity bossEntity) {
        return saveData(Collections.singletonMap(string, bossEntity));
    }

    @Override
    public void clearContainer() {
        this.container.clear();
    }

    @Override
    public boolean exists(String string) {
        return this.container.containsKey(string);
    }
}
