package net.frozenorb.camcorder.playback.command;

import com.google.common.collect.ImmutableSet;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.action.actions.EntityLookAndRelativeMoveAction;
import net.frozenorb.camcorder.action.actions.SpawnPlayerAction;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.frozenorb.qlib.qLib;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class PlaybackStartCommand {

    @Command(names={ "playback start" }, permission="op")
    public static void playbackStart(Player sender, @Param(name="recording id") String recordingId, @Param(name="view with", defaultValue = "self") Player viewWith) {
        File recordingFile = new File(recordingId + ".ccr");

        if (!recordingFile.exists()) {
            sender.sendMessage(ChatColor.RED + "No recording found.");
            return;
        }

        Set<UUID> viewers = new HashSet<>();

        viewers.add(sender.getUniqueId());
        viewers.add(viewWith.getUniqueId());

        Camcorder.getInstance().getPlaybackHandler().startPlayback(viewers, recordingFile);
        sender.sendMessage(ChatColor.YELLOW + "Playback started.");
    }

}