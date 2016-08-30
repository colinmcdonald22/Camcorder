package net.frozenorb.camcorder.recording;

import com.google.common.collect.ImmutableSet;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.recording.components.MovementListener;
import net.frozenorb.qlib.command.FrozenCommandHandler;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public final class RecordingHandler {

    private Set<Recording> activeRecordings = new HashSet<>();

    public RecordingHandler() {
        FrozenCommandHandler.loadCommandsFromPackage(Camcorder.getInstance(), "net.frozenorb.camcorder.recording.command");
        Camcorder.getInstance().getServer().getPluginManager().registerEvents(new MovementListener(), Camcorder.getInstance());
    }

    public Recording startRecording(Location a, Location b) {
        Recording recording = new Recording(a, b);
        activeRecordings.add(recording);
        return recording;
    }

    public boolean stopRecording(Recording recording, File saveTo) {
        activeRecordings.remove(recording);

        if (saveTo != null) {
            return recording.write(saveTo);
        } else {
            return true;
        }
    }

    public Recording findRecording(String id) {
        for (Recording recording : activeRecordings) {
            if (recording.getId().equalsIgnoreCase(id)) {
                return recording;
            }
        }

        return null;
    }

    // All of these findRecordings methods use lazy-initialization of the Set.
    public Set<Recording> findRecordings(Location location) {
        Set<Recording> recordings = null;

        for (Recording recording : activeRecordings) {
            if (recording.contains(location)) {
                if (recordings == null) {
                    recordings = new HashSet<>();
                }

                recordings.add(recording);
            }
        }

        return recordings == null ? ImmutableSet.of() : ImmutableSet.copyOf(recordings);
    }

    public Set<Recording> findRecordings(Entity entity) {
        Set<Recording> recordings = null;

        for (Recording recording : activeRecordings) {
            if (recording.contains(entity)) {
                if (recordings == null) {
                    recordings = new HashSet<>();
                }

                recordings.add(recording);
            }
        }

        return recordings == null ? ImmutableSet.of() : ImmutableSet.copyOf(recordings);
    }

    public Set<Recording> findRecordings(Block block) {
        Set<Recording> recordings = null;

        for (Recording recording : activeRecordings) {
            if (recording.contains(block)) {
                if (recordings == null) {
                    recordings = new HashSet<>();
                }

                recordings.add(recording);
            }
        }

        return recordings == null ? ImmutableSet.of() : ImmutableSet.copyOf(recordings);
    }

    public Set<Recording> getActiveRecordings() {
        return ImmutableSet.copyOf(activeRecordings);
    }

}