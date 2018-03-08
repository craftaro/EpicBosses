package net.aminecraftdev.custombosses.utils.factions;

import net.aminecraftdev.custombosses.utils.IFactionHelper;
import net.redstoneore.legacyfactions.Relation;
import net.redstoneore.legacyfactions.entity.Board;
import net.redstoneore.legacyfactions.entity.FPlayerColl;
import net.redstoneore.legacyfactions.entity.Faction;
import net.redstoneore.legacyfactions.locality.Locality;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 27-Oct-17
 */
public class LegacyFactions implements IFactionHelper {

    @Override
    public boolean isFriendly(Player a, Player b) {
        Faction aFac = FPlayerColl.get(a).getFaction();
        Faction bFac = FPlayerColl.get(b).getFaction();
        Relation relation = aFac.getRelationTo(bFac);

        if(bFac.isWilderness()) return false;
        if(aFac == bFac) return true;
        if(relation.isEnemy()) return false;
        if(relation.isNeutral()) return false;
        if(relation.isTruce()) return true;
        if(relation.isAlly()) return true;
        if(relation.isMember()) return true;
        return false;
    }

    @Override
    public boolean isFriendly(Player a, Location location) {
        Faction aFac = FPlayerColl.get(a).getFaction();
        Faction bFac = Board.get().getFactionAt(Locality.of(location));
        Relation relation = aFac.getRelationTo(bFac);

        if(bFac.isWilderness() || relation.isEnemy() || relation.isNeutral()) return false;
        if(relation.isTruce() || relation.isAlly() || relation.isMember()) return true;
        return false;
    }

    @Override
    public boolean isInWarzone(Location location) {
        Faction faction = Board.get().getFactionAt(Locality.of(location));

        return faction.isWarZone();
    }

    @Override
    public boolean isInClaimedLand(Location location) {
        Faction faction = Board.get().getFactionAt(Locality.of(location));

        return !(faction.isWarZone() || faction.isWilderness() || faction.isSafeZone());
    }
}
