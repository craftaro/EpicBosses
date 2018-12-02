package com.songoda.epicbosses.panel.skills.custom;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.MessagesFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.elements.SubCommandSkillElement;
import com.songoda.epicbosses.skills.types.CommandSkillElement;
import com.songoda.epicbosses.skills.types.PotionSkillElement;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.command.SubCommand;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import com.songoda.epicbosses.utils.potion.holder.PotionEffectHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Dec-18
 */
public class CommandSkillEditorPanel extends VariablePanelHandler<Skill> {

    private MessagesFileManager messagesFileManager;
    private ItemStackConverter itemStackConverter;
    private CustomBosses plugin;

    public CommandSkillEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, Skill skill) {
        CommandSkillElement commandSkillElement = this.plugin.getBossSkillManager().getCommandSkillElement(skill);
        List<SubCommandSkillElement> subCommandSkillElements = commandSkillElement.getCommands();
        int maxPage = panel.getMaxPage(subCommandSkillElements);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, subCommandSkillElements, skill);
            return true;
        }));

        loadPage(panel, 0, subCommandSkillElements, skill);
    }

    @Override
    public void openFor(Player player, Skill skill) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        replaceMap.put("{name}", BossAPI.getSkillName(skill));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter counter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getMainSkillEditMenu(), skill);

        counter.getSlotsWith("AddNew").forEach(slot -> panel.setOnClick(slot, getAddNewAction(skill)));
        fillPanel(panel, skill);

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();

        panelBuilderCounter
                .addSlotCounter("AddNew");
    }

    private ClickAction getAddNewAction(Skill skill) {
        return event -> {

        };
    }

    private void loadPage(Panel panel, int page, List<SubCommandSkillElement> subCommandSkillElements, Skill skill) {
        panel.loadPage(page, (slot, realisticSlot) -> {
            if(slot >= subCommandSkillElements.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                SubCommandSkillElement subCommandSkillElement = subCommandSkillElements.get(slot);

                ItemStack itemStack = new ItemStack(Material.BOOK);

                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getConfig().getStringList("Display.Skills.Commands.lore"));
                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Skills.Commands.name"));

                panel.setItem(realisticSlot, itemStack, event -> {
                    ClickType clickType = event.getClick();

                    if(clickType.name().contains("RIGHT")) {
                        //TODO: Modify Chance
                    } else {
                        //TODO: Modify commands
                    }

                    loadPage(panel, page, subCommandSkillElements, skill);
                });
            }
        });
    }
}
