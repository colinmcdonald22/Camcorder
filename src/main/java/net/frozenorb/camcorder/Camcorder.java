package net.frozenorb.camcorder;

import lombok.Getter;
import net.frozenorb.camcorder.playback.PlaybackHandler;
import net.frozenorb.camcorder.recording.RecordingHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Camcorder extends JavaPlugin {

    public static final int RECORDING_VERSION = 1;
    public static final int ENTITY_ID_OFFSET = 1000;

    @Getter private static Camcorder instance;

    @Getter private RecordingHandler recordingHandler;
    @Getter private PlaybackHandler playbackHandler;
    @Getter private int tick = 0;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        playbackHandler = new PlaybackHandler();
        recordingHandler = new RecordingHandler();

        new BukkitRunnable() {

            public void run() {
                tick++;
            }

        }.runTaskTimer(this, 1L, 1L);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    // TODO: Actually record data
    // TODO: Code to 'init' a recording -- record entities, chunk data, etc. etc.
    // TODO: Code to be able to end a recording -- reset blocks, entities, etc.
    // TODO: Finish partially done + need to do packets.
    // TODO: Save even more space by pre-converting doubles+floats+stuff into bytes

        /*
            Done -----------------------------

            ENTITY_EQUIPMENT
            COLLECT
            UPDATE_SIGN
            REMOVE_ENTITY_EFFECT
            ENTITY_DESTROY
            ENTITY_EFFECT
            ENTITY
            SPAWN_ENTITY_WEATHER
            BED
            ANIMATION
            ENTITY_VELOCITY
            WORLD_PARTICLES
            NAMED_SOUND_EFFECT
            WORLD_EVENT
            REL_ENTITY_MOVE
            ENTITY_MOVE_LOOK
            ENTITY_LOOK
            ENTITY_HEAD_ROTATION
            ENTITY_TELEPORT
            BLOCK_ACTION
            BLOCK_BREAK_ANIMATION
            BLOCK_CHANGE
            SPAWN_ENTITY_EXPERIENCE_ORB
            NAMED_ENTITY_SPAWN
            SPAWN_ENTITY_LIVING

            Partially done -----------------------------

            EXPLOSION (Possibly store player data, possibly store records?)
            ATTACH_ENTITY (Verify how us<->NMS should work, as the constructor is *really* weird.)
            SPAWN_ENTITY (Needs entity type byte and object data stuff.)
            ENTITY_METADATA (It crashed my client. Yeah.)

            Need to do -----------------------------

            SPAWN_ENTITY_PAINTING
            ENTITY_STATUS
            MULTI_BLOCK_CHANGE
     */

}