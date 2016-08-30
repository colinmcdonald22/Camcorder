package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.Block;
import net.minecraft.server.v1_7_R4.PacketPlayOutBlockAction;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class BlockActionAction extends Action {

    private int x;
    private int y;
    private int z;
    private byte byte1;
    private byte byte2;
    private Material blockType;

    public BlockActionAction() {}

    public BlockActionAction(Material blockType, Location location, byte byte1, byte byte2) {
        this.blockType = blockType;
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.byte1 = byte1;
        this.byte2 = byte2;
    }

    public void read(ByteBuf in) {
        x = ByteBufUtils.readVarInt(in);
        y = ByteBufUtils.readVarInt(in);
        z = ByteBufUtils.readVarInt(in);
        byte1 = in.readByte();
        byte2 = in.readByte();
        blockType = Material.getMaterial(ByteBufUtils.readVarInt(in));
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(x, out);
        ByteBufUtils.writeVarInt(y, out);
        ByteBufUtils.writeVarInt(z, out);
        out.writeByte(byte1);
        out.writeByte(byte2);
        ByteBufUtils.writeVarInt(blockType.getId(), out);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(x, y, z, Block.getById(blockType.getId()), byte1, byte2);
        sendPacket(packet, viewer);
    }

}