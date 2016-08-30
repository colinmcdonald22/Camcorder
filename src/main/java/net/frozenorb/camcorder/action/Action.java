package net.frozenorb.camcorder.action;

import lombok.Getter;
import lombok.Setter;
import net.frozenorb.camcorder.playback.Playback;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public abstract class Action {

    @Getter @Setter private int tickOffset;

    public abstract void read(ByteBuf in);
    public abstract void write(ByteBuf out);
    public abstract void play(Playback playback, Player viewer);

    public void sendPacket(Packet packet, Player target) {
        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet);
    }

}