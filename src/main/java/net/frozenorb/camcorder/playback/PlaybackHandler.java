package net.frozenorb.camcorder.playback;

import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class PlaybackHandler {

    private Map<UUID, Playback> activePlaybacks = new HashMap<>();

    public void startPlayback(Set<UUID> viewers, File file) {
        Playback playback = new Playback(file, viewers);

        new PlaybackThread(playback).start();
        viewers.forEach(v -> activePlaybacks.put(v, playback));
    }

    public Playback getPlayback(Player player) {
        return activePlaybacks.get(player.getUniqueId());
    }

}