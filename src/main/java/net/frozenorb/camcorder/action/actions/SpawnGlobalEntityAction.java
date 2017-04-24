package net.frozenorb.camcorder.action.actions;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityWeather;
import net.minecraft.util.io.netty.buffer.ByteBuf;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class SpawnGlobalEntityAction extends Action {

    private int entityId;
    private byte type;
    private int x;
    private int y;
    private int z;

    public SpawnGlobalEntityAction(LightningStrike lightningStrike) {
        this(lightningStrike, (byte) 1); // Currently only use for this packet
    }

    public SpawnGlobalEntityAction(Entity entity, byte type) {
        Location location = entity.getLocation();

        this.entityId = entity.getEntityId();
        this.type = type;
        this.x = MathHelper.floor(location.getX() * 32.0);
        this.y = MathHelper.floor(location.getY() * 32.0);
        this.z = MathHelper.floor(location.getZ() * 32.0);
    }

    @Override
    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        type = in.readByte();
        x = ByteBufUtils.readVarInt(in);
        y = ByteBufUtils.readVarInt(in);
        z = ByteBufUtils.readVarInt(in);
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeByte(type);
        ByteBufUtils.writeVarInt(x, out);
        ByteBufUtils.writeVarInt(y, out);
        ByteBufUtils.writeVarInt(z, out);
    }

    @Override
    public void play(Player viewer) {
        PacketPlayOutSpawnEntityWeather packet = new PacketPlayOutSpawnEntityWeather();

        ReflectionUtils.setField(packet, "a", entityId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "b", x);
        ReflectionUtils.setField(packet, "c", y);
        ReflectionUtils.setField(packet, "d", z);
        ReflectionUtils.setField(packet, "e", type);

        sendPacket(viewer, packet);
    }

}