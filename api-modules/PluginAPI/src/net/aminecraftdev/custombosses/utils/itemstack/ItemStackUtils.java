package net.aminecraftdev.custombosses.utils.itemstack;

import net.aminecraftdev.custombosses.utils.NumberUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public class ItemStackUtils {

    private static final Pattern SPLIT_PATTERN = Pattern.compile("((.*)[:+',;.](\\d+))");
    private static final NumberUtils NUMBER_UTILS = new NumberUtils();

    public ItemStack getItemStackByString(String id) {
        int itemId = 0;
        short metaData = 0;
        String itemName;

        Matcher matcher = SPLIT_PATTERN.matcher(id);

        if(matcher.matches()) {
            itemName = matcher.group(2);
            metaData = Short.parseShort(matcher.group(3));
        } else {
            itemName = id;
        }

        if(NUMBER_UTILS.isInt(itemName)) {
            itemId = Integer.parseInt(itemName);
        } else if(NUMBER_UTILS.isInt(id)) {
            itemId = Integer.parseInt(id);
        } else {
            itemName = itemName.toLowerCase(Locale.ENGLISH);
        }

        Material material = Material.getMaterial(itemId);

        if(material == null) {
            material = Material.getMaterial(itemName);
        }

        if(material == null) material = Material.AIR;

        return new ItemStack(material, 1, metaData);
    }

}
