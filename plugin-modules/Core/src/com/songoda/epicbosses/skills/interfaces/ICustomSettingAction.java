package com.songoda.epicbosses.skills.interfaces;

import com.songoda.epicbosses.utils.panel.base.ClickAction;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 20-Dec-18
 */
public interface ICustomSettingAction {

    ClickAction getAction();

    String getSettingName();

    ItemStack getDisplayItemStack();

    String getCurrent();

    List<String> getExtraInformation();
}
