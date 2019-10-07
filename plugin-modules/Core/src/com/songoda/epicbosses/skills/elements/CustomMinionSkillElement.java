package com.songoda.epicbosses.skills.elements;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.skills.interfaces.IOtherSkillDataElement;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 13-Nov-18
 */
public class CustomMinionSkillElement implements IOtherSkillDataElement {

    @Expose
    private String minionToSpawn;
    @Expose
    private Integer amount;

    public CustomMinionSkillElement(Integer amount, String minionToSpawn) {
        this.amount = amount;
        this.minionToSpawn = minionToSpawn;
    }

    public String getMinionToSpawn() {
        return this.minionToSpawn;
    }

    public void setMinionToSpawn(String minionToSpawn) {
        this.minionToSpawn = minionToSpawn;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
