package net.frozenorb.camcorder.action.actions;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.EnumEntitySize;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMoveLook;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class EntityLookAndRelativeMoveAction extends Action {

    private int entityId;
    private byte x;
    private byte y;
    private byte z;
    private byte yaw;
    private byte pitch;
    private boolean onGround;

    public EntityLookAndRelativeMoveAction(Entity entity, Location oldLocation, Location newLocation) {
        this.entityId = entity.getEntityId();
        this.x = (byte) (EnumEntitySize.SIZE_2.a(newLocation.getX()) - EnumEntitySize.SIZE_2.a(oldLocation.getX()));
        this.y = (byte) (MathHelper.floor(newLocation.getY() * 32.0) - MathHelper.floor(oldLocation.getY() * 32.0));
        this.z = (byte) (EnumEntitySize.SIZE_2.a(newLocation.getZ()) - EnumEntitySize.SIZE_2.a(oldLocation.getZ()));
        this.yaw = (byte) MathHelper.floor(newLocation.getYaw() * 256.0 / 360.0);
        this.pitch = (byte) MathHelper.floor(newLocation.getPitch() * 256.0 / 360.0);
        this.onGround = entity.isOnGround();
    }

    @Override
    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        x = in.readByte();
        y = in.readByte();
        z = in.readByte();
        yaw = in.readByte();
        pitch = in.readByte();
        onGround = in.readBoolean();
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeByte(x);
        out.writeByte(y);
        out.writeByte(z);
        out.writeByte(yaw);
        out.writeByte(pitch);
        out.writeBoolean(onGround);
    }

    @Override
    public void play(Player viewer) {
        sendPacket(viewer, new PacketPlayOutRelEntityMoveLook(entityId + Camcorder.ENTITY_ID_OFFSET, x, y, z, yaw, pitch, onGround));
    }

}