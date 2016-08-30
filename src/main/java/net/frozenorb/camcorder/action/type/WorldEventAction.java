package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutWorldEvent;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class WorldEventAction extends Action {

    private int effectId;
    private int x;
    private int y;
    private int z;
    private int data;
    private boolean disableRelVolume;

    public WorldEventAction() {}

    public WorldEventAction(int effectId, Location location, int data, boolean disableRelVolume) {
        this.effectId = effectId;
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.data = data;
        this.disableRelVolume = disableRelVolume;
    }

    public void read(ByteBuf in) {
        effectId = ByteBufUtils.readVarInt(in);
        x = ByteBufUtils.readVarInt(in);
        y = ByteBufUtils.readVarInt(in);
        z = ByteBufUtils.readVarInt(in);
        data = ByteBufUtils.readVarInt(in);
        disableRelVolume = in.readBoolean();
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(effectId, out);
        ByteBufUtils.writeVarInt(x, out);
        ByteBufUtils.writeVarInt(y, out);
        ByteBufUtils.writeVarInt(z, out);
        ByteBufUtils.writeVarInt(data, out);
        out.writeBoolean(disableRelVolume);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(effectId, x, y, z, data, disableRelVolume);
        sendPacket(packet, viewer);
    }

}