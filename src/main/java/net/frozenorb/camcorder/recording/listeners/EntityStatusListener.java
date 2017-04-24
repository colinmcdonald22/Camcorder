package net.frozenorb.camcorder.recording.listeners;

import net.frozenorb.camcorder.action.actions.EntityStatusAction;
import net.frozenorb.camcorder.recording.RecordingHandler;

import org.bukkit.EntityEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public final class EntityStatusListener implements Listener {

    private final RecordingHandler recordingHandler;

    public EntityStatusListener(RecordingHandler recordingHandler) {
        this.recordingHandler = recordingHandler;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        recordingHandler.findRecordings(event.getEntity(), recording -> {
            recording.record(new EntityStatusAction(event.getEntity(), EntityEffect.DEATH));
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();

        // this if statement is taken from NMS (EntityLiving line 680)
        if (entity.getNoDamageTicks() <= (entity.getMaximumNoDamageTicks() / 2.0)) {
            recordingHandler.findRecordings(entity, recording -> {
                recording.record(new EntityStatusAction(entity, EntityEffect.HURT));
            });
        }
    }

    // there's no way to subscribe to a hierarchy of events, so we just delegate
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        onEntityDamage(event);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        onEntityDamage(event);
    }

}