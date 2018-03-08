package net.aminecraftdev.custombosses.utils.factions;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import net.aminecraftdev.custombosses.utils.IFactionHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public class FactionsM implements IFactionHelper {

    @Override
    public boolean isFriendly(Player a, Player b) {
        MPlayer mp1 = MPlayer.get(a);
        MPlayer mp2 = MPlayer.get(b);
        Faction mp2Fac = mp2.getFaction();
        Rel relation = mp1.getRelationTo(mp2);

        if(ChatColor.stripColor(mp2Fac.getName()).equalsIgnoreCase("Wilderness")) return false;
        if(relation == Rel.ENEMY) return false;
        if(relation == Rel.NEUTRAL) return false;
        if(relation == Rel.ALLY) return true;
        if(relation == Rel.TRUCE) return false;
        if(relation == Rel.LEADER) return true;
        if(relation == Rel.OFFICER) return true;
        if(relation == Rel.MEMBER) return true;
        if(relation == Rel.RECRUIT) return true;
        return false;
    }

    @Override
    public boolean isFriendly(Player a, Location location) {
        MPlayer mp1 = MPlayer.get(a);
        Faction mp1Fac = mp1.getFaction();
        Faction locFac = BoardColl.get().getFactionAt(PS.valueOf(location));
        Rel relation = mp1Fac.getRelationTo(locFac);

        if(ChatColor.stripColor(locFac.getName()).equalsIgnoreCase("Wilderness")) return true;
        if(relation == Rel.ENEMY) return false;
        if(relation == Rel.NEUTRAL) return false;
        if(relation == Rel.ALLY) return true;
        if(relation == Rel.TRUCE) return true;
        if(relation == Rel.LEADER) return true;
        if(relation == Rel.OFFICER) return true;
        if(relation == Rel.MEMBER) return true;
        if(relation == Rel.RECRUIT) return true;
        return false;
    }

    @Override
    public boolean isInWarzone(Location location) {
        Faction locFac = BoardColl.get().getFactionAt(PS.valueOf(location));

        if(ChatColor.stripColor(locFac.getName()).equalsIgnoreCase("WarZone")) return true;
        return false;
    }

    @Override
    public boolean isInClaimedLand(Location location) {
        Faction locFac = BoardColl.get().getFactionAt(PS.valueOf(location));
        String string = ChatColor.stripColor(locFac.getName());

        if(string.equalsIgnoreCase("WarZone")) return false;
        if(string.equalsIgnoreCase("SafeZone")) return false;
        if(string.equalsIgnoreCase("Wilderness")) return false;
        return true;
    }
}
