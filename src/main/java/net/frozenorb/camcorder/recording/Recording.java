package net.frozenorb.camcorder.recording;

import com.google.common.collect.ImmutableList;

import lombok.Getter;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.action.ActionRegistry;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.qlib.qLib;
import net.minecraft.util.io.netty.buffer.ByteBuf;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class Recording {

    @Getter private final String id;
    @Getter private final Instant started;
    @Getter private final String regionWorld;
    @Getter private final int regionMinX;
    @Getter private final int regionMaxX;
    @Getter private final int regionMinZ;
    @Getter private final int regionMaxZ;
    @Getter private final List<Action> actions;

    // creates a recording for playback
    public Recording(File file) {
        ByteBuf in = ByteBufUtils.readGzipped(file);
        int version = ByteBufUtils.readVarInt(in);

        if (version != Camcorder.RECORDING_VERSION) {
            throw new IllegalStateException("Cannot read outdated file! File version: " + version + ", current version: " + Camcorder.RECORDING_VERSION);
        }

        id = ByteBufUtils.readString(in);
        started = Instant.ofEpochMilli(in.readLong());
        regionWorld = ByteBufUtils.readString(in);
        regionMinX = ByteBufUtils.readVarInt(in);
        regionMaxX = ByteBufUtils.readVarInt(in);
        regionMinZ = ByteBufUtils.readVarInt(in);
        regionMaxZ = ByteBufUtils.readVarInt(in);

        int actionCount = ByteBufUtils.readVarInt(in);
        Bukkit.broadcastMessage("preparing to read" + actionCount);
        List<Action> loadingActions = new ArrayList<>(actionCount);

        for (int i = 0; i < actionCount; i++) {
            int type = ByteBufUtils.readVarInt(in);
            int msOffset = ByteBufUtils.readVarInt(in);

            try {
                Action action = ActionRegistry.findAction(type).newInstance();
                action.read(in);
                action.setMsOffset(msOffset);

                loadingActions.add(action);
            } catch (Exception ex) {
                throw new RuntimeException("Could not read action " + type + " (index " + i + ")", ex);
            }
        }

        actions = ImmutableList.copyOf(loadingActions);
    }

    // creates a writable recording
    public Recording(Location a, Location b) {
        id = String.valueOf(qLib.RANDOM.nextInt(100_000));
        started = Instant.now();
        regionWorld = a.getWorld().getName();
        regionMinX = Math.min(a.getBlockX(), b.getBlockX());
        regionMaxX = Math.max(a.getBlockX(), b.getBlockX());
        regionMinZ = Math.min(a.getBlockZ(), b.getBlockZ());
        regionMaxZ = Math.max(a.getBlockZ(), b.getBlockZ());
        actions = new ArrayList<>();
    }

    public boolean contains(Entity entity) {
        return contains(entity.getLocation());
    }

    public boolean contains(Location location) {
        return location.getWorld().getName().equals(regionWorld)
            && location.getBlockX() >= regionMinX
            && location.getBlockX() <= regionMaxX
            && location.getBlockZ() >= regionMinZ
            && location.getBlockZ() <= regionMaxZ;
    }

    public void record(Action action) {
        action.setMsOffset((int) (System.currentTimeMillis() - started.toEpochMilli()));
        actions.add(action);
    }

    public void save(File file) {
        ByteBufUtils.writeGzipped(file, out -> {
            ByteBufUtils.writeVarInt(Camcorder.RECORDING_VERSION, out);
            ByteBufUtils.writeString(id, out);
            out.writeLong(started.toEpochMilli());
            ByteBufUtils.writeString(regionWorld, out);
            ByteBufUtils.writeVarInt(regionMinX, out);
            ByteBufUtils.writeVarInt(regionMaxX, out);
            ByteBufUtils.writeVarInt(regionMinZ, out);
            ByteBufUtils.writeVarInt(regionMaxZ, out);

            ByteBufUtils.writeVarInt(actions.size(), out);
            Bukkit.broadcastMessage("wrote " + actions.size() + " actions!");

            for (Action action : actions) {
                ByteBufUtils.writeVarInt(ActionRegistry.findId(action.getClass()), out);
                ByteBufUtils.writeVarInt(action.getMsOffset(), out);
                action.write(out);
            }
        });
    }

}