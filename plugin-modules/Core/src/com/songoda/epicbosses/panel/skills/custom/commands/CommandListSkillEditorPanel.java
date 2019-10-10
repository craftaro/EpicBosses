package com.songoda.epicbosses.panel.skills.custom.commands;

import com.google.gson.JsonObject;
import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.managers.files.CommandsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.elements.SubCommandSkillElement;
import com.songoda.epicbosses.skills.types.CommandSkillElement;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 09-Dec-18
 */
public class CommandListSkillEditorPanel extends SubVariablePanelHandler<Skill, SubCommandSkillElement> {

    private CommandsFileManager commandsFileManager;
    private ItemStackConverter itemStackConverter;
    private BossSkillManager bossSkillManager;
    private EpicBosses plugin;

    public CommandListSkillEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemStackConverter = new ItemStackConverter();
        this.bossSkillManager = plugin.getBossSkillManager();
        this.commandsFileManager = plugin.getBossCommandFileManager();
    }

    @Override
    public void fillPanel(Panel panel, Skill skill, SubCommandSkillElement subCommandSkillElement) {
        Map<String, List<String>> currentCommands = this.commandsFileManager.getCommandsMap();
        List<String> entryList = new ArrayList<>(currentCommands.keySet());
        List<String> commands = subCommandSkillElement.getCommands();
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if (requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, commands, currentCommands, entryList, skill, subCommandSkillElement);
            return true;
        }));

        loadPage(panel, 0, commands, currentCommands, entryList, skill, subCommandSkillElement);
    }

    @Override
    public void openFor(Player player, Skill skill, SubCommandSkillElement subCommandSkillElement) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        replaceMap.put("{name}", BossAPI.getSkillName(skill));
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getCommandSkillEditorPanel(), skill);

        ServerUtils.get().runTaskAsync(() -> fillPanel(panel, skill, subCommandSkillElement));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, List<String> commands, Map<String, List<String>> allCommands, List<String> entryList, Skill skill, SubCommandSkillElement subCommandSkillElement) {
        panel.loadPage(page, (slot, realisticSlot) -> {
            if (slot >= allCommands.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {
                });
            } else {
                String name = entryList.get(slot);
                List<String> boundCommands = allCommands.get(name);
                ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack("DefaultTextMenuItem");
                ItemStack itemStack = this.itemStackConverter.from(itemStackHolder);

                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{name}", name);

                if (commands.contains(name)) {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getDisplay().getString("Display.Skills.CommandList.selectedName"), replaceMap);
                } else {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getDisplay().getString("Display.Skills.CommandList.name"), replaceMap);
                }

                ItemMeta itemMeta = itemStack.getItemMeta();
                List<String> presetLore = this.plugin.getDisplay().getStringList("Display.Skills.CommandList.lore");
                List<String> newLore = new ArrayList<>();

                for (String s : presetLore) {
                    if (s.contains("{commands}")) {
                        for (String command : boundCommands) {
                            List<String> split = StringUtils.get().splitString(command, 45);

                            split.forEach(string -> newLore.add(StringUtils.get().translateColor("&7") + string));
                            newLore.add(" ");
                        }
                    } else {
                        newLore.add(StringUtils.get().translateColor(s));
                    }
                }

                itemMeta.setLore(newLore);
                itemStack.setItemMeta(itemMeta);

                panel.setItem(realisticSlot, itemStack, e -> {
                    if (commands.contains(name)) {
                        commands.remove(name);
                    } else {
                        commands.add(name);
                    }

                    ServerUtils.get().runTaskAsync(() -> {
                        subCommandSkillElement.setCommands(commands);

                        CommandSkillElement commandSkillElement = this.bossSkillManager.getCommandSkillElement(skill);
                        List<SubCommandSkillElement> subCommandSkillElements = commandSkillElement.getCommands();
                        List<SubCommandSkillElement> newElements = new ArrayList<>();

                        for (SubCommandSkillElement subElement : subCommandSkillElements) {
                            if (subElement.getName().equals(subCommandSkillElement.getName())) {
                                newElements.add(subCommandSkillElement);
                            } else {
                                newElements.add(subElement);
                            }
                        }

                        commandSkillElement.setCommands(newElements);
                        saveSkill(skill, commandSkillElement);
                    });

                    loadPage(panel, page, commands, allCommands, entryList, skill, subCommandSkillElement);
                });
            }
        });
    }

    private void saveSkill(Skill skill, CommandSkillElement commandSkillElement) {
        JsonObject jsonObject = BossAPI.convertObjectToJsonObject(commandSkillElement);

        skill.setCustomData(jsonObject);
        this.plugin.getSkillsFileManager().save();
    }
}
