package net.portalsam.magichealth.item;

import net.portalsam.magichealth.MagicHealth;
import net.portalsam.magichealth.database.PluginLanguage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class HeartCrystal {

    static ItemStack item;
    static final NamespacedKey key = new NamespacedKey(MagicHealth.getMagicHealthInstance(), "heart_crystal");

    static ShapedRecipe recipe;

    public static ItemStack getItem() {
        if (item == null) {
            ItemStack _item = new ItemStack(Material.DIAMOND, 1);
            ItemMeta meta = _item.getItemMeta();

            assert meta != null;
            meta.setDisplayName(PluginLanguage.filterItems(PluginLanguage.getHeartCrystalName()));

            meta.setLore(PluginLanguage.filterItems(PluginLanguage.getHeartCrystalLore()));
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
            persistentDataContainer.set(key, PersistentDataType.BYTE, (byte) 1);

            meta.setCustomModelData(121269);
            _item.setItemMeta(meta);
            item = _item;
        }
        return item;
    }

    public static NamespacedKey getKey() {
        return key;
    }

    public static Recipe getRecipe() {
        if (recipe == null){
            recipe = new ShapedRecipe(key, getItem());
            RecipeChoice heartShard = new RecipeChoice.ExactChoice(HeartShard.getItem());

            recipe.shape("SSS", "SDS", "SSS");
            recipe.setIngredient('S', heartShard);
            recipe.setIngredient('D', Material.DIAMOND);
        }

        return recipe;
    }

}