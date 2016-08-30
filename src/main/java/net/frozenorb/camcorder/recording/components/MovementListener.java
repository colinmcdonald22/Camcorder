package net.frozenorb.camcorder.recording.components;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.type.*;
import net.frozenorb.camcorder.recording.Recording;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.EntityEquipment;

public final class MovementListener implements Listener {

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerMove(PlayerMoveEvent event) {
        // We can't use the standing .findRecordings method because we have to track entering/exiting.
        for (Recording recording : Camcorder.getInstance().getRecordingHandler().getActiveRecordings()) {
            boolean containsTo = recording.contains(event.getTo());
            boolean containsFrom = recording.contains(event.getFrom());

            if (containsTo && containsFrom) { // In frame.
                recording.recordAction(new EntityMoveLookAction(event.getPlayer(), event.getFrom(), event.getTo()));
                recording.recordAction(new EntityHeadRotationAction(event.getPlayer(), event.getTo().getYaw()));
            } else if (!containsTo && containsFrom) { // Just left frame.
                recording.recordAction(new EntityDestroyAction(event.getPlayer()));
            } else if (containsTo) { // Just entered frame
                recording.recordAction(new NamedEntitySpawnAction(event.getPlayer()));
                EntityEquipment equipment = event.getPlayer().getEquipment();

                recording.recordAction(new EntityEquipmentAction(event.getPlayer(), 1, equipment.getBoots()));
                recording.recordAction(new EntityEquipmentAction(event.getPlayer(), 2, equipment.getLeggings()));
                recording.recordAction(new EntityEquipmentAction(event.getPlayer(), 3, equipment.getChestplate()));
                recording.recordAction(new EntityEquipmentAction(event.getPlayer(), 4, equipment.getHelmet()));
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onEntityDamage(EntityDamageEvent event) {
        for (Recording recording : Camcorder.getInstance().getRecordingHandler().findRecordings(event.getEntity())) {
            recording.recordAction(new DamageAnimationAction((LivingEntity) event.getEntity()));
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        for (Recording recording : Camcorder.getInstance().getRecordingHandler().findRecordings(event.getItem())) {
            // We record the destroy action always, but only the collect if the player involved is in the frame.
            if (recording.contains(event.getPlayer())) {
                recording.recordAction(new CollectAction(event.getItem(), event.getPlayer()));
            }

            recording.recordAction(new EntityDestroyAction(event.getItem()));
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onBlockBreak(BlockBreakEvent event) {
        for (Recording recording : Camcorder.getInstance().getRecordingHandler().findRecordings(event.getBlock())) {
            //recording.recordAction(new WorldEventAction(2001, event.getBlock().getLocation(), event.getBlock().getTypeId(), false));
            recording.recordAction(new BlockChangeAction(Material.AIR, (byte) 0, event.getBlock().getLocation()));
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onBlockPlace(BlockPlaceEvent event) {
        for (Recording recording : Camcorder.getInstance().getRecordingHandler().findRecordings(event.getBlock())) {
            recording.recordAction(new BlockChangeAction(event.getBlock()));
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        for (Recording recording : Camcorder.getInstance().getRecordingHandler().findRecordings(event.getPlayer())) {
            recording.recordAction(new EntityEquipmentAction(event.getPlayer(), 0, event.getPlayer().getInventory().getItem(event.getNewSlot())));
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerAnimation(PlayerAnimationEvent event) {
        for (Recording recording : Camcorder.getInstance().getRecordingHandler().findRecordings(event.getPlayer())) {
            recording.recordAction(new ArmAnimationAction(event.getPlayer()));
        }
    }

}