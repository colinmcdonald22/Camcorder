package net.frozenorb.camcorder.action.actions;

import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityStatus;
import net.minecraft.util.io.netty.buffer.ByteBuf;

import org.bukkit.EntityEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class EntityStatusAction extends Action {

    private int entityId;
    private byte status;

    public EntityStatusAction(Entity entity, EntityEffect status) {
        this.entityId = entity.getEntityId();
        this.status = status.getData();
    }

    @Override
    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        status = in.readByte();
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeByte(status);
    }

    @Override
    public void play(Player viewer) {
        PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus();

        ReflectionUtils.setField(packet, "a", entityId);
        ReflectionUtils.setField(packet, "b", status);

        sendPacket(viewer, packet);
    }

}