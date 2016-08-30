package net.frozenorb.camcorder.playback.command;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class PlaybackStatusCommand {

    @Command(names={ "playback status" }, permissionNode="op")
    public static void playbackStatus(Player sender) {
        Playback playback = Camcorder.getInstance().getPlaybackHandler().getPlayback(sender);

        if (playback == null) {
            sender.sendMessage(ChatColor.RED + "You are not watching a recording.");
        } else {
            sender.sendMessage(ChatColor.GREEN + "You are watching recording " + ChatColor.YELLOW + playback.getId() + ChatColor.GREEN + ".");
            sender.sendMessage(ChatColor.GREEN + "Recording duration: " + ChatColor.WHITE + TimeUtils.formatIntoMMSS(playback.getCurrentTick() / 20) + "/" + TimeUtils.formatIntoMMSS(playback.getDurationTicks() / 20));
            sender.sendMessage(ChatColor.GREEN + "Recording version: " + ChatColor.WHITE + playback.getId());
            sender.sendMessage(ChatColor.GREEN + "Recording time: " + ChatColor.WHITE + playback.getCreated());
            sender.sendMessage(ChatColor.GREEN + "Playback speed: " + ChatColor.WHITE + (playback.getSpeed() == 0 ? "Paused" : playback.getSpeed() + "x"));
        }
    }

}