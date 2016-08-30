package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class SpawnEntityAction extends Action {

    private int entityId;
    private byte entityType;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    // TODO: Entity type value, object data

    public SpawnEntityAction() {}

    public SpawnEntityAction(Entity entity) {
        this.entityId = entity.getEntityId();
        this.entityType = 0;
        this.x = entity.getLocation().getX();
        this.y = entity.getLocation().getY();
        this.z = entity.getLocation().getZ();
        this.pitch = entity.getLocation().getPitch();
        this.yaw = entity.getLocation().getYaw();
        // objectData
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        entityType = in.readByte();
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
        pitch = in.readFloat();
        yaw = in.readFloat();
        // objectData
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeByte(entityType);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
        out.writeFloat(pitch);
        out.writeFloat(yaw);
        // objectData
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity();

        ReflectionUtils.setField(packet, "a", entityId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "j", entityType);
        ReflectionUtils.setField(packet, "b", MathHelper.floor(x * 32D));
        ReflectionUtils.setField(packet, "c", MathHelper.floor(y * 32D));
        ReflectionUtils.setField(packet, "d", MathHelper.floor(z * 32D));
        ReflectionUtils.setField(packet, "h", MathHelper.d(pitch * 256.0F / 360.0F));
        ReflectionUtils.setField(packet, "i", MathHelper.d(yaw * 256.0F / 360.0F));
        // objectData

        sendPacket(packet, viewer);
    }

}