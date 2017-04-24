package net.frozenorb.camcorder.playback.command;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class PlaybackSpeedCommand{

    @Command(names={ "playback speed" }, permission="op")
    public static void playbackSpeed(Player sender, @Param(name="speed") double speed) {
        Playback playback = Camcorder.getInstance().getPlaybackHandler().getPlayback(sender);

        if (playback == null) {
            sender.sendMessage(ChatColor.RED + "You are not watching a recording.");
        } else {
            playback.setSpeed(speed);
            sender.sendMessage(ChatColor.GREEN + "New speed: " + ChatColor.WHITE + (playback.getSpeed() == 0 ? "Paused" : playback.getSpeed() + "x"));
        }
    }

}