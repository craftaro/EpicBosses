package com.songoda.epicbosses.panel.skills.custom.custom;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Dec-18
 */
public class SpecialSettingsEditorPanel extends SubVariablePanelHandler<Skill, CustomSkillElement> {

    private ItemStackConverter itemStackConverter;
    private BossSkillManager bossSkillManager;
    private CustomBosses plugin;

    public SpecialSettingsEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemStackConverter = new ItemStackConverter();
        this.bossSkillManager = plugin.getBossSkillManager();
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

    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }
}
