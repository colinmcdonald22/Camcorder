package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.EntityMetadata;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.buffer.Unpooled;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Map;

@ToString
@EqualsAndHashCode
public final class NamedEntitySpawnAction extends Action {

    private int entityId;
    private GameProfile gameProfile;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private Material heldItemType;
    private EntityMetadata metadata;

    public NamedEntitySpawnAction() {}

    public NamedEntitySpawnAction(Player player) {
        this.entityId = player.getEntityId();
        this.gameProfile = ((CraftPlayer) player).getHandle().getProfile();
        this.x = player.getLocation().getX();
        this.y = player.getLocation().getY();
        this.z = player.getLocation().getZ();
        this.yaw = player.getLocation().getYaw();
        this.pitch = player.getLocation().getPitch();
        this.heldItemType = player.getItemInHand() == null ? Material.AIR : player.getItemInHand().getType();

        ByteBuf transfer = Unpooled.buffer();
        PacketDataSerializer packetDataSerializer = new PacketDataSerializer(transfer);
        ((CraftPlayer) player).getHandle().getDataWatcher().a(packetDataSerializer);
        this.metadata = new EntityMetadata();
        metadata.read(transfer);
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        gameProfile = new GameProfile(ByteBufUtils.readUuid(in), ByteBufUtils.readString(in));

        int size = ByteBufUtils.readVarInt(in);

        for(int i = 0; i < size; i++) {
            String name = ByteBufUtils.readString(in);
            String value = ByteBufUtils.readString(in);
            String signature = ByteBufUtils.readString(in);

            gameProfile.getProperties().put(name, new Property(name, value, signature));
        }

        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
        yaw = in.readFloat();
        pitch = in.readFloat();
        heldItemType = Material.getMaterial(ByteBufUtils.readVarInt(in));
        metadata = new EntityMetadata();
        metadata.read(in);
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        ByteBufUtils.writeUuid(gameProfile.getId(), out);
        ByteBufUtils.writeString(gameProfile.getName(), out);

        ByteBufUtils.writeVarInt(gameProfile.getProperties().size(), out);

        for (Map.Entry<String, Property> property : gameProfile.getProperties().entries()) {
            ByteBufUtils.writeString(property.getValue().getName(), out);
            ByteBufUtils.writeString(property.getValue().getValue(), out);
            ByteBufUtils.writeString(property.getValue().getSignature(), out);
        }

        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
        out.writeFloat(yaw);
        out.writeFloat(pitch);
        ByteBufUtils.writeVarInt(heldItemType.getId(), out);
        metadata.write(out);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();

        ReflectionUtils.setField(packet, "a", entityId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "b", gameProfile);
        ReflectionUtils.setField(packet, "c", MathHelper.floor(x * 32D));
        ReflectionUtils.setField(packet, "d", MathHelper.floor(y * 32D));
        ReflectionUtils.setField(packet, "e", MathHelper.floor(z * 32D));
        ReflectionUtils.setField(packet, "f", (byte) ((int) (yaw * 256F / 360F)));
        ReflectionUtils.setField(packet, "g", (byte) ((int) (pitch * 256F / 360F)));
        ReflectionUtils.setField(packet, "h", heldItemType.getId());

        // Mojang is weird. What can I say.
        ReflectionUtils.setField(packet, "i", new DataWatcher(null) {

            public void a(PacketDataSerializer out, int version) {
                metadata.write(out);
            }

        });

        sendPacket(packet, viewer);
    }

}