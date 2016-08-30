package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityHeadRotation;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class EntityHeadRotationAction extends Action {

    private int entityId;
    private byte rotation;

    public EntityHeadRotationAction() {}

    public EntityHeadRotationAction(Entity entity, float yaw) {
        this.entityId = entity.getEntityId();
        this.rotation = (byte) (((yaw % 360) / 360) * 256);
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        rotation = in.readByte();
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeByte(rotation);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation();

        ReflectionUtils.setField(packet, "a", entityId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "b", rotation);

        sendPacket(packet, viewer);
    }

}