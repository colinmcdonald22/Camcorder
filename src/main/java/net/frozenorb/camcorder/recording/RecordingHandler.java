package net.frozenorb.camcorder.recording;

import com.google.common.collect.ImmutableSet;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.recording.listeners.GeneralListener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public final class RecordingHandler {

    private Set<Recording> activeRecordings = new HashSet<>();

    public RecordingHandler() {
        Bukkit.getPluginManager().registerEvents(new GeneralListener(this), Camcorder.getInstance());
    }

    public Recording startRecording(Location a, Location b) {
        Recording recording = new Recording(a, b);
        activeRecordings.add(recording);
        return recording;
    }

    public void stopRecording(Recording recording, File saveTo) {
        activeRecordings.remove(recording);
        recording.save(saveTo);
    }

    public Recording findRecording(String id) {
        for (Recording recording : activeRecordings) {
            if (recording.getId().equals(id)) {
                return recording;
            }
        }

        return null;
    }

    public void findRecordings(Entity entity, Consumer<Recording> recordingConsumer) {
        findRecordings(entity.getLocation(), recordingConsumer);
    }

    public void findRecordings(Location location, Consumer<Recording> recordingConsumer) {
        for (Recording recording : activeRecordings) {
            if (recording.contains(location)) {
                recordingConsumer.accept(recording);
            }
        }
    }

    public Set<Recording> getActiveRecordings() {
        return ImmutableSet.copyOf(activeRecordings);
    }

}