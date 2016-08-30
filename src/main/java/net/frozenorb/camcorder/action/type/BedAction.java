package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutBed;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class BedAction extends Action {

    private int entityId;
    private int x;
    private int y;
    private int z;

    public BedAction() {}

    public BedAction(Entity entity, Location bedLocation) {
        this.entityId = entity.getEntityId();
        this.x = bedLocation.getBlockX();
        this.y = bedLocation.getBlockY();
        this.z = bedLocation.getBlockZ();
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
        PacketPlayOutBed packet = new PacketPlayOutBed();

        ReflectionUtils.setField(packet, "a", entityId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "b", x);
        ReflectionUtils.setField(packet, "c", y);
        ReflectionUtils.setField(packet, "d", z);

        sendPacket(packet, viewer);
    }

}