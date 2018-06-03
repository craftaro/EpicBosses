package net.aminecraftdev.custombosses.utils.file;

import java.io.File;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public interface IFileHandler<ReturnObject> {

    ReturnObject loadFile(File file);

    void saveFile(File file, ReturnObject returnObject);

}
