package net.frozenorb.camcorder.recording.command;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.recording.Recording;
import net.frozenorb.qlib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class RecordingStartCommand {

    @Command(names={ "recording start" }, permission="op")
    public static void recordingStart(Player sender) {
        WorldEditPlugin worldEdit = (WorldEditPlugin) Camcorder.getInstance().getServer().getPluginManager().getPlugin("WorldEdit");

        if (worldEdit == null) {
            sender.sendMessage(ChatColor.RED + "WorldEdit not found.");
            return;
        }

        CuboidSelection selection = (CuboidSelection) worldEdit.getSelection(sender);

        if (selection == null) {
            sender.sendMessage(ChatColor.RED + "You must have an area selected!");
            return;
        }

        Recording recording = Camcorder.getInstance().getRecordingHandler().startRecording(selection.getMinimumPoint(), selection.getMaximumPoint());
        sender.sendMessage(ChatColor.GREEN + "Recording started. " + ChatColor.YELLOW + "Id: " + recording.getId());
    }

}