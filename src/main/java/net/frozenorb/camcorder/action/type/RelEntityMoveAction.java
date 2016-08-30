package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMove;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class RelEntityMoveAction extends Action {

    private int entityId;
    private byte x;
    private byte y;
    private byte z;
    private boolean onGround;

    public RelEntityMoveAction() {}

    public RelEntityMoveAction(Entity entity, int x, int y, int z, boolean onGround) {
        this.entityId = entity.getEntityId();
        this.x = (byte) x;
        this.y = (byte) y;
        this.z = (byte) z;
        this.onGround = onGround;
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        x = in.readByte();
        y = in.readByte();
        z = in.readByte();
        onGround = in.readBoolean();
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeByte(x);
        out.writeByte(y);
        out.writeByte(z);
        out.writeBoolean(onGround);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutRelEntityMove packet = new PacketPlayOutRelEntityMove(entityId + Camcorder.ENTITY_ID_OFFSET, x, y, z, onGround);
        sendPacket(packet, viewer);
    }

}