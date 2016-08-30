package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityWeather;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class SpawnEntityWeatherAction extends Action {

    private int entityId;
    private int x;
    private int y;
    private int z;

    public SpawnEntityWeatherAction() {}

    public SpawnEntityWeatherAction(LightningStrike lightningStrike) {
        this.entityId = lightningStrike.getEntityId();
        this.x = lightningStrike.getLocation().getBlockX();
        this.y = lightningStrike.getLocation().getBlockY();
        this.z = lightningStrike.getLocation().getBlockZ();
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        x = ByteBufUtils.readVarInt(in);
        y = ByteBufUtils.readVarInt(in);
        z = ByteBufUtils.readVarInt(in);
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        ByteBufUtils.writeVarInt(x, out);
        ByteBufUtils.writeVarInt(y, out);
        ByteBufUtils.writeVarInt(z, out);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutSpawnEntityWeather packet = new PacketPlayOutSpawnEntityWeather();

        ReflectionUtils.setField(packet, "a", entityId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "b", MathHelper.floor(x * 32D));
        ReflectionUtils.setField(packet, "c", MathHelper.floor(y * 32D));
        ReflectionUtils.setField(packet, "d", MathHelper.floor(z * 32D));
        ReflectionUtils.setField(packet, "e", 1); // 1 = Lightning

        sendPacket(packet, viewer);
    }

}