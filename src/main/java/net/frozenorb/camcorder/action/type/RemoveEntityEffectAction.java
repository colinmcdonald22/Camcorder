package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.MobEffect;
import net.minecraft.server.v1_7_R4.PacketPlayOutRemoveEntityEffect;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

@ToString
@EqualsAndHashCode
public final class RemoveEntityEffectAction extends Action {

    private int entityId;
    private byte effectId;

    public RemoveEntityEffectAction() {}

    public RemoveEntityEffectAction(LivingEntity livingEntity, PotionEffectType potionEffectType) {
        this.entityId = livingEntity.getEntityId();
        this.effectId = (byte) potionEffectType.getId();
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        effectId = in.readByte();
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeByte(effectId);
    }

    public void play(Playback playback, Player viewer) {
        // We only create a MobEffect because the packet's constructor requires it.
        // Eventually, everything but the effectId is discarded.
        PacketPlayOutRemoveEntityEffect packet = new PacketPlayOutRemoveEntityEffect(entityId + Camcorder.ENTITY_ID_OFFSET, new MobEffect(effectId, 1));
        sendPacket(packet, viewer);
    }

}