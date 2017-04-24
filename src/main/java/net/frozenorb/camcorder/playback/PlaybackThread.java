package net.frozenorb.camcorder.playback;

import net.frozenorb.camcorder.action.Action;

import org.bukkit.Bukkit;

import java.util.Objects;

public final class PlaybackThread extends Thread {

    private final Playback playback;

    PlaybackThread(Playback playback) {
        this.playback = playback;
    }

    @Override
    public void run() {
        long time = 0;

        for (Action action : playback.getRecording().getActions()) {
            while (playback.getSpeed() == 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            long delay = action.getMsOffset() - time;

            if (delay != 0) {
                try {
                    Thread.sleep((long) (delay / playback.getSpeed()));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                time += delay;
            }

            playback.getViewers().stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(action::play);
        }
    }

}