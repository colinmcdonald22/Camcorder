package net.frozenorb.camcorder;

import lombok.Getter;
import net.frozenorb.camcorder.playback.PlaybackHandler;
import net.frozenorb.camcorder.recording.RecordingHandler;
import net.frozenorb.qlib.command.FrozenCommandHandler;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Camcorder extends JavaPlugin {

    public static final int RECORDING_VERSION = 1;
    public static final int ENTITY_ID_OFFSET = 1000;

    @Getter private static Camcorder instance;

    @Getter private RecordingHandler recordingHandler;
    @Getter private PlaybackHandler playbackHandler;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        playbackHandler = new PlaybackHandler();
        recordingHandler = new RecordingHandler();

        FrozenCommandHandler.registerAll(this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

}