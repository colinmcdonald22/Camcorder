package net.frozenorb.camcorder.action.actions;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutCollect;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class CollectItemAction extends Action {

    private int collectedId;
    private int collectorId;

    public CollectItemAction(Item collected, Player collector) {
        this.collectedId = collected.getEntityId();
        this.collectorId = collector.getEntityId();
    }

    @Override
    public void read(ByteBuf in) {
        collectedId = ByteBufUtils.readVarInt(in);
        collectorId = ByteBufUtils.readVarInt(in);
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(collectedId, out);
        ByteBufUtils.writeVarInt(collectorId, out);
    }

    @Override
    public void play(Player viewer) {
        sendPacket(viewer, new PacketPlayOutCollect(collectedId + Camcorder.ENTITY_ID_OFFSET, collectorId + Camcorder.ENTITY_ID_OFFSET));
    }

}