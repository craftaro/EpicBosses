package com.songoda.epicbosses.managers.files;

import lombok.Getter;
import com.songoda.epicbosses.utils.ILoadable;
import com.songoda.epicbosses.utils.IReloadable;
import com.songoda.epicbosses.utils.ISavable;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.file.ItemStackFileHandler;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jun-18
 */
public class ItemsFileManager implements ILoadable, ISavable, IReloadable {

    @Getter private final ItemStackConverter itemStackConverter = new ItemStackConverter();

    private Map<String, ItemStackHolder> itemStackHolders = new HashMap<>();
    private ItemStackFileHandler itemStackFileHandler;

    public ItemsFileManager(JavaPlugin javaPlugin) {
        File file = new File(javaPlugin.getDataFolder(), "items.json");

        this.itemStackFileHandler = new ItemStackFileHandler(javaPlugin, file, true);
    }

    @Override
    public void load() {
        this.itemStackHolders = this.itemStackFileHandler.loadFile();
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public void save() {
        this.itemStackFileHandler.saveFile(this.itemStackHolders);
    }

    public ItemStackHolder getItemStackHolder(String name) {
        return this.itemStackHolders.getOrDefault(name, null);
    }

    public Map<String, ItemStackHolder> getItemStackHolders() {
        return new HashMap<>(this.itemStackHolders);
    }

}
