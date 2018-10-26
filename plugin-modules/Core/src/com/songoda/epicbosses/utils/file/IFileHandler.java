package com.songoda.epicbosses.utils.file;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public interface IFileHandler<ReturnObject> {

    void createFile();

    ReturnObject loadFile();

    void saveFile(ReturnObject returnObject);

}
