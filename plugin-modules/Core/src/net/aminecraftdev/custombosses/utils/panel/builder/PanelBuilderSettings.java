package net.aminecraftdev.custombosses.utils.panel.builder;

import lombok.Getter;
import net.aminecraftdev.custombosses.utils.itemstack.ItemStackHolderConverter;
import net.aminecraftdev.custombosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Jul-18
 */
public class PanelBuilderSettings {

    @Getter private boolean emptySpaceFiller, backButton, exitButton;
    @Getter private int fillTo, backButtonSlot, exitButtonSlot;
    @Getter private ItemStackHolder emptySpaceFillerItem;

    public PanelBuilderSettings(ConfigurationSection configurationSection) {
        ItemStackHolderConverter itemStackHolderConverter = new ItemStackHolderConverter();

        this.emptySpaceFiller = configurationSection.getBoolean("Settings.emptySpaceFiller", false);
        this.backButton = configurationSection.getBoolean("Settings.backButton", false);
        this.exitButton = configurationSection.getBoolean("Settings.exitButton", false);
        this.fillTo = configurationSection.getInt("Settings.fillTo", 0);
        this.backButtonSlot = configurationSection.getInt("Buttons.backButton", -1);
        this.exitButtonSlot = configurationSection.getInt("Buttons.exitButton", -1);
        this.emptySpaceFillerItem = itemStackHolderConverter.to(configurationSection.getConfigurationSection("EmptySpaceFiller"));
    }

}
