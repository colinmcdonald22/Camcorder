package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class EntityTeleportAction extends Action {

    private int entityId;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public EntityTeleportAction() {}

    public EntityTeleportAction(Entity entity, Location newLocation) {
        this.entityId = entity.getEntityId();
        this.x = newLocation.getX();
        this.y = newLocation.getY();
        this.z = newLocation.getZ();
        this.yaw = newLocation.getYaw();
        this.pitch = newLocation.getPitch();
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
        yaw = in.readFloat();
        pitch = in.readFloat();
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
        out.writeFloat(yaw);
        out.writeFloat(pitch);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
        
        ReflectionUtils.setField(packet, "a", entityId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "b", MathHelper.floor(x * 32D));
        ReflectionUtils.setField(packet, "c", MathHelper.floor(y * 32D));
        ReflectionUtils.setField(packet, "d", MathHelper.floor(z * 32D));
        ReflectionUtils.setField(packet, "e", (byte) ((int) (yaw * 256F / 360F)));
        ReflectionUtils.setField(packet, "f", (byte) ((int) (pitch * 256F / 360F)));

        sendPacket(packet, viewer);
    }

}