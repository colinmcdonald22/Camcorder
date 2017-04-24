package net.frozenorb.camcorder.action.actions;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class EntityTeleportAction extends Action {

    private int entityId;
    private int x;
    private int y;
    private int z;
    private byte yaw;
    private byte pitch;
    private boolean onGround;

    public EntityTeleportAction(Entity entity) {
        Location location = entity.getLocation();

        this.entityId = entity.getEntityId();
        this.x = MathHelper.floor(location.getX() * 32.0);
        this.y = MathHelper.floor(location.getY() * 32.0);
        this.z = MathHelper.floor(location.getZ() * 32.0);
        this.yaw = (byte) MathHelper.floor(location.getYaw() * 256.0 / 360.0);
        this.pitch = (byte) MathHelper.floor(location.getPitch() * 256.0 / 360.0);
        this.onGround = entity.isOnGround();
    }

    @Override
    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        x = ByteBufUtils.readVarInt(in);
        y = ByteBufUtils.readVarInt(in);
        z = ByteBufUtils.readVarInt(in);
        yaw = in.readByte();
        pitch = in.readByte();
        onGround = in.readBoolean();
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        ByteBufUtils.writeVarInt(x, out);
        ByteBufUtils.writeVarInt(y, out);
        ByteBufUtils.writeVarInt(z, out);
        out.writeByte(yaw);
        out.writeByte(pitch);
        out.writeBoolean(onGround);
    }

    @Override
    public void play(Player viewer) {
        sendPacket(viewer, new PacketPlayOutEntityTeleport(entityId + Camcorder.ENTITY_ID_OFFSET, x, y, z, yaw, pitch, onGround));
    }

}