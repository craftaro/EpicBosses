package com.songoda.epicbosses.handlers.droptable;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.handlers.IGetDropTableListItem;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Oct-18
 */
public class GetDropTableItemStack implements IGetDropTableListItem<ItemStack> {

    private ItemsFileManager itemsFileManager;

    public GetDropTableItemStack(EpicBosses plugin) {
        this.itemsFileManager = plugin.getItemStackManager();
    }

    @Override
    public ItemStack getListItem(String id) {
        ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack(id);

        if(itemStackHolder == null) {
            Debug.FAILED_TO_LOAD_CUSTOM_ITEM.debug(id);
            return null;
        }

        ItemStack itemStack = this.itemsFileManager.getItemStackConverter().from(itemStackHolder);

        if(itemStack == null) {
            Debug.FAILED_TO_LOAD_CUSTOM_ITEM.debug(id);
            return null;
        }

        return itemStack;
    }
}
