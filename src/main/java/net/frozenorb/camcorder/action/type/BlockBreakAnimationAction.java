package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutBlockBreakAnimation;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class BlockBreakAnimationAction extends Action {

    private int entityId;
    private int x;
    private int y;
    private int z;
    private byte data;

    public BlockBreakAnimationAction() {}

    public BlockBreakAnimationAction(Player player, Location location, byte data) {
        this.entityId = player.getEntityId();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.data = data;
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        x = ByteBufUtils.readVarInt(in);
        y = ByteBufUtils.readVarInt(in);
        z = ByteBufUtils.readVarInt(in);
        data = in.readByte();
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        ByteBufUtils.writeVarInt(x, out);
        ByteBufUtils.writeVarInt(y, out);
        ByteBufUtils.writeVarInt(z, out);
        out.writeByte(data);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(entityId + Camcorder.ENTITY_ID_OFFSET, x, y, z, data);
        sendPacket(packet, viewer);
    }

}