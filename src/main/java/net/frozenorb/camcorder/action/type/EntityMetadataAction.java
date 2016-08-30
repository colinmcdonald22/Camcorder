package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.EntityMetadata;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.buffer.Unpooled;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class EntityMetadataAction extends Action {

    private int entityId;
    private EntityMetadata metadata;

    public EntityMetadataAction() {}

    public EntityMetadataAction(Entity entity) {
        this.entityId = entity.getEntityId();

        ByteBuf transfer = Unpooled.buffer();
        PacketDataSerializer packetDataSerializer = new PacketDataSerializer(transfer);
        ((CraftEntity) entity).getHandle().getDataWatcher().a(packetDataSerializer);
        this.metadata = new EntityMetadata();
        metadata.read(transfer);
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        metadata = new EntityMetadata();
        metadata.read(in);
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        metadata.write(out);
    }

    public void play(Playback playback, Player viewer) {
        /*PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entityId, new ArrayList<>(), true) {

            // Because of Mojang's stupid data watcher system, we have to do this.
            public void b(PacketDataSerializer packetdataserializer) {
                if (packetdataserializer.version < 16) {
                    packetdataserializer.writeInt(entityId);
                } else {
                    packetdataserializer.b(entityId);
                }

                metadata.write(packetdataserializer);
            }

        };

        sendPacket(packet, viewer);*/
    }

}