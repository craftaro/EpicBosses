package net.aminecraftdev.custombosses.utils.itemstack.repository;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public class ItemData {

    private final int itemNo;
    private final short itemData;

    protected ItemData(final int itemNo, final short itemData) {
        this.itemNo = itemNo;
        this.itemData = itemData;
    }

    private int getItemNo() {
        return itemNo;
    }

    private short getItemData() {
        return itemData;
    }

    @Override
    public int hashCode() {
        return (31 * itemNo) ^ itemData;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof ItemData)) {
            return false;
        }

        ItemData pairo = (ItemData) o;
        return this.itemNo == pairo.getItemNo() && this.itemData == pairo.getItemData();
    }

}
