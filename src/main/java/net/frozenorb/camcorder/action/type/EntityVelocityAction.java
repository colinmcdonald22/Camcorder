package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityVelocity;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@ToString
@EqualsAndHashCode
public final class EntityVelocityAction extends Action {

    private int entityId;
    private double x;
    private double y;
    private double z;

    public EntityVelocityAction() {}

    public EntityVelocityAction(Entity entity, Vector velocity) {
        this.entityId = entity.getEntityId();
        this.x = velocity.getX();
        this.y = velocity.getY();
        this.z = velocity.getZ();
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutEntityVelocity packet = new PacketPlayOutEntityVelocity(entityId + Camcorder.ENTITY_ID_OFFSET, x, y, z);
        sendPacket(packet, viewer);
    }

}