package com.songoda.epicbosses.panel.skills.custom.custom;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 13-Dec-18
 */
public class CustomSkillTypeEditorPanel extends SubVariablePanelHandler<Skill, CustomSkillElement> {

    public CustomSkillTypeEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void openFor(Player player, Skill skill, CustomSkillElement customSkillElement) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        replaceMap.put("{name}", BossAPI.getSkillName(skill));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter counter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
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
