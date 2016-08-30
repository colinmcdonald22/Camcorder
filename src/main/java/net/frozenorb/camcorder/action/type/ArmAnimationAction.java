package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutAnimation;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class ArmAnimationAction extends Action {

    private int entityId;

    public ArmAnimationAction() {}

    public ArmAnimationAction(Player entity) {
        this.entityId = entity.getEntityId();
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation();

        ReflectionUtils.setField(packet, "a", entityId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "b", 0); // 0 = Swing arm

        sendPacket(packet, viewer);
    }

}