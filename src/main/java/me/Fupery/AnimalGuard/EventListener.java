package me.Fupery.AnimalGuard;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

abstract class EventListener implements Listener {

    void processEvent(Cancellable event, Player player, Entity entity, EventAction action) {
        if (!action.isProtectedFor(entity.getType())
                || player.hasPermission(action.getPermission())
                || WGBukkit.getPlugin().canBuild(player, entity.getLocation())) {
            return;
        }
        if (!event.isCancelled()) {
            player.sendMessage(AnimalGuard.getDenyMessage());
        }
        event.setCancelled(true);
    }

    void processEvent(Cancellable event, Entity entity) {
        if (!EventAction.ENTITY_ATTACK.isProtectedFor(entity.getType())) {
            return;
        }
        World world = entity.getWorld();
        ApplicableRegionSet set = WGBukkit.getRegionManager(world).getApplicableRegions(entity.getLocation());

        if (set.size() > 0) {
            event.setCancelled(true);
        }
    }

    enum EventAction {
        ATTACK, INTERACT, LEASH, UNLEASH, SHEAR, ENTITY_ATTACK;

        private static Set<EntityType> protectedEntities;

        public String getPermission() {
            return "animalguard.player." + name().toLowerCase();
        }

        public boolean isProtectedFor(EntityType type) {
            return protectedEntities.contains(type);
        }

        public void loadProtections(FileConfiguration config) {
            protectedEntities = new ConcurrentSkipListSet<>();
            List<String> protectedTypes = config.getStringList(name());
            String error = String.format("Error processing protections for action '%s' in config: ", name());
            //todo hardcoding

            if (protectedTypes == null) {
                AnimalGuard.plugin().getLogger().warning(error + "No protection values could be found.");
                //todo hardcoding
                return;
            }
            for (String entityType : protectedTypes) {
                try {
                    EntityType type = EntityType.valueOf(entityType.toUpperCase());
                    protectedEntities.add(type);

                } catch (IllegalArgumentException e) {
                    AnimalGuard.plugin().getLogger().warning(String.format(error +
                            "EntityType '%s' is invalid", entityType));
                    //todo hardcoding
                }
            }
        }
    }
}
