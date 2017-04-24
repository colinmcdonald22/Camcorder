package net.frozenorb.camcorder.recording.listeners;

import net.frozenorb.camcorder.action.actions.SpawnGlobalEntityAction;
import net.frozenorb.camcorder.recording.RecordingHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

public final class SpawnGlobalEntityListener implements Listener {

    private final RecordingHandler recordingHandler;

    public SpawnGlobalEntityListener(RecordingHandler recordingHandler) {
        this.recordingHandler = recordingHandler;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLightningStrikeEvent(LightningStrikeEvent event) {
        recordingHandler.findRecordings(event.getLightning().getLocation(), recording -> {
            recording.record(new SpawnGlobalEntityAction(event.getLightning()));
        });
    }

}