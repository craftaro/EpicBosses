package com.songoda.epicbosses.panel.skills.custom;

import com.google.gson.JsonObject;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.elements.SubCommandSkillElement;
import com.songoda.epicbosses.skills.types.CommandSkillElement;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Dec-18
 */
public class CommandSkillEditorPanel extends VariablePanelHandler<Skill> {

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
        ServerUtils.get().runTaskAsync(() -> {
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
        });
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getAddNewAction(Skill skill) {
        return event -> {
            SubCommandSkillElement subCommandSkillElement = new SubCommandSkillElement(UUID.randomUUID().toString(), 100.0, new ArrayList<>());
            CommandSkillElement commandSkillElement = this.plugin.getBossSkillManager().getCommandSkillElement(skill);

            List<SubCommandSkillElement> subElements = commandSkillElement.getCommands();

            subElements.add(subCommandSkillElement);
            commandSkillElement.setCommands(subElements);

            JsonObject jsonObject = BossAPI.convertObjectToJsonObject(commandSkillElement);

            skill.setCustomData(jsonObject);
            this.plugin.getSkillsFileManager().save();

            openFor((Player) event.getWhoClicked(), skill);
        };
    }

    private void loadPage(Panel panel, int page, List<SubCommandSkillElement> subCommandSkillElements, Skill skill) {
        panel.loadPage(page, (slot, realisticSlot) -> {
            if(slot >= subCommandSkillElements.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                SubCommandSkillElement subCommandSkillElement = subCommandSkillElements.get(slot);
                Map<String, String> replaceMap = new HashMap<>();
                Double chance = subCommandSkillElement.getChance();
                List<String> commands = subCommandSkillElement.getCommands();

                if(chance == null) chance = 100.0;
                if(commands == null) commands = new ArrayList<>();

                replaceMap.put("{chance}", NumberUtils.get().formatDouble(chance));
                replaceMap.put("{commands}", StringUtils.get().appendList(commands));

                ItemStack itemStack = new ItemStack(Material.BOOK);

                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getConfig().getStringList("Display.Skills.Commands.lore"), replaceMap);
                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Skills.Commands.name"), replaceMap);

                panel.setItem(realisticSlot, itemStack, event -> this.bossPanelManager.getModifyCommandEditMenu().openFor((Player) event.getWhoClicked(), skill, subCommandSkillElement));
            }
        });
    }
}
