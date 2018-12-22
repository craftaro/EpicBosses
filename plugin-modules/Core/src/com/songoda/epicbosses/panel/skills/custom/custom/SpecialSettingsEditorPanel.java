package com.songoda.epicbosses.panel.skills.custom.custom;

import com.google.gson.JsonObject;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.managers.files.MinionsFileManager;
import com.songoda.epicbosses.skills.CustomSkillHandler;
import com.songoda.epicbosses.skills.interfaces.ICustomSkillAction;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
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

    private ItemStackConverter itemStackConverter;
    private MinionsFileManager minionsFileManager;
    private BossSkillManager bossSkillManager;
    private CustomBosses plugin;

    public SpecialSettingsEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemStackConverter = new ItemStackConverter();
        this.bossSkillManager = plugin.getBossSkillManager();
        this.minionsFileManager = plugin.getMinionsFileManager();
    }

    @Override
    public void openFor(Player player, Skill skill, CustomSkillElement customSkillElement) {
        Panel panel = getPanelBuilder().getPanel()
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

        loadPage(panel, 0, skill, customSkillElement, customSkillHandler, customButtons);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, Skill skill, CustomSkillElement customSkillElement, CustomSkillHandler customSkillHandler, List<ICustomSkillAction> clickActions) {
        panel.loadPage(page, ((slot, realisticSlot) -> {
            if(slot >= clickActions.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                ICustomSkillAction customSkillAction = clickActions.get(slot);
                ClickAction clickAction = customSkillAction.getAction();
                String name = customSkillAction.getSettingName();
                ItemStack displayStack = customSkillAction.getDisplayItemStack();

                

            }
        }));
    }


}
