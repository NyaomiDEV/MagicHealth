package net.portalsam.magichealth.event;

import net.portalsam.magichealth.MagicHealth;
import net.portalsam.magichealth.config.MagicHealthConfig;
import net.portalsam.magichealth.database.PlayerHealth;
import net.portalsam.magichealth.item.HeartDust;
import net.portalsam.magichealth.item.HeartShard;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class MagicEntityDeathEvent implements Listener {

    public static List<EntityType> commonMobList = new ArrayList<>();
    public static List<EntityType> uncommonMobList = new ArrayList<>();
    public static List<EntityType> bossMobList = new ArrayList<>();

    private static final MagicHealth magicHealth = MagicHealth.getMagicHealthInstance();
    private static final Logger log = MagicHealth.getMagicHealthLogger();

    static {

        for(String entity : MagicHealthConfig.getCommonMobList()) {

            try {

                Class<? extends Entity> entityClass = EntityType.valueOf(entity.toUpperCase()).getEntityClass();
                assert entityClass != null;

                if(LivingEntity.class.isAssignableFrom(entityClass)) {
                    commonMobList.add(EntityType.valueOf(entity.toUpperCase()));
                } else {
                    throw new IllegalArgumentException();
                }

            } catch(IllegalArgumentException ignored) {

                log.info(String.format("[%s] Entity '" + entity + "' was not recognized in config.yml, did you type the EntityType correctly, or is it not a LivingEntity?", magicHealth.getDescription().getName()));

            }

        }

        for(String entity : MagicHealthConfig.getUncommonMobList()) {

            try {

                Class<? extends Entity> entityClass = EntityType.valueOf(entity.toUpperCase()).getEntityClass();
                assert entityClass != null;

                if(LivingEntity.class.isAssignableFrom(entityClass)) {
                    uncommonMobList.add(EntityType.valueOf(entity.toUpperCase()));
                } else {
                    throw new IllegalArgumentException();
                }

            } catch(IllegalArgumentException ignored) {

                log.info(String.format("[%s] Entity '" + entity + "' was not recognized in config.yml, did you type the EntityType correctly, or is it not a LivingEntity?", magicHealth.getDescription().getName()));

            }

        }

        for(String entity : MagicHealthConfig.getBossMobList()) {

            try {

                Class<? extends Entity> entityClass = EntityType.valueOf(entity.toUpperCase()).getEntityClass();
                assert entityClass != null;

                if(LivingEntity.class.isAssignableFrom(entityClass)) {
                    bossMobList.add(EntityType.valueOf(entity.toUpperCase()));
                } else {
                    throw new IllegalArgumentException();
                }

            } catch(IllegalArgumentException ignored) {

                log.info(String.format("[%s] Entity '" + entity + "' was not recognized in config.yml, did you type the EntityType correctly, or is it not a LivingEntity?", magicHealth.getDescription().getName()));

            }

        }

    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        if(MagicHealthConfig.mobDropsAreEnabled()) {

            // Get the data that was set in MagicEntitySpawnEvent, if it's true exit this branch.

            LivingEntity entity = event.getEntity();
            PersistentDataContainer spawnDataContainer = entity.getPersistentDataContainer();
            String entityData = spawnDataContainer.get(new NamespacedKey(MagicHealth.getMagicHealthInstance(), "disable_magicDrops"), PersistentDataType.STRING);

            if(entityData == null) entityData = "false";
            if(entityData.equalsIgnoreCase("true")) return;

            if(event.getEntity().getKiller() != null) {

                // Generate a number to determine drops.
                float dropChance = (float)ThreadLocalRandom.current().nextDouble(0D, 100D);

                // List of boss mobs.

                for(EntityType entityType : bossMobList) {

                    if(entityType == entity.getType()) {

                        // If the player is at max health and the server disables them looting heart dust, don't do anything.
                        if(MagicHealthConfig.doMaxHealthPlayersLootHeartDust()) {
                            if(dropChance <= MagicHealthConfig.getBossMobDropChance()) {

                                ItemStack drop = HeartShard.getItem();
                                drop.setAmount(ThreadLocalRandom.current().nextInt(MagicHealthConfig.getBossMobDropAmounts()[0], MagicHealthConfig.getBossMobDropAmounts()[1]));

                                Objects.requireNonNull(entity.getLocation().getWorld()).dropItem(entity.getLocation(), drop);

                            }
                        } else if(PlayerHealth.getPlayerMaximumHealthFromDatabase(Objects.requireNonNull(entity.getKiller())) < MagicHealthConfig.getMaximumPlayerHealth()) {
                            if(dropChance <= MagicHealthConfig.getBossMobDropChance()) {

                                ItemStack drop = HeartShard.getItem();
                                drop.setAmount(ThreadLocalRandom.current().nextInt(MagicHealthConfig.getBossMobDropAmounts()[0], MagicHealthConfig.getBossMobDropAmounts()[1]));

                                Objects.requireNonNull(entity.getLocation().getWorld()).dropItem(entity.getLocation(), drop);

                            }
                        } else return;

                    }
                }

                // List of uncommon mobs.

                for(EntityType entityType : uncommonMobList) {

                    if(entityType == entity.getType()) {

                        // If the player is at max health and the server disables them looting heart dust, don't do anything.
                        if(MagicHealthConfig.doMaxHealthPlayersLootHeartDust()) {
                            if (dropChance <= MagicHealthConfig.getUncommonMobDropChance()) {

                                ItemStack drop = HeartDust.getItem();
                                drop.setAmount(ThreadLocalRandom.current().nextInt(MagicHealthConfig.getUncommonMobDropAmounts()[0], MagicHealthConfig.getUncommonMobDropAmounts()[1]));

                                Objects.requireNonNull(entity.getLocation().getWorld()).dropItem(entity.getLocation(), drop);

                            }
                        } else if(PlayerHealth.getPlayerMaximumHealthFromDatabase(Objects.requireNonNull(entity.getKiller())) < MagicHealthConfig.getMaximumPlayerHealth()) {
                            if (dropChance <= MagicHealthConfig.getUncommonMobDropChance()) {

                                ItemStack drop = HeartDust.getItem();
                                drop.setAmount(ThreadLocalRandom.current().nextInt(MagicHealthConfig.getUncommonMobDropAmounts()[0], MagicHealthConfig.getUncommonMobDropAmounts()[1]));

                                Objects.requireNonNull(entity.getLocation().getWorld()).dropItem(entity.getLocation(), drop);

                            }
                        } else return;
                    }
                }

                // List of common mobs.

                for(EntityType entityType : commonMobList) {

                    if(entityType == entity.getType()) {

                        // If the player is at max health and the server disables them looting heart dust, don't do anything.
                        if(MagicHealthConfig.doMaxHealthPlayersLootHeartDust()) {
                            if (dropChance <= MagicHealthConfig.getCommonMobDropChance()) {

                                ItemStack drop = HeartDust.getItem();
                                drop.setAmount(ThreadLocalRandom.current().nextInt(MagicHealthConfig.getCommonMobDropAmounts()[0], MagicHealthConfig.getCommonMobDropAmounts()[1]));

                                Objects.requireNonNull(entity.getLocation().getWorld()).dropItem(entity.getLocation(), drop);

                            }
                        } else if(PlayerHealth.getPlayerMaximumHealthFromDatabase(Objects.requireNonNull(entity.getKiller())) < MagicHealthConfig.getMaximumPlayerHealth()) {
                            if (dropChance <= MagicHealthConfig.getCommonMobDropChance()) {

                                ItemStack drop = HeartDust.getItem();
                                drop.setAmount(ThreadLocalRandom.current().nextInt(MagicHealthConfig.getCommonMobDropAmounts()[0], MagicHealthConfig.getCommonMobDropAmounts()[1]));

                                Objects.requireNonNull(entity.getLocation().getWorld()).dropItem(entity.getLocation(), drop);

                            }
                        } else return;
                    }
                }
            }
        }

    }
}