package net.portalsam.magichealth.item;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import net.portalsam.magichealth.MagicHealth;
import net.portalsam.magichealth.config.MagicHealthConfig;

public class MagicHealthItems {

    static final ArrayList<ItemStack> items = new ArrayList<>();

    static {
        items.add(HeartCrystal.getItem());
        items.add(HeartDrainAmulet.getItem());
        items.add(HeartDust.getItem());
        items.add(HeartShard.getItem());
    }

    public static ArrayList<ItemStack> getItems() {
        return items;
    }

    public static void registerRecipes() {
        if(MagicHealthConfig.isEnablingHeartCrystalCrafting()) {
            Bukkit.addRecipe(HeartCrystal.getRecipe());

            MagicHealth.getMagicHealthLogger().info(String.format("[%s] Registered recipe for Heart Crystal",
                    MagicHealth.getMagicHealthInstance().getDescription().getName()));
        }

        if(MagicHealthConfig.isEnablingHeartDrainAmuletCrafting()) {
            Bukkit.addRecipe(HeartDrainAmulet.getRecipe());

            MagicHealth.getMagicHealthLogger().info(String.format("[%s] Registered recipe for Heart Drain Amulet",
                    MagicHealth.getMagicHealthInstance().getDescription().getName()));
        }

        if(MagicHealthConfig.isEnablingHeartShardCrafting()) {
            Bukkit.addRecipe(HeartShard.getRecipe());

            MagicHealth.getMagicHealthLogger().info(String.format("[%s] Registered recipe for Heart Shard",
                    MagicHealth.getMagicHealthInstance().getDescription().getName()));
        }

        MagicHealth.getMagicHealthLogger().info(String.format("[%s] Finished registering recipes",
                MagicHealth.getMagicHealthInstance().getDescription().getName()));
    }

}
