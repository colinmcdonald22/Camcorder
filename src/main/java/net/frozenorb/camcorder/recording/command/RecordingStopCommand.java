package net.frozenorb.camcorder.recording.command;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.recording.Recording;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;

public final class RecordingStopCommand {

    @Command(names={ "recording stop" }, permission="op")
    public static void recordingStop(Player sender, @Param(name="recording id") String recordingId) {
        Recording recording = Camcorder.getInstance().getRecordingHandler().findRecording(recordingId);

        if (recording == null) {
            sender.sendMessage(ChatColor.RED + "No recording found.");
            return;
        }

        File saveTo = new File(recording.getId() + ".ccr");
        Camcorder.getInstance().getRecordingHandler().stopRecording(recording, saveTo);
        sender.sendMessage(ChatColor.GREEN + "Recording stopped. " + ChatColor.YELLOW + "Id: " + recording.getId() + ", File: " + saveTo.getAbsolutePath());
    }

}