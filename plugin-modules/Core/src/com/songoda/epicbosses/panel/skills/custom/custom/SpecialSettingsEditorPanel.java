package com.songoda.epicbosses.panel.skills.custom.custom;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.skills.CustomSkillHandler;
import com.songoda.epicbosses.skills.interfaces.ICustomSkillAction;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Dec-18
 */
public class SpecialSettingsEditorPanel extends SubVariablePanelHandler<Skill, CustomSkillElement> {

    private BossSkillManager bossSkillManager;
    private CustomBosses plugin;

    public SpecialSettingsEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.bossSkillManager = plugin.getBossSkillManager();
    }

    @Override
    public void openFor(Player player, Skill skill, CustomSkillElement customSkillElement) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getSkillName(skill));
        replaceMap.put("{selected}", customSkillElement.getCustom().getType());
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getCustomSkillEditorPanel(), skill);

        fillPanel(panel, skill, customSkillElement);

        panel.openFor(player);
    }

    @Override
    public void fillPanel(Panel panel, Skill skill, CustomSkillElement customSkillElement) {
        String currentSkillName = customSkillElement.getCustom().getType();
        CustomSkillHandler customSkillHandler = this.bossSkillManager.getSkills().stream().filter(cSH -> cSH.getSkillName().equalsIgnoreCase(currentSkillName)).findFirst().orElse(null);

        if(customSkillHandler == null) {
            Debug.FAILED_TO_FIND_ASSIGNED_CUSTOMSKILLHANDLER.debug(currentSkillName);
            return;
        }

        List<ICustomSkillAction> customButtons = customSkillHandler.getOtherSkillDataActions(skill, customSkillElement);

        if(customButtons == null || customButtons.isEmpty()) return;

        int maxPage = panel.getMaxPage(customButtons);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, customButtons);
            return true;
        }));


        loadPage(panel, 0, customButtons);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, List<ICustomSkillAction> clickActions) {
        panel.loadPage(page, ((slot, realisticSlot) -> {
            if(slot >= clickActions.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                ICustomSkillAction customSkillAction = clickActions.get(slot);
                ClickAction clickAction = customSkillAction.getAction();
                String name = customSkillAction.getSettingName();
                ItemStack displayStack = customSkillAction.getDisplayItemStack().clone();
                String currently = customSkillAction.getCurrent();

                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{setting}", name);
                replaceMap.put("{currently}", currently);

                if(displayStack == null || displayStack.getType() == Material.AIR) return;

                ItemStackUtils.applyDisplayName(displayStack, this.plugin.getConfig().getString("Display.Skills.CustomSetting.name"), replaceMap);
                ItemStackUtils.applyDisplayLore(displayStack, this.plugin.getConfig().getStringList("Display.Skills.CustomSetting.lore"), replaceMap);

                panel.setItem(realisticSlot, displayStack, clickAction);
            }
        }));
    }


}
