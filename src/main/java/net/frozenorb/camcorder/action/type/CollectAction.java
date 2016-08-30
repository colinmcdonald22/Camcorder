package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutCollect;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class CollectAction extends Action {

    private int collectedId;
    private int collectorId;

    public CollectAction() {}

    public CollectAction(Item collected, Player collector) {
        this.collectedId = collected.getEntityId();
        this.collectorId = collector.getEntityId();
    }

    public void read(ByteBuf in) {
        collectedId = ByteBufUtils.readVarInt(in);
        collectorId = ByteBufUtils.readVarInt(in);
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(collectedId, out);
        ByteBufUtils.writeVarInt(collectorId, out);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutCollect packet = new PacketPlayOutCollect(collectedId + Camcorder.ENTITY_ID_OFFSET, collectorId + Camcorder.ENTITY_ID_OFFSET);
        sendPacket(packet, viewer);
    }

}