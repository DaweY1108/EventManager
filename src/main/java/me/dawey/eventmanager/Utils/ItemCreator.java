package me.dawey.eventmanager.Utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.dawey.eventmanager.EventManager;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemCreator {

    private static EventManager plugin = EventManager.getPlugin(EventManager.class);

    public static ItemStack create(ConfigurationSection section) {
        //Ha a config üres
        if (section == null) {
            return null;
        }

        //Item inicalizálása
        ItemStack item = null;

        //Item ID-k kezelése
        item = (section == null || !section.contains("id")) ? null : new ItemStack(Material.getMaterial(section.getString("id")));


        //Meta inicalizálása
        ItemMeta meta = item.getItemMeta();
        boolean modifiedMeta = false;

        //Item mennyiseg kezelese
        if (section.contains("amount")) {
            item.setAmount(section.getInt("amount"));
        }

        //Item nevenek kezelese
        if (section.contains("name")) {
            meta.setDisplayName(ColorFormat.format(section.getString("name")));
            modifiedMeta = true;
        }

        //Item leirasanak kezelese
        if (section.contains("lore")) {
            List<String> dummy = section.getStringList("lore");
            List<String> lore = new ArrayList<String>();
            for (String s : dummy) {
                lore.add(ColorFormat.format(s));
            }
            meta.setLore(lore);
            modifiedMeta = true;
        }

        //Item enchantok kezelese
        if (section.contains("enchantments")) {
            List<String> enchantmentStrings = section.getStringList("enchantments");
            for (String enchantmentString : enchantmentStrings) {
                String enchantmentName = enchantmentString.split(":")[0];
                int enchantmentLevel = Integer.valueOf(enchantmentString.split(":")[1]);
                Bukkit.broadcastMessage(enchantmentName);
                switch (enchantmentName) {
                    case "arrowdamage":
                        meta.addEnchant(Enchantment.ARROW_DAMAGE, enchantmentLevel, true);
                        break;
                    case "arrowfire":
                        meta.addEnchant(Enchantment.ARROW_FIRE, enchantmentLevel, true);
                        break;
                    case "arrowinfinite":
                        meta.addEnchant(Enchantment.ARROW_INFINITE, enchantmentLevel, true);
                        break;
                    case "arrowknockback":
                        meta.addEnchant(Enchantment.ARROW_KNOCKBACK, enchantmentLevel, true);
                        break;
                    case "damage":
                        meta.addEnchant(Enchantment.DAMAGE_ALL, enchantmentLevel, true);
                        break;
                    case "digspeed":
                        meta.addEnchant(Enchantment.DIG_SPEED, enchantmentLevel, true);
                        break;
                    case "durability":
                        meta.addEnchant(Enchantment.DURABILITY, enchantmentLevel, true);
                        break;
                    case "fireaspect":
                        meta.addEnchant(Enchantment.FIRE_ASPECT, enchantmentLevel, true);
                        break;
                    case "knockback":
                        meta.addEnchant(Enchantment.KNOCKBACK, enchantmentLevel, true);
                        break;
                    case "lootbonusblock":
                        meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, enchantmentLevel, true);
                        break;
                    case "lootbonusmob":
                        meta.addEnchant(Enchantment.LOOT_BONUS_MOBS, enchantmentLevel, true);
                        break;
                    case "luck":
                        meta.addEnchant(Enchantment.LUCK, enchantmentLevel, true);
                        break;
                    case "protectionfall":
                        meta.addEnchant(Enchantment.PROTECTION_FALL, enchantmentLevel, true);
                        break;
                    case "protectionfire":
                        meta.addEnchant(Enchantment.PROTECTION_FALL, enchantmentLevel, true);
                        break;
                    case "silktouch":
                        meta.addEnchant(Enchantment.SILK_TOUCH, enchantmentLevel, true);
                        break;
                }
            }
            modifiedMeta = true;
        }

        //Item fejtextura kezelese
        if (section.contains("skullTextureID")) {
            String skullTextureID = section.getString("skullTextureID");
            String skullTextureUUID = plugin.getConfig().getString("texture-UUID");
            if (skullTextureID != null) {
                String skinURL = "http://textures.minecraft.net/texture/" + skullTextureID;
                SkullMeta skullMeta = (SkullMeta) meta;
                skullMeta.setOwner("YouCantFindMe");
                GameProfile profile = new GameProfile((UUID.fromString(skullTextureUUID)), null);
                byte[] encodedData = Base64.encodeBase64(("{textures:{SKIN:{url:\"" + skinURL + "\"}}}").getBytes());
                profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
                Field profileField = null;
                try {
                    profileField = skullMeta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(skullMeta, profile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                meta = skullMeta;
                modifiedMeta = true;
            }
        }

        //Item tagek kezelése
        if (section.contains("tags")) {
            for (String tags : section.getStringList("tags")) {
                String[] tagList = tags.split(":");
                String tagName = tagList[0];
                String tagValue = tagList[1];
                switch (tagName) {
                    case "vanilladurability":
                        Damageable dam = (Damageable) meta;
                        dam.setDamage(Integer.valueOf(tagValue));
                        meta = (ItemMeta) dam;
                        break;
                    case "unbreakable":
                        meta.setUnbreakable(Boolean.valueOf(tagValue));
                        break;
                    case "custommodeldata":
                        meta.setCustomModelData(Integer.valueOf(tagValue));
                        break;
                }
            }
            modifiedMeta = true;
        }

        //Flagek kezelése
        if (section.contains("flags")) {
            for (String flags : section.getStringList("flags")) {
                meta.addItemFlags(ItemFlag.valueOf(flags));
            }
            modifiedMeta = true;
        }

        //Itemmeta hozzáadása az itemhez
        if (modifiedMeta) {
            item.setItemMeta(meta);
        }

        return item;
    }
}
