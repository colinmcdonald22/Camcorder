package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutUpdateSign;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class UpdateSignAction extends Action {

    private int x;
    private int y;
    private int z;
    // If we were to use an array we'd be fixed at 4 elements, so this is equal.
    private String line1;
    private String line2;
    private String line3;
    private String line4;

    public UpdateSignAction() {}

    public UpdateSignAction(Location signLocation, String[] signLines) {
        this.x = signLocation.getBlockX();
        this.y = signLocation.getBlockY();
        this.z = signLocation.getBlockZ();
        this.line1 = signLines[0];
        this.line2 = signLines[1];
        this.line3 = signLines[2];
        this.line4 = signLines[3];
    }

    public void read(ByteBuf in) {
        x = ByteBufUtils.readVarInt(in);
        y = ByteBufUtils.readVarInt(in);
        z = ByteBufUtils.readVarInt(in);
        line1 = ByteBufUtils.readString(in);
        line2 = ByteBufUtils.readString(in);
        line3 = ByteBufUtils.readString(in);
        line4 = ByteBufUtils.readString(in);
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(x, out);
        ByteBufUtils.writeVarInt(y, out);
        ByteBufUtils.writeVarInt(z, out);
        ByteBufUtils.writeString(line1, out);
        ByteBufUtils.writeString(line2, out);
        ByteBufUtils.writeString(line3, out);
        ByteBufUtils.writeString(line4, out);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutUpdateSign packet = new PacketPlayOutUpdateSign(x, y, z, new String[] { line1, line2, line3, line4 });
        sendPacket(packet, viewer);
    }

}