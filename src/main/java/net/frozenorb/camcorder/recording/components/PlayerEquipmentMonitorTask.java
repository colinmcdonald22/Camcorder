package net.frozenorb.camcorder.recording.components;

import lombok.AllArgsConstructor;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.recording.Recording;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@AllArgsConstructor
public final class PlayerEquipmentMonitorTask extends BukkitRunnable {

    private Recording recording;
    private Map<UUID, ItemStack[]> previousEquipment = new HashMap<>();

    public void run() {
        if (!Camcorder.getInstance().getRecordingHandler().getActiveRecordings().contains(recording)) {
            cancel();
            return;
        }

        Set<UUID> leftFrame = new HashSet<>(previousEquipment.keySet());

        for (Player player : recording.getWorld().getPlayers()) {
            if (!recording.contains(player)) {
                continue;
            }

            leftFrame.remove(player.getUniqueId());

            ItemStack[] current = new ItemStack[] {
                    player.getItemInHand(),
                    player.getInventory().getBoots(),
                    player.getInventory().getLeggings(),
                    player.getInventory().getChestplate(),
                    player.getInventory().getHelmet()
            };

            if (previousEquipment.containsKey(player.getUniqueId())) {
                ItemStack[] previous = previousEquipment.get(player.getUniqueId());

                for (int i = 0; i < 5; i++) {
                    ItemStack oldItem = previous[i];
                    ItemStack newItem = current[i];

                    if (net.minecraft.server.v1_7_R4.ItemStack)
                }
            } else {
                UUID.randomUUID()
            }

            // Save their current equipment so we can compare it next tick.
            previousEquipment.put(player.getUniqueId(), current);
        }

        // If they left the frame (aka they're not there now), clean up their
        // data so we're not storing unneeded stuff.
        for (UUID player : leftFrame) {
            previousEquipment.remove(player);
        }
    }

    private boolean fastMatches(ItemStack a, ItemStack b) {
        return a == null && b == null?true:(a != null && b != null?a.item == b.item && a.count == b.count && a.damage == b.damage:false);
    }

}