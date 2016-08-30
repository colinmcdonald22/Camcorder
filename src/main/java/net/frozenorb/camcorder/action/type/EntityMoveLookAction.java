package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMoveLook;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class EntityMoveLookAction extends Action {

    private int entityId;
    private byte x;
    private byte y;
    private byte z;
    private byte yaw;
    private byte pitch;
    private boolean onGround;

    public EntityMoveLookAction() {}

    public EntityMoveLookAction(Entity entity, Location oldLocation, Location newLocation) {
        int oldX = (int) (oldLocation.getX() * 32D);
        int oldY = (int) (oldLocation.getY() * 32D);
        int oldZ = (int) (oldLocation.getZ() * 32D);
        int newX = (int) (newLocation.getX() * 32D);
        int newY = (int) (newLocation.getY() * 32D);
        int newZ = (int) (newLocation.getZ() * 32D);

        this.entityId = entity.getEntityId();
        this.x = (byte) (newX - oldX);
        this.y = (byte) (newY - oldY);
        this.z = (byte) (newZ - oldZ);
        this.yaw = (byte) (((newLocation.getYaw() % 360) / 360) * 256);
        this.pitch = (byte) (((newLocation.getPitch() % 360) / 360) * 256);
        this.onGround = entity.isOnGround();
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        x = in.readByte();
        y = in.readByte();
        z = in.readByte();
        yaw = in.readByte();
        pitch = in.readByte();
        onGround = in.readBoolean();
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeByte(x);
        out.writeByte(y);
        out.writeByte(z);
        out.writeByte(yaw);
        out.writeByte(pitch);
        out.writeBoolean(onGround);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutRelEntityMoveLook(entityId + Camcorder.ENTITY_ID_OFFSET, x, y, z, yaw, pitch, onGround);
        sendPacket(packet, viewer);
    }

}