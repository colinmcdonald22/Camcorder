package net.frozenorb.camcorder.action.actions;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.MobEffect;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEffect;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class EntityEffectAction extends Action {

    private int entityId;
    private byte effectId;
    private byte amplifier;
    private int duration;
    private boolean hideParticles;

    public EntityEffectAction(LivingEntity livingEntity, PotionEffect potionEffect) {
        this.entityId = livingEntity.getEntityId();
        this.effectId = (byte) potionEffect.getType().getId();
        this.amplifier = (byte) potionEffect.getAmplifier();
        this.duration = potionEffect.getDuration();
        this.hideParticles = potionEffect.isAmbient();
    }

    @Override
    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        effectId = in.readByte();
        amplifier = in.readByte();
        duration = ByteBufUtils.readVarInt(in);
        hideParticles = in.readBoolean();
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeByte(effectId);
        out.writeByte(amplifier);
        ByteBufUtils.writeVarInt(duration, out);
        out.writeBoolean(hideParticles);
    }

    @Override
    public void play(Player viewer) {
        sendPacket(viewer, new PacketPlayOutEntityEffect(entityId + Camcorder.ENTITY_ID_OFFSET, new MobEffect(effectId, duration, amplifier, hideParticles)));
    }

}