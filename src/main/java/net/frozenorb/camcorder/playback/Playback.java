package net.frozenorb.camcorder.playback;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.action.ActionRegistry;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.buffer.Unpooled;
import net.minecraft.util.org.apache.commons.io.IOUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.zip.GZIPInputStream;

public final class Playback {

    @Getter private String id;
    @Getter private int version;
    @Getter private Date created;
    @Getter private int currentTick = 0;
    @Getter private int durationTicks = 0;
    @Getter private UUID playingFor;
    @Getter @Setter private int speed = 1;
    private Map<Integer, Set<Action>> actions = new HashMap<>();

    // Override default public constructor
    Playback(Player player) {
        this.playingFor = player.getUniqueId();
    }

    public boolean tick() {
        Player playingForBukkit = Camcorder.getInstance().getServer().getPlayer(playingFor);

        if (playingForBukkit == null) {
            return false;
        }

        if (speed != 0) {
            for (int i = 0; i < speed; i++) {
                for (Action action : getActions(currentTick + i)) {
                    action.play(Playback.this, playingForBukkit);
                }
            }

            currentTick += speed;
        }

        if (currentTick > durationTicks) {
            playingForBukkit.sendMessage(ChatColor.YELLOW + "Playback ended.");
            return false;
        } else {
            return true;
        }
    }

    public Set<Action> getActions(int tickOffset) {
        return actions.containsKey(tickOffset) ? ImmutableSet.copyOf(actions.get(tickOffset)) : ImmutableSet.of();
    }

    public boolean read(File file) {
        try {
            InputStream fileInputStream = new GZIPInputStream(new FileInputStream(file));
            byte[] data = IOUtils.toByteArray(fileInputStream);

            read(Unpooled.wrappedBuffer(data));

            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean read(ByteBuf in) {
        version = ByteBufUtils.readVarInt(in);

        if (version != Camcorder.RECORDING_VERSION) {
            return false;
        }

        id = ByteBufUtils.readString(in);
        created = new Date(in.readLong());

        int size = ByteBufUtils.readVarInt(in);

        for (int i = 0; i < size; i++) {
            try {
                int id = ByteBufUtils.readVarInt(in);
                int tickOffset = ByteBufUtils.readVarInt(in);
                Action action = ActionRegistry.findAction(id).newInstance();
                action.read(in);

                if (!actions.containsKey(tickOffset)) {
                    actions.put(tickOffset, new HashSet<>());
                }

                durationTicks = Math.max(durationTicks, tickOffset);

                actions.get(tickOffset).add(action);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

}