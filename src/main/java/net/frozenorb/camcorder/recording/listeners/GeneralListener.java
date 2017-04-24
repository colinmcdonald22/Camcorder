package net.frozenorb.camcorder.recording.listeners;

import net.frozenorb.camcorder.action.actions.*;
import net.frozenorb.camcorder.recording.Recording;
import net.frozenorb.camcorder.recording.RecordingHandler;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.EntityEquipment;

public final class GeneralListener implements Listener {

    private final RecordingHandler recordingHandler;

    public GeneralListener(RecordingHandler recordingHandler) {
        this.recordingHandler = recordingHandler;
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // We can't use the standing .findRecordings method because we have to track entering/exiting.
        for (Recording recording : recordingHandler.getActiveRecordings()) {
            boolean containsTo = recording.contains(event.getTo());
            boolean containsFrom = recording.contains(event.getFrom());

            if (containsTo && containsFrom) { // In frame.
                recording.record(new EntityLookAndRelativeMoveAction(player, event.getFrom(), event.getTo()));
                recording.record(new EntityHeadLookAction(player));
            } else if (!containsTo && containsFrom) { // Just left frame.
                recording.record(new EntityDestroyAction(player));
            } else if (containsTo) { // Just entered frame
                recording.record(new SpawnPlayerAction(player));
                EntityEquipment equipment = player.getEquipment();

                recording.record(new EntityEquipmentAction(player, EntityEquipmentAction.Slot.BOOTS, equipment.getBoots()));
                recording.record(new EntityEquipmentAction(player, EntityEquipmentAction.Slot.LEGGINGS, equipment.getLeggings()));
                recording.record(new EntityEquipmentAction(player, EntityEquipmentAction.Slot.CHESTPLATE, equipment.getChestplate()));
                recording.record(new EntityEquipmentAction(player, EntityEquipmentAction.Slot.HELMET, equipment.getHelmet()));
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Item item = event.getItem();
        Player player = event.getPlayer();

        recordingHandler.findRecordings(item, recording -> {
            if (recording.contains(player)) {
                recording.record(new CollectItemAction(item, player));
            }

            recording.record(new EntityDestroyAction(item));
        });
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        recordingHandler.findRecordings(player, recording -> {
            recording.record(new EntityEquipmentAction(player, EntityEquipmentAction.Slot.HAND, player.getItemInHand()));
        });
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerAnimation(PlayerAnimationEvent event) {
        Player player = event.getPlayer();

        recordingHandler.findRecordings(player, recording -> {
            recording.record(new AnimationAction(player, AnimationAction.Animation.SWING_ARM));
        });
    }

}