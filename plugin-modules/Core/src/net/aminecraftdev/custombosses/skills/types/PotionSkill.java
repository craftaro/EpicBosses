package net.aminecraftdev.custombosses.skills.types;

import net.aminecraftdev.custombosses.entities.components.skills.SkillComponent;
import net.aminecraftdev.custombosses.skills.CustomSkill;
import net.aminecraftdev.custombosses.entities.components.skills.data.PotionSkillData;
import net.aminecraftdev.custombosses.utils.parser.PotionParser;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 12-Mar-18
 */
public class PotionSkill extends CustomSkill {

    private List<PotionEffect> potionEffects;

    public PotionSkill(SkillComponent skillComponent, PotionSkillData potionSkillData) {
        super(skillComponent);

        this.potionEffects = new ArrayList<>();

        potionSkillData.getPotionEffects().forEach(potionEffectComponent -> this.potionEffects.add(new PotionParser(potionEffectComponent).parse()));
    }

    public void setPotionEffects(List<PotionEffect> potionEffects) {
        this.potionEffects = potionEffects;
    }

    @Override
    public void execute(Player player) {
        for(PotionEffect potionEffect : this.potionEffects) {
            player.addPotionEffect(potionEffect);
        }
    }
}
