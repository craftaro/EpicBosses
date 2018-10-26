package utils.factions;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Relation;
import com.songoda.epicbosses.utils.IFactionHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public class FactionsUUID implements IFactionHelper {

    @Override
    public boolean isFriendly(Player a, Player b) {
        Faction pFac = FPlayers.getInstance().getByPlayer(a).getFaction();
        Faction zFac = FPlayers.getInstance().getByPlayer(b).getFaction();
        Relation r = pFac.getRelationTo(zFac);

        if(ChatColor.stripColor(zFac.getId()).equalsIgnoreCase("Wilderness")) return false;
        if(a == b) return true;
        if(r.isEnemy()) return false;
        if(r.isNeutral()) return false;
        if(r.isTruce()) return true;
        if(r.isAlly()) return true;
        if(r.isMember()) return true;
        return false;
    }

    @Override
    public boolean isFriendly(Player a, Location location) {
        Faction pFac = FPlayers.getInstance().getByPlayer(a).getFaction();
        FLocation fLoc = new FLocation(location);
        Faction locFac = Board.getInstance().getFactionAt(fLoc);
        Relation r = pFac.getRelationTo(locFac);

        if(ChatColor.stripColor(locFac.getComparisonTag()).equalsIgnoreCase("Wilderness")) return false;
        if(r.isEnemy()) return false;
        if(r.isNeutral()) return false;
        if(r.isTruce()) return true;
        if(r.isAlly()) return true;
        if(r.isMember()) return true;
        return false;
    }

    @Override
    public boolean isInWarzone(Location location) {
        FLocation fLoc = new FLocation(location);
        Faction locFac = Board.getInstance().getFactionAt(fLoc);

        if(ChatColor.stripColor(locFac.getComparisonTag()).equalsIgnoreCase("WarZone")) return true;
        return false;
    }

    @Override
    public boolean isInClaimedLand(Location location) {
        FLocation fLoc = new FLocation(location);
        Faction locFac = Board.getInstance().getFactionAt(fLoc);
        String string = ChatColor.stripColor(locFac.getComparisonTag());

        if(string.equalsIgnoreCase("WarZone")) return false;
        if(string.equalsIgnoreCase("SafeZone")) return false;
        if(string.equalsIgnoreCase("Wilderness")) return false;
        return true;
    }
}
