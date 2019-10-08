package com.songoda.epicbosses.managers.files;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.file.SkillsFileHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.utils.ILoadable;
import com.songoda.epicbosses.utils.ISavable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 13-Nov-18
 */
public class SkillsFileManager implements ILoadable, ISavable {

    private Map<String, Skill> skillMap = new HashMap<>();
    private SkillsFileHandler skillsFileHandler;

    public SkillsFileManager(EpicBosses plugin) {
        File file = new File(plugin.getDataFolder(), "skills.json");

        this.skillsFileHandler = new SkillsFileHandler(plugin, true, file);
    }

    @Override
    public void load() {
        this.skillMap = this.skillsFileHandler.loadFile();
    }

    public void reload() {
        load();
    }

    @Override
    public void save() {
        this.skillsFileHandler.saveFile(this.skillMap);
    }

    public void saveSkill(String name, Skill skill) {
        if (this.skillMap.containsKey(name)) return;

        this.skillMap.put(name, skill);
        save();
    }

    public Skill getSkill(String name) {
        return this.skillMap.getOrDefault(name, null);
    }

    public Map<String, Skill> getSkillMap() {
        return new HashMap<>(this.skillMap);
    }
}
