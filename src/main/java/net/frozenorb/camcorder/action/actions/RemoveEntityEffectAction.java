package net.frozenorb.camcorder.action.actions;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.MobEffect;
import net.minecraft.server.v1_7_R4.PacketPlayOutRemoveEntityEffect;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class RemoveEntityEffectAction extends Action {

    private int entityId;
    private byte effectId;

    public RemoveEntityEffectAction(LivingEntity livingEntity, PotionEffectType potionEffectType) {
        this.entityId = livingEntity.getEntityId();
        this.effectId = (byte) potionEffectType.getId();
    }

    @Override
    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        effectId = in.readByte();
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeByte(effectId);
    }

    @Override
    public void play(Player viewer) {
        sendPacket(viewer, new PacketPlayOutRemoveEntityEffect(entityId + Camcorder.ENTITY_ID_OFFSET, new MobEffect(effectId, 1)));
    }

}