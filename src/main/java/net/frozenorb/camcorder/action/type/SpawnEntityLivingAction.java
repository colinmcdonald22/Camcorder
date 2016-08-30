package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.EntityMetadata;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.buffer.Unpooled;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class SpawnEntityLivingAction extends Action {

    public static final double MOTION_MAX = 3.9D;

    private int entityId;
    private int entityType;
    private EnumEntitySize entitySize;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private float headPitch;
    private double motX;
    private double motY;
    private double motZ;
    private EntityMetadata metadata;

    public SpawnEntityLivingAction() {}

    public SpawnEntityLivingAction(LivingEntity livingEntity) {
        this.entityId = livingEntity.getEntityId();
        this.entityType = livingEntity.getType().getTypeId();
        this.entitySize = ((CraftLivingEntity) livingEntity).getHandle().as;
        this.x = livingEntity.getLocation().getX();
        this.y = livingEntity.getLocation().getY();
        this.z = livingEntity.getLocation().getZ();
        this.yaw = livingEntity.getLocation().getYaw();
        this.pitch = livingEntity.getLocation().getPitch();
        this.headPitch = ((CraftLivingEntity) livingEntity).getHandle().aO;
        this.motX = livingEntity.getVelocity().getX();
        this.motY = livingEntity.getVelocity().getY();
        this.motZ = livingEntity.getVelocity().getZ();

        ByteBuf transfer = Unpooled.buffer();
        PacketDataSerializer packetDataSerializer = new PacketDataSerializer(transfer);
        ((CraftLivingEntity) livingEntity).getHandle().getDataWatcher().a(packetDataSerializer);
        this.metadata = new EntityMetadata();
        metadata.read(transfer);

        motX = Math.max(motX, -MOTION_MAX);
        motX = Math.min(motX, MOTION_MAX);

        motY = Math.max(motY, -MOTION_MAX);
        motY = Math.min(motY, MOTION_MAX);

        motZ = Math.max(motZ, -MOTION_MAX);
        motZ = Math.min(motZ, MOTION_MAX);
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        entityType = ByteBufUtils.readVarInt(in);
        entitySize = EnumEntitySize.values()[ByteBufUtils.readVarInt(in)];
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
        yaw = in.readFloat();
        pitch = in.readFloat();
        motX = in.readDouble();
        motY = in.readDouble();
        motZ = in.readDouble();
        metadata = new EntityMetadata();
        metadata.read(in);
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        ByteBufUtils.writeVarInt(entityType, out);
        ByteBufUtils.writeVarInt(entitySize.ordinal(), out);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
        out.writeFloat(yaw);
        out.writeFloat(pitch);
        out.writeDouble(motX);
        out.writeDouble(motY);
        out.writeDouble(motZ);
        metadata.write(out);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();

        ReflectionUtils.setField(packet, "a", entityId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "b", (byte) entityType);
        ReflectionUtils.setField(packet, "c", entitySize.a(x));
        ReflectionUtils.setField(packet, "d", MathHelper.floor(y * 32D));
        ReflectionUtils.setField(packet, "e", entitySize.a(z));
        ReflectionUtils.setField(packet, "i", (byte) ((int) (yaw * 256F / 360F)));
        ReflectionUtils.setField(packet, "j", (byte) ((int) (pitch * 256F / 360F)));
        ReflectionUtils.setField(packet, "k", (byte) ((int) (headPitch * 256F / 360F)));
        ReflectionUtils.setField(packet, "f", (int) (motX * 8000D));
        ReflectionUtils.setField(packet, "g", (int) (motY * 8000D));
        ReflectionUtils.setField(packet, "h", (int) (motZ * 8000D));

        // Mojang is weird. What can I say.
        ReflectionUtils.setField(packet, "l", new DataWatcher(null) {

            public void a(PacketDataSerializer out, int version) {
                metadata.write(out);
            }

        });

        sendPacket(packet, viewer);
    }

}