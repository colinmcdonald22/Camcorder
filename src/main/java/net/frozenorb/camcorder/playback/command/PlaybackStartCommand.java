package net.frozenorb.camcorder.playback.command;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;

public final class PlaybackStartCommand {

    @Command(names={ "playback start" }, permissionNode="op")
    public static void playbackStart(Player sender, @Parameter(name="recording id") String recordingId) {
        File recordingFile = new File("recording-" + recordingId + ".camcorder");

        if (!recordingFile.exists()) {
            sender.sendMessage(ChatColor.RED + "No recording found.");
            return;
        }

        Camcorder.getInstance().getPlaybackHandler().startPlayback(sender, recordingFile);
        sender.sendMessage(ChatColor.YELLOW + "Playback started.");
    }

}