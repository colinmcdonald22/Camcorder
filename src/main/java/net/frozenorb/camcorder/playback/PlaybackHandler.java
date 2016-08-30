package net.frozenorb.camcorder.playback;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.qlib.command.FrozenCommandHandler;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlaybackHandler {

    private Map<UUID, Playback> activePlaybacks = new HashMap<>();

    public PlaybackHandler() {
        FrozenCommandHandler.loadCommandsFromPackage(Camcorder.getInstance(), "net.frozenorb.camcorder.playback.command");
    }

    public Playback startPlayback(Player player, File file) {
        Playback playback = new Playback(player);

        if (!playback.read(file)) {
            return null;
        }

        new BukkitRunnable() {

            public void run() {
                boolean continuePlayback = playback.tick();

                if (!continuePlayback) {
                    cancel();
                }
            }

        }.runTaskTimer(Camcorder.getInstance(), 1L, 1L);

        activePlaybacks.put(player.getUniqueId(), playback);
        return playback;
    }

    public Playback getPlayback(Player player) {
        return activePlaybacks.get(player.getUniqueId());
    }

}