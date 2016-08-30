package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityLook;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class EntityLookAction extends Action {

    private int entityId;
    private byte yaw;
    private byte pitch;
    private boolean onGround;

    public EntityLookAction() {}

    public EntityLookAction(Entity entity, int yaw, int pitch, boolean onGround) {
        this.entityId = entity.getEntityId();
        this.yaw = (byte) yaw;
        this.pitch = (byte) pitch;
        this.onGround = onGround;
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        yaw = in.readByte();
        pitch = in.readByte();
        onGround = in.readBoolean();
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeByte(yaw);
        out.writeByte(pitch);
        out.writeBoolean(onGround);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutEntityLook packet = new PacketPlayOutEntityLook(entityId + Camcorder.ENTITY_ID_OFFSET, yaw, pitch, onGround);
        sendPacket(packet, viewer);
    }

}