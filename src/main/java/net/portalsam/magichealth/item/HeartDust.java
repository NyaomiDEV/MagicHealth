package net.portalsam.magichealth.item;

import net.portalsam.magichealth.MagicHealth;
import net.portalsam.magichealth.database.PluginLanguage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class HeartDust {

    static ItemStack item;
    static final NamespacedKey key = new NamespacedKey(MagicHealth.getMagicHealthInstance(), "heart_drain_amulet");

    public static ItemStack getItem() {
        if (item == null){
            ItemStack _item = new ItemStack(Material.RED_DYE, 1);
            ItemMeta meta = _item.getItemMeta();

            assert meta != null;
            meta.setDisplayName(PluginLanguage.getHeartDustName());

            meta.setLore(PluginLanguage.filterItems(PluginLanguage.getHeartDustLore()));
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            // I wanted to add tags to everything but it breaks back-compat with crafting
            //PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
            //persistentDataContainer.set(key, PersistentDataType.BYTE, (byte) 1);

            meta.setCustomModelData(121269);
            _item.setItemMeta(meta);
            item = _item;
        }
        return item;
    }

    public static NamespacedKey getKey() {
        return key;
    }

}
