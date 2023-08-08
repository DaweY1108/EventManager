package me.dawey.eventmanager.Utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.lang.reflect.Field;

public class SkullTexture {
    //Block kicserélése fejtextúrás fejre
    public static void setSkullURL(String textureURL, Block block) {
        block.setType(Material.PLAYER_HEAD);
        final Skull skull = (Skull) block.getState();
        String url = "http://textures.minecraft.net/texture/" + textureURL;

        try {
            Field profileField = skull.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skull, getNonPlayerProfile(url));
        }catch (NoSuchFieldException | IllegalAccessException e) { e.printStackTrace(); }

        skull.update();

    }

    //Random profil készítése és a növénytextúra lehívása
    public static GameProfile getNonPlayerProfile(String skinURL) {
        GameProfile newSkinProfile = new GameProfile(java.util.UUID.randomUUID(), null);
        newSkinProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"" + skinURL + "\"}}}")));
        return newSkinProfile;
    }
}
