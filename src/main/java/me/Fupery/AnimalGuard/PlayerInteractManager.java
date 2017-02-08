package me.Fupery.AnimalGuard;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

class PlayerInteractManager extends EventListener {

    @EventHandler
    public void onPlayerDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            processEvent(event, ((Player) event.getDamager()), event.getEntity(), EventAction.ATTACK);

        } else {
            processEvent(event, event.getEntity());
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        processEvent(event, event.getPlayer(), event.getRightClicked(), EventAction.INTERACT);
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        processEvent(event, event.getPlayer(), event.getRightClicked(), EventAction.INTERACT);
    }

    @EventHandler
    public void onPlayerShear(PlayerShearEntityEvent event) {
        processEvent(event, event.getPlayer(), event.getEntity(), EventAction.SHEAR);
    }

    @EventHandler
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        processEvent(event, event.getPlayer(), event.getEntity(), EventAction.LEASH);
    }

    @EventHandler
    public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event) {
        processEvent(event, event.getPlayer(), event.getEntity(), EventAction.UNLEASH);
    }
}
