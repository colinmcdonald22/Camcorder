package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.minecraft.server.v1_7_R4.PacketPlayOutExplosion;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@ToString
@EqualsAndHashCode
public final class ExplosionAction extends Action {

    private double x;
    private double y;
    private double z;
    private float radius;

    // TODO: We should look into storing player movement (probably not, as we're 3rd person) and the list of records.

    public ExplosionAction() {}

    public ExplosionAction(Location location, float radius) {
        this.x = location.getX();
        this.y = location.getY();
        this.z =  location.getZ();
        this.radius = radius;
    }

    public void read(ByteBuf in) {
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
        radius = in.readFloat();
    }

    public void write(ByteBuf out) {
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
        out.writeFloat(radius);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutExplosion packet = new PacketPlayOutExplosion(x, y, z, radius, new ArrayList<>(), null);
        sendPacket(packet, viewer);
    }

}