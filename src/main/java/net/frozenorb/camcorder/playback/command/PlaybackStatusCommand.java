package net.frozenorb.camcorder.playback.command;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.recording.Recording;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class PlaybackStatusCommand {

    @Command(names={ "playback status" }, permission="op")
    public static void playbackStatus(Player sender) {
        Playback playback = Camcorder.getInstance().getPlaybackHandler().getPlayback(sender);

        if (playback == null) {
            sender.sendMessage(ChatColor.RED + "You are not watching a recording.");
        } else {
            Recording recording = playback.getRecording();

            sender.sendMessage(ChatColor.GREEN + "You are watching recording " + ChatColor.YELLOW + recording.getId() + ChatColor.GREEN + ".");
            sender.sendMessage(ChatColor.GREEN + "Recording captured: " + ChatColor.WHITE + recording.getStarted());
            sender.sendMessage(ChatColor.GREEN + "Recording location: " + ChatColor.WHITE + "(" + recording.getRegionMinX() + ", " + recording.getRegionMinZ() + ") -> (" + recording.getRegionMaxX() + ", " + recording.getRegionMaxZ() + ") in world " + recording.getRegionWorld());
            sender.sendMessage(ChatColor.GREEN + "Playback speed: " + ChatColor.WHITE + (playback.getSpeed() == 0 ? "Paused" : playback.getSpeed() + "x"));
        }
    }

}