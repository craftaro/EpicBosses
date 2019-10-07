package com.songoda.epicbosses.managers.files;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.file.MessagesFileHandler;
import com.songoda.epicbosses.utils.ILoadable;
import com.songoda.epicbosses.utils.ISavable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 17-Oct-18
 */
public class MessagesFileManager implements ILoadable, ISavable {

    private Map<String, List<String>> messagesMap = new HashMap<>();
    private MessagesFileHandler messagesFileHandler;

    public MessagesFileManager(EpicBosses epicBosses) {
        File file = new File(epicBosses.getDataFolder(), "messages.json");

        this.messagesFileHandler = new MessagesFileHandler(epicBosses, true, file);
    }

    @Override
    public void load() {
        this.messagesMap = this.messagesFileHandler.loadFile();
    }

    public void reload() {
        load();
    }

    @Override
    public void save() {
        this.messagesFileHandler.saveFile(this.messagesMap);
    }

    public List<String> getMessages(String id) {
        return this.messagesMap.getOrDefault(id, null);
    }

    public Map<String, List<String>> getMessagesMap() {
        return new HashMap<>(this.messagesMap);
    }

    public boolean addNewMessage(String id, List<String> message) {
        if(this.messagesMap.containsKey(id)) return false;

        messagesMap.put(id, message);
        return true;
    }

    public void removeMessage(String id) {
        messagesMap.remove(id);
    }
}