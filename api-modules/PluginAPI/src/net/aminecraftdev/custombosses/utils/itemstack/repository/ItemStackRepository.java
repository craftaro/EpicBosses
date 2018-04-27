package net.aminecraftdev.custombosses.utils.itemstack.repository;

import net.aminecraftdev.custombosses.utils.IReloadable;
import net.aminecraftdev.custombosses.utils.NumberUtils;
import net.aminecraftdev.custombosses.utils.file.ManagedFile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public class ItemStackRepository implements IReloadable {

    private static ItemStackRepository instance;

    private final Pattern csvSplitPattern = Pattern.compile("(\"([^\"]*)\"|[^,]*)(,|$)");
    private final Pattern splitPattern = Pattern.compile("((.*)[:+',;.](\\d+))");
    private final Map<String, Integer> items = new HashMap<>();
    private final Map<ItemData, List<String>> names = new HashMap<>();
    private final Map<ItemData, String> primaryName = new HashMap<>();
    private final Map<String, Short> durabilities = new HashMap<>();
    private final Map<String, String> nbtData = new HashMap<>();
    private final ManagedFile file;

    public ItemStackRepository(JavaPlugin javaPlugin) {
        instance = this;

        file = new ManagedFile("items.csv", javaPlugin);
    }

    @Override
    public void reload() {
        final List<String> lines = file.getLines();

        if (lines.isEmpty()) {
            return;
        }

        durabilities.clear();
        items.clear();
        names.clear();
        primaryName.clear();

        for (String line : lines) {
            if (line.length() > 0 && line.charAt(0) == '#') {
                continue;
            }

            String itemName = null;
            int numeric = -1;
            short data = 0;
            String nbt = null;

            int col = 0;
            Matcher matcher = csvSplitPattern.matcher(line);
            while (matcher.find()) {
                String match = matcher.group(1);
                if (StringUtils.stripToNull(match) == null) {
                    continue;
                }
                match = StringUtils.strip(match.trim(), "\"");
                switch (col) {
                    case 0:
                        itemName = match.toLowerCase(Locale.ENGLISH);
                        break;
                    case 1:
                        numeric = Integer.parseInt(match);
                        break;
                    case 2:
                        data = Short.parseShort(match);
                        break;
                    case 3:
                        nbt = StringUtils.stripToNull(match);
                        break;
                    default:
                        continue;
                }
                col++;
            }
            // Invalid row
            if (itemName == null || numeric < 0) {
                continue;
            }
            durabilities.put(itemName, data);
            items.put(itemName, numeric);
            if (nbt != null) {
                nbtData.put(itemName, nbt);
            }

            ItemData itemData = new ItemData(numeric, data);
            if (names.containsKey(itemData)) {
                List<String> nameList = names.get(itemData);
                nameList.add(itemName);
            } else {
                List<String> nameList = new ArrayList<>();
                nameList.add(itemName);
                names.put(itemData, nameList);
                primaryName.put(itemData, itemName);
            }
        }

        for (List<String> nameList : names.values()) {
            nameList.sort(LengthCompare.INSTANCE);
        }
    }

    public ItemStack getItemStack(String id, int quantity) {
        ItemStack itemStack = getItemStack(id.toLowerCase(Locale.ENGLISH));

        itemStack.setAmount(quantity);

        return itemStack;
    }

    public ItemStack getItemStack(String id) {
        int itemid = 0;
        String itemname;
        short metaData = 0;
        Matcher parts = splitPattern.matcher(id);
        if (parts.matches()) {
            itemname = parts.group(2);
            metaData = Short.parseShort(parts.group(3));
        } else {
            itemname = id;
        }

        if (NumberUtils.get().isInt(itemname)) {
            itemid = Integer.parseInt(itemname);
        } else if (NumberUtils.get().isInt(id)) {
            itemid = Integer.parseInt(id);
        } else {
            itemname = itemname.toLowerCase(Locale.ENGLISH);
        }

        if (itemid < 1) {
            if (items.containsKey(itemname)) {
                itemid = items.get(itemname);
                if (durabilities.containsKey(itemname) && metaData == 0) {
                    metaData = durabilities.get(itemname);
                }
            } else if (Material.getMaterial(itemname.toUpperCase(Locale.ENGLISH)) != null) {
                Material bMaterial = Material.getMaterial(itemname.toUpperCase(Locale.ENGLISH));
                itemid = bMaterial.getId();
            } else {
                try {
                    Material bMaterial = Bukkit.getUnsafe().getMaterialFromInternalName(itemname.toLowerCase(Locale.ENGLISH));
                    itemid = bMaterial.getId();
                } catch (Throwable throwable) {
//                    throw new Exception(tl("unknownItemName", itemname), throwable);
                }
            }
        }

        if (itemid < 1) {
//            throw new Exception(tl("unknownItemName", itemname));
        }

        final Material mat = Material.getMaterial(itemid);
        if (mat == null) {
//            throw new Exception(tl("unknownItemId", itemid));
        }
        ItemStack retval = new ItemStack(mat);
        if (nbtData.containsKey(itemname)) {
            String nbt = nbtData.get(itemname);
            if (nbt.startsWith("*")) {
                nbt = nbtData.get(nbt.substring(1));
            }
//            retval = ess.getServer().getUnsafe().modifyItemStack(retval, nbt);
        }
        if (mat == Material.MOB_SPAWNER) {
            if (metaData == 0) metaData = EntityType.PIG.getTypeId();
            try {
//                retval = ess.getSpawnerProvider().setEntityType(retval, EntityType.fromId(metaData));
            } catch (IllegalArgumentException e) {
//                throw new Exception("Can't spawn entity ID " + metaData + " from mob spawners.");
            }
        } else if (mat == Material.MONSTER_EGG) {
            EntityType type;
            try {
                type = EntityType.fromId(metaData);
            } catch (IllegalArgumentException e) {
//                throw new Exception("Can't spawn entity ID " + metaData + " from spawn eggs.");
            }
//            retval = ess.getSpawnEggProvider().createEggItem(type);
        } else if (mat.name().endsWith("POTION")) {
//                && ReflUtil.getNmsVersionObject().isLowerThan(ReflUtil.V1_11_R1)) { // Only apply this to pre-1.11 as items.csv might only work in 1.11
//            retval = ess.getPotionMetaProvider().createPotionItem(mat, metaData);
        } else {
            retval.setDurability(metaData);
        }
        retval.setAmount(mat.getMaxStackSize());
        return retval;
    }

    public static ItemStackRepository get() {
        return instance;
    }
}
