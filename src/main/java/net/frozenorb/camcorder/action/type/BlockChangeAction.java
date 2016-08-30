package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutBlockChange;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class BlockChangeAction extends Action {

    private int x;
    private int y;
    private int z;
    private Material newType;
    private byte newData;

    public BlockChangeAction() {}

    public BlockChangeAction(Block block) {
        this(block.getType(), block.getData(), block.getLocation());
    }

    public BlockChangeAction(Material newType, byte newData, Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.newType = newType;
        this.newData = newData;
    }

    public void read(ByteBuf in) {
        x = ByteBufUtils.readVarInt(in);
        y = ByteBufUtils.readVarInt(in);
        z = ByteBufUtils.readVarInt(in);
        newType = Material.getMaterial(ByteBufUtils.readVarInt(in));
        newData = in.readByte();
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(x, out);
        ByteBufUtils.writeVarInt(y, out);
        ByteBufUtils.writeVarInt(z, out);
        ByteBufUtils.writeVarInt(newType.getId(), out);
        out.writeByte(newData);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange();

        ReflectionUtils.setField(packet, "a", x);
        ReflectionUtils.setField(packet, "b", y);
        ReflectionUtils.setField(packet, "c", z);
        ReflectionUtils.setField(packet, "block", net.minecraft.server.v1_7_R4.Block.getById(newType.getId()));
        ReflectionUtils.setField(packet, "data", newData);

        sendPacket(packet, viewer);
    }

}