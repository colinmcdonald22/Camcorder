package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntity;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class EntityAction extends Action {

    private int entityId;

    public EntityAction() {}

    public EntityAction(Entity entity) {
        this.entityId = entity.getEntityId();
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutEntity packet = new PacketPlayOutEntity(entityId + Camcorder.ENTITY_ID_OFFSET);
        sendPacket(packet, viewer);
    }

}