package net.frozenorb.camcorder.action.actions;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityHeadRotation;
import net.minecraft.util.io.netty.buffer.ByteBuf;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class EntityHeadLookAction extends Action {

    private int entityId;
    private byte rotation;

    public EntityHeadLookAction(LivingEntity livingEntity) {
        this.entityId = livingEntity.getEntityId();

        float headRot = ((CraftLivingEntity) livingEntity).getHandle().getHeadRotation();
        this.rotation = (byte) MathHelper.floor(headRot * 256.0 / 360.0);
    }

    @Override
    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        rotation = in.readByte();
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeByte(rotation);
    }

    @Override
    public void play(Player viewer) {
        PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation();

        ReflectionUtils.setField(packet, "a", entityId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "b", rotation);

        sendPacket(viewer, packet);
    }

}