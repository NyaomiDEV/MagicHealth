package net.portalsam.magichealth.event;

import net.portalsam.magichealth.MagicHealth;
import net.portalsam.magichealth.config.MagicHealthConfig;
import net.portalsam.magichealth.database.PlayerHealth;
import net.portalsam.magichealth.database.PluginLanguage;
import net.portalsam.magichealth.item.HeartCrystal;
import net.portalsam.magichealth.item.HeartDrainAmulet;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;
import java.util.logging.Logger;

public class MagicPlayerInteractEvent implements Listener {

    private static final MagicHealth magicHealth = MagicHealth.getMagicHealthInstance();
    private static final Logger log = MagicHealth.getMagicHealthLogger();

    /*/ Item right click event. /*/

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        try {

                // Check if item used is the heart_crystal.
                if(Objects.requireNonNull(player.getInventory().getItemInMainHand().getItemMeta()).getPersistentDataContainer().has(HeartCrystal.getKey(), PersistentDataType.BYTE)) {
                    if(event.getAction() == Action.RIGHT_CLICK_AIR) {

                        // Check if the config has the heart_crystal enabled.
                        if(MagicHealthConfig.isEnablingHeartCrystal()) {

                        // Prevent the player from using this unless they can.
                        if(PlayerHealth.getPlayerMaximumHealth(player) >= MagicHealthConfig.getMaximumPlayerHealth()) {
                            player.sendMessage(PluginLanguage.filterDefault(PluginLanguage.getHeartCrystalAlreadyMax()));
                        } else {

                            // Increase the players health by what's in the configuration, or a heart by default.
                            PlayerHealth.increasePlayerMaxHealth(player, MagicHealthConfig.getIncreaseHealthBy(), true);
                            log.info(String.format("[%s] player " + player.getDisplayName() + " used a Heart Crystal, their new health is " + PlayerHealth.getPlayerMaximumHealth(player), magicHealth.getDescription().getName()));

                            // Play a particle effect and sound sequence.
                            player.spawnParticle(Particle.HEART, player.getEyeLocation(), 75, 0.5, 0.5, 0.5);
                            player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.7f, 0.95f);

                            //Destroy item after use.
                            if (player.getInventory().getItemInMainHand().getAmount() > 1)
                                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                            else
                                player.getInventory().setItemInMainHand(null);

                        }
                    } else {
                            player.sendMessage(PluginLanguage.filterDefault(PluginLanguage.getHeartCrystalDisabled()));
                        }
                }
            }

                // Check if item used is the heart_drain_amulet.
                if(Objects.requireNonNull(player.getInventory().getItemInMainHand().getItemMeta()).getPersistentDataContainer().has(HeartDrainAmulet.getKey(), PersistentDataType.BYTE)) {
                    if(event.getAction() == Action.RIGHT_CLICK_AIR) {

                        // Check if the config has the heart_drain_amulet enabled.
                        if(MagicHealthConfig.isEnablingHeartDrainAmulet()) {

                        // Prevent the player from using this unless they can.
                        if(PlayerHealth.getPlayerMaximumHealth(player) <= MagicHealthConfig.getMinimumPlayerHealth()) {
                            player.sendMessage(PluginLanguage.filterDefault(PluginLanguage.getHeartDrainAmuletAlreadyMin()));
                        } else {

                            // Decrease the players health by what's in the configuration, or a heart by default.
                            PlayerHealth.decreasePlayerMaxHealth(player, MagicHealthConfig.getDecreaseHealthBy(), true);
                            log.info(String.format("[%s] player " + player.getDisplayName() + " used a Heart Drain Amulet, their new health is " + PlayerHealth.getPlayerMaximumHealth(player), magicHealth.getDescription().getName()));

                            // Play a particle effect and sound sequence.
                            player.spawnParticle(Particle.DRAGON_BREATH, player.getEyeLocation(), 15, 1, 1, 1);
                            player.playSound(player.getEyeLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 0.7f, 1.15f);

                            //Destroy item after use.
                            if (player.getInventory().getItemInMainHand().getAmount() > 1)
                                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                            else
                                player.getInventory().setItemInMainHand(null);

                        }
                    } else {
                            player.sendMessage(PluginLanguage.filterDefault(PluginLanguage.getHeartDrainAmuletDisabled()));
                        }
                }
            }

        } catch(Exception ignored) {
            // I'm lazy :)
        }

    }
}