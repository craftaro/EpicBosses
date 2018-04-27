package net.aminecraftdev.custombosses.entity.components;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 24-Apr-18
 */
public class EditorComponent {

    private boolean editable = false;

    public void setIsEditable(boolean bool) {
        this.editable = bool;
    }

    public boolean isEditable() {
        return this.editable;
    }
}
