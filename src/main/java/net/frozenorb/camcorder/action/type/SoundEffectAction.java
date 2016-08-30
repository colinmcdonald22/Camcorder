package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedSoundEffect;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class SoundEffectAction extends Action {

    private String soundName;
    private double x;
    private double y;
    private double z;
    private float volume;
    private float pitch;

    public SoundEffectAction() {}

    public SoundEffectAction(String soundName, Location location, float volume, float pitch) {
        this.soundName = soundName;
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.volume = volume;
        this.pitch = pitch;
    }

    public void read(ByteBuf in) {
        soundName = ByteBufUtils.readString(in);
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
        volume = in.readFloat();
        pitch = in.readFloat();
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeString(soundName, out);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
        out.writeFloat(volume);
        out.writeFloat(pitch);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(soundName, x,  y, z, volume, pitch);
        sendPacket(packet, viewer);
    }

}