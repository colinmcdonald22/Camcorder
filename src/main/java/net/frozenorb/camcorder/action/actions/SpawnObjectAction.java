package net.frozenorb.camcorder.action.actions;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity;
import net.minecraft.util.io.netty.buffer.ByteBuf;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import lombok.NoArgsConstructor;

// Ids are from EntityTrackerEntry line 441 (or http://wiki.vg/index.php?title=Entities&oldid=6052)
// Data values are from http://wiki.vg/index.php?title=Object_Data&oldid=7152
@NoArgsConstructor
public final class SpawnObjectAction extends Action {

    private int entityId;
    private byte type;
    private int x;
    private int y;
    private int z;
    // the order of pitch/yaw is inverted in this packet :(
    private byte pitch;
    private byte yaw;
    private int data;
    private short motX;
    private short motY;
    private short motZ;

    // See link at top of this class
    public SpawnObjectAction(Entity entity, int type, int data) {
        Location location = entity.getLocation();

        this.entityId = entity.getEntityId();
        this.type = (byte) type;
        this.x = MathHelper.floor(location.getX() * 32.0);
        this.y = MathHelper.floor(location.getY() * 32.0);
        this.z = MathHelper.floor(location.getZ() * 32.0);
        this.pitch = (byte) MathHelper.floor(location.getPitch() * 256.0 / 360.0);
        this.yaw = (byte) MathHelper.floor(location.getYaw() * 256.0 / 360.0);
        this.data = data;

        if (data > 0) {
            Vector velocity = entity.getVelocity();

            // clamp values to +/- 3.9 (Mojang does this, likely overflow prevention?)
            double velocityX = Math.min(Math.max(velocity.getX(), -3.9), 3.9);
            double velocityY = Math.min(Math.max(velocity.getY(), -3.9), 3.9);
            double velocityZ = Math.min(Math.max(velocity.getZ(), -3.9), 3.9);

            this.motX = (short) (velocityX * 8000.0);
            this.motY = (short) (velocityY * 8000.0);
            this.motZ = (short) (velocityZ * 8000.0);
        }
    }

    @Override
    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        type = in.readByte();
        x = ByteBufUtils.readVarInt(in);
        y = ByteBufUtils.readVarInt(in);
        z = ByteBufUtils.readVarInt(in);
        pitch = in.readByte();
        yaw = in.readByte();
        data = ByteBufUtils.readVarInt(in);

        if (data > 0) {
            motX = in.readShort();
            motY = in.readShort();
            motZ = in.readShort();
        }
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeByte(type);
        ByteBufUtils.writeVarInt(x, out);
        ByteBufUtils.writeVarInt(y, out);
        ByteBufUtils.writeVarInt(z, out);
        out.writeByte(pitch);
        out.writeByte(yaw);
        ByteBufUtils.writeVarInt(data, out);

        if (data > 0) {
            out.writeShort(motX);
            out.writeShort(motY);
            out.writeShort(motZ);
        }
    }

    @Override
    public void play(Player viewer) {
        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity();

        ReflectionUtils.setField(packet, "a", entityId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "j", type);
        ReflectionUtils.setField(packet, "b", x);
        ReflectionUtils.setField(packet, "c", y);
        ReflectionUtils.setField(packet, "d", z);
        ReflectionUtils.setField(packet, "h", pitch);
        ReflectionUtils.setField(packet, "i", yaw);
        ReflectionUtils.setField(packet, "k", data);
        ReflectionUtils.setField(packet, "e", motX);
        ReflectionUtils.setField(packet, "f", motY);
        ReflectionUtils.setField(packet, "g", motZ);

        sendPacket(viewer, packet);
    }

}