package net.aminecraftdev.custombosses.utils;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Jul-18
 */
public interface IContainer<StorageType, KeyType> {

    StorageType getData();

    boolean saveData(StorageType storageType);

    void clearContainer();

    boolean exists(KeyType keyType);

}
