package net.frozenorb.camcorder.action.actions;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.WatchableObject;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class EntityMetadataAction extends Action {

    private int entityId;
    private List<WatchableObject> metadata;

    public EntityMetadataAction(Entity entity) {
        this.entityId = entity.getEntityId();

        // there is an option to do diff-based here, but we'd have to track that ourselves
        // (as we can't mutate the "actual" data value while recording)
        DataWatcher dataWatcher = ((CraftEntity) entity).getHandle().getDataWatcher();
        this.metadata = dataWatcher.c();
    }

    @Override
    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        // we just wrap our ByteBuf so we can use NMS's serialization code
        metadata = DataWatcher.b(new PacketDataSerializer(in));
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        // we serialize with version=5 (1.7.10) just because it's the "native" version
        DataWatcher.a(metadata, new PacketDataSerializer(out), 5);
    }

    @Override
    public void play(Player viewer) {
        // the boolean value is ignored so we just put whatever
        sendPacket(viewer, new PacketPlayOutEntityMetadata(entityId + Camcorder.ENTITY_ID_OFFSET, metadata, false));
    }

}