package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class DropsElement {

    @Expose @Getter @Setter private Boolean naturalDrops, dropExp;
    @Expose @Getter @Setter private String dropTable;

    public DropsElement(Boolean naturalDrops, Boolean dropExp, String dropTable) {
        this.naturalDrops = naturalDrops;
        this.dropExp = dropExp;
        this.dropTable = dropTable;
    }

}
