package net.aminecraftdev.custombosses.innerapi.factions;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Rel;
import net.aminecraftdev.custombosses.innerapi.dependencies.FactionBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public class FactionsOne implements FactionBuilder {

    @Override
    public boolean isFriendly(Player a, Player b) {
        Faction pFac = FPlayers.i.get(a).getFaction();
        Faction zFac = FPlayers.i.get(b).getFaction();
        Rel r = pFac.getRelationTo(zFac);

        if(ChatColor.stripColor(zFac.getId()).equalsIgnoreCase("Wilderness")) return false;
        if(a == b) return true;
        if(r.equals(Rel.ALLY)) return true;
        if(r.equals(Rel.TRUCE)) return true;
        if(r.equals(Rel.LEADER)) return true;
        if(r.equals(Rel.MEMBER)) return true;
        if(r.equals(Rel.OFFICER)) return true;
        if(r.equals(Rel.RECRUIT)) return true;
        if(r.equals(Rel.ENEMY)) return false;
        if(r.equals(Rel.NEUTRAL)) return false;
        return false;
    }

    @Override
    public boolean isFriendly(Player a, Location location) {
        Faction pFac = FPlayers.i.get(a).getFaction();
        FLocation fLoc = new FLocation(location);
        Faction locFac = Board.getFactionAt(fLoc);
        Rel r = pFac.getRelationTo(locFac);

        if(ChatColor.stripColor(locFac.getComparisonTag()).equalsIgnoreCase("Wilderness")) return false;
        if(r.equals(Rel.ALLY)) return true;
        if(r.equals(Rel.TRUCE)) return true;
        if(r.equals(Rel.LEADER)) return true;
        if(r.equals(Rel.MEMBER)) return true;
        if(r.equals(Rel.OFFICER)) return true;
        if(r.equals(Rel.RECRUIT)) return true;
        if(r.equals(Rel.ENEMY)) return false;
        if(r.equals(Rel.NEUTRAL)) return false;
        return false;
    }

    @Override
    public boolean isInWarzone(Location location) {
        FLocation fLoc = new FLocation(location);
        Faction locFac = Board.getFactionAt(fLoc);

        if(ChatColor.stripColor(locFac.getComparisonTag()).equalsIgnoreCase("WarZone")) return true;
        return false;
    }

    @Override
    public boolean isInClaimedLand(Location location) {
        FLocation fLoc = new FLocation(location);
        Faction locFac = Board.getFactionAt(fLoc);
        String string = ChatColor.stripColor(locFac.getComparisonTag());

        if(string.equalsIgnoreCase("WarZone")) return false;
        if(string.equalsIgnoreCase("SafeZone")) return false;
        if(string.equalsIgnoreCase("Wilderness")) return false;
        return true;
    }
}
