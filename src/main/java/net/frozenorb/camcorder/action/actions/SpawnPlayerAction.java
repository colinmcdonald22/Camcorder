package net.frozenorb.camcorder.action.actions;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.WatchableObject;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;
import net.minecraft.util.io.netty.buffer.ByteBuf;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public final class SpawnPlayerAction extends Action {

    private int entityId;
    private GameProfile gameProfile;
    private int x;
    private int y;
    private int z;
    private byte yaw;
    private byte pitch;
    private short currentItem;
    private List<WatchableObject> metadata;

    public SpawnPlayerAction(Player player) {
        Location location = player.getLocation();
        DataWatcher dataWatcher = ((CraftEntity) player).getHandle().getDataWatcher();

        this.entityId = player.getEntityId();
        this.gameProfile = ((CraftPlayer) player).getProfile();
        this.x = MathHelper.floor(location.getX() * 32.0);
        this.y = MathHelper.floor(location.getY() * 32.0);
        this.z = MathHelper.floor(location.getZ() * 32.0);
        this.yaw = (byte) MathHelper.floor(location.getYaw() * 256.0 / 360.0);
        this.pitch = (byte) MathHelper.floor(location.getPitch() * 256.0 / 360.0);
        this.currentItem = (short) (player.getItemInHand() == null ? 0 : player.getItemInHand().getTypeId());
        this.metadata = dataWatcher.c();
    }

    @Override
    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        gameProfile = new GameProfile(ByteBufUtils.readUuid(in), ByteBufUtils.readString(in));

        /*for (int i = 0; i < ByteBufUtils.readVarInt(in); i++) {
            String name = ByteBufUtils.readString(in);
            gameProfile.getProperties().put(name, new Property(name, ByteBufUtils.readString(in), ByteBufUtils.readString(in)));
        }*/

        x = ByteBufUtils.readVarInt(in);
        y = ByteBufUtils.readVarInt(in);
        z = ByteBufUtils.readVarInt(in);
        yaw = in.readByte();
        pitch = in.readByte();
        currentItem = in.readShort();
        metadata = DataWatcher.b(new PacketDataSerializer(in)); // see EntityMetadataAction
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        ByteBufUtils.writeUuid(gameProfile.getId(), out);
        ByteBufUtils.writeString(gameProfile.getName(), out);
        /*ByteBufUtils.writeVarInt(gameProfile.getProperties().size(), out);

        for (Property property : gameProfile.getProperties().values()) {
            ByteBufUtils.writeString(property.getName(), out);
            ByteBufUtils.writeString(property.getValue(), out);
            ByteBufUtils.writeString(property.getSignature(), out);
        }*/

        ByteBufUtils.writeVarInt(x, out);
        ByteBufUtils.writeVarInt(y, out);
        ByteBufUtils.writeVarInt(z, out);
        out.writeByte(yaw);
        out.writeByte(pitch);
        out.writeShort(currentItem);
        DataWatcher.a(metadata, new PacketDataSerializer(out), 5); // see EntityMetadataAction
    }

    @Override
    public void play(Player viewer) {
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();

        ReflectionUtils.setField(packet, "a", entityId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "b", gameProfile);
        ReflectionUtils.setField(packet, "c", x);
        ReflectionUtils.setField(packet, "d", y);
        ReflectionUtils.setField(packet, "e", z);
        ReflectionUtils.setField(packet, "f", yaw);
        ReflectionUtils.setField(packet, "g", pitch);
        ReflectionUtils.setField(packet, "h", currentItem);
        ReflectionUtils.setField(packet, "i", new DataWatcher(null) {

            @Override
            public void a(PacketDataSerializer out, int version) {
                // we store a List<WatchableObject> in this packet, not a
                // DataWatcher, so we create a dummy one which just writes our List
                DataWatcher.a(metadata, out, version);
            }

        });

        sendPacket(viewer, packet);
    }

}