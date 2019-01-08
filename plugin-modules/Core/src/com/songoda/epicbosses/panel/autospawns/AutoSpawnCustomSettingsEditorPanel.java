package com.songoda.epicbosses.panel.autospawns;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.skills.interfaces.ICustomSettingAction;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
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
 * @since 07-Jan-19
 */
public class AutoSpawnCustomSettingsEditorPanel extends VariablePanelHandler<AutoSpawn> {

    private CustomBosses plugin;

    public AutoSpawnCustomSettingsEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, AutoSpawn autoSpawn) {
        List<ICustomSettingAction> customButtons = autoSpawn.getIntervalSpawnData().getCustomSettingActions(autoSpawn, this);

        if(customButtons == null || customButtons.isEmpty()) return;

        int maxPage = panel.getMaxPage(customButtons);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, customButtons, autoSpawn);
            return true;
        }));


        loadPage(panel, 0, customButtons, autoSpawn);
    }

    @Override
    public void openFor(Player player, AutoSpawn autoSpawn) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getAutoSpawnName(autoSpawn));
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getMainAutoSpawnEditPanel(), autoSpawn);

        fillPanel(panel, autoSpawn);
        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, List<ICustomSettingAction> clickActions, AutoSpawn autoSpawn) {
        panel.loadPage(page, ((slot, realisticSlot) -> {
            if(slot >= clickActions.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                ICustomSettingAction customSettingAction = clickActions.get(slot);
                ClickAction clickAction = customSettingAction.getAction();
                String name = customSettingAction.getSettingName();
                ItemStack displayStack = customSettingAction.getDisplayItemStack().clone();
                String currently = customSettingAction.getCurrent();
                List<String> extraInfo = customSettingAction.getExtraInformation();

                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{setting}", name);
                replaceMap.put("{currently}", currently);

                if(displayStack == null || displayStack.getType() == Material.AIR) return;

                ItemStackUtils.applyDisplayName(displayStack, this.plugin.getConfig().getString("Display.AutoSpawns.CustomSettings.name"), replaceMap);

                ItemMeta itemMeta = displayStack.getItemMeta();
                List<String> lore = this.plugin.getConfig().getStringList("Display.AutoSpawns.CustomSettings.lore");
                List<String> newLore = new ArrayList<>();

                for(String s : lore) {
                    if(s.contains("{extraInformation}")) {
                        if(extraInfo == null || extraInfo.isEmpty()) continue;

                        newLore.add("&7");
                        newLore.addAll(extraInfo);
                    } else {
                        for(String replaceKey : replaceMap.keySet()) {
                            if(s.contains(replaceKey)) {
                                s = s.replace(replaceKey, replaceMap.get(replaceKey));
                            }
                        }

                        newLore.add(s);
                    }
                }

                newLore.replaceAll(s -> s.replace('&', '§'));
                itemMeta.setLore(newLore);
                displayStack.setItemMeta(itemMeta);

                panel.setItem(realisticSlot, displayStack, event -> {
                    if(!autoSpawn.isEditing()) {
                        Message.Boss_AutoSpawn_MustToggleEditing.msg(event.getWhoClicked());
                    } else {
                        clickAction.onClick(event);
                    }
                });
            }
        }));
    }

}
