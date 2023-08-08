package me.dawey.eventmanager.Utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class ItemSimilar {
    public static boolean isSimilar(ItemStack item1, ItemStack item2) {
        if (item2.getType() != item1.getType())
            return false;
        if (item2.hasItemMeta() != item1.hasItemMeta())
            return false;
        if (item2.hasItemMeta()) {
            ItemMeta item1Meta = item1.getItemMeta();
            ItemMeta item2Meta = item2.getItemMeta();
            if (item2Meta.hasDisplayName() != item1Meta.hasDisplayName())
                return false;
            if (item2Meta.hasDisplayName() &&
                    !item2Meta.getDisplayName().equals(item1Meta.getDisplayName()))
                return false;
            if (item2Meta.hasLore() != item1Meta.hasLore())
                return false;
            if (item2Meta.hasLore())
                for (int i = 0; i < item2Meta.getLore().size(); i++) {
                    if (!((String)item2Meta.getLore().get(i)).equals(item1Meta.getLore().get(i)))
                        return false;
                }
            if (item2Meta.hasEnchants() != item1Meta.hasEnchants())
                return false;
            if (item2Meta.hasEnchants()) {
                if (item2Meta.getEnchants().size() != item1Meta.getEnchants().size())
                    return false;
                for (Map.Entry<Enchantment, Integer> enchantInfo : (Iterable<Map.Entry<Enchantment, Integer>>)item1Meta.getEnchants().entrySet()) {
                    if (item2Meta.getEnchantLevel(enchantInfo.getKey()) != item1Meta.getEnchantLevel(enchantInfo.getKey()))
                        return false;
                }
            }
            if (item2Meta.getItemFlags().size() != item1Meta.getItemFlags().size())
                return false;
            for (ItemFlag flag : item2Meta.getItemFlags()) {
                if (!item1Meta.hasItemFlag(flag))
                    return false;
            }
            if (item2Meta instanceof Damageable != item1Meta instanceof Damageable)
                return false;
            if (item2Meta instanceof Damageable) {
                Damageable dam1 = (Damageable)item1Meta;
                Damageable dam2 = (Damageable)item2Meta;
                if (dam1.hasDamage() != dam2.hasDamage())
                    return false;
                if (dam2.hasDamage() &&
                        dam2.getDamage() != dam1.getDamage())
                    return false;
            }
            if (item2Meta.hasCustomModelData() != item1Meta.hasCustomModelData())
                return false;
            if (item2Meta.hasCustomModelData() &&
                    item2Meta.getCustomModelData() != item1Meta.getCustomModelData())
                return false;
        }
        return true;
    }
}
