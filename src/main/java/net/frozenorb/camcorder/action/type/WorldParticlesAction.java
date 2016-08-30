package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@ToString
@EqualsAndHashCode
public final class WorldParticlesAction extends Action {

    private String particleName;
    private float x;
    private float y;
    private float z;
    private float xOffset;
    private float yOffset;
    private float zOffset;
    private float particleData;
    private int count;

    public WorldParticlesAction() {}

    public WorldParticlesAction(String particleName, Location location, Vector offset, float particleData, int count) {
        this.particleName = particleName;
        this.x = (float) location.getX();
        this.y = (float) location.getY();
        this.z = (float) location.getZ();
        this.xOffset = (float) offset.getX();
        this.yOffset = (float) offset.getY();
        this.zOffset = (float) offset.getZ();
        this.particleData = particleData;
        this.count = count;
    }

    public void read(ByteBuf in) {
        particleName = ByteBufUtils.readString(in);
        x = in.readFloat();
        y = in.readFloat();
        z = in.readFloat();
        xOffset = in.readFloat();
        yOffset = in.readFloat();
        zOffset = in.readFloat();
        particleData = in.readFloat();
        count = ByteBufUtils.readVarInt(in);
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeString(particleName, out);
        out.writeFloat(x);
        out.writeFloat(y);
        out.writeFloat(z);
        out.writeFloat(xOffset);
        out.writeFloat(yOffset);
        out.writeFloat(zOffset);
        out.writeFloat(particleData);
        ByteBufUtils.writeVarInt(count, out);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particleName, x, y, z, xOffset, yOffset, zOffset, particleData, count);
        sendPacket(packet, viewer);
    }

}