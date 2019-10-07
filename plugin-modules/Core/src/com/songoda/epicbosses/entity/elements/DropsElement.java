package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class DropsElement {

    @Expose
    private Boolean naturalDrops, dropExp;
    @Expose
    private String dropTable;

    public DropsElement(Boolean naturalDrops, Boolean dropExp, String dropTable) {
        this.naturalDrops = naturalDrops;
        this.dropExp = dropExp;
        this.dropTable = dropTable;
    }

    public Boolean getNaturalDrops() {
        return this.naturalDrops;
    }

    public void setNaturalDrops(Boolean naturalDrops) {
        this.naturalDrops = naturalDrops;
    }

    public Boolean getDropExp() {
        return this.dropExp;
    }

    public void setDropExp(Boolean dropExp) {
        this.dropExp = dropExp;
    }

    public String getDropTable() {
        return this.dropTable;
    }

    public void setDropTable(String dropTable) {
        this.dropTable = dropTable;
    }
}
