package net.frozenorb.camcorder.action.actions;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutAnimation;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class AnimationAction extends Action {

    private int entityId;
    private int animation;

    public AnimationAction(LivingEntity entity, Animation animation) {
        this.entityId = entity.getEntityId();
        this.animation = animation.getValue();
    }

    @Override
    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        animation = ByteBufUtils.readVarInt(in);
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        ByteBufUtils.writeVarInt(animation, out);
    }

    @Override
    public void play(Player viewer) {
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation();

        ReflectionUtils.setField(packet, "a", entityId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "b", animation);

        sendPacket(viewer, packet);
    }

    public enum Animation {

        SWING_ARM(0),
        EAT_FOOD(3),
        CRITICAL(4),
        MAGIC_CRITICAL(5);

        @Getter private int value;

        Animation(int value) {
            this.value = value;
        }

    }

}