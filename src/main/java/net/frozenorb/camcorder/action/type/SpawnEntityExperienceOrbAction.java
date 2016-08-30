package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityExperienceOrb;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class SpawnEntityExperienceOrbAction extends Action {

    private int entityId;
    private int x;
    private int y;
    private int z;
    private int count;

    public SpawnEntityExperienceOrbAction() {}

    public SpawnEntityExperienceOrbAction(ExperienceOrb experienceOrb) {
        this.entityId = experienceOrb.getEntityId();
        this.x = experienceOrb.getLocation().getBlockX();
        this.y = experienceOrb.getLocation().getBlockY();
        this.z = experienceOrb.getLocation().getBlockZ();
        this.count = experienceOrb.getExperience();
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        x = ByteBufUtils.readVarInt(in);
        y = ByteBufUtils.readVarInt(in);
        z = ByteBufUtils.readVarInt(in);
        count = ByteBufUtils.readVarInt(in);
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        ByteBufUtils.writeVarInt(x, out);
        ByteBufUtils.writeVarInt(y, out);
        ByteBufUtils.writeVarInt(z, out);
        ByteBufUtils.writeVarInt(count, out);
    }

    public void play(Playback playback, Player viewer) {
        PacketPlayOutSpawnEntityExperienceOrb packet = new PacketPlayOutSpawnEntityExperienceOrb();

        ReflectionUtils.setField(packet, "a", entityId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "b", MathHelper.floor(x * 32D));
        ReflectionUtils.setField(packet, "c", MathHelper.floor(y * 32D));
        ReflectionUtils.setField(packet, "d", MathHelper.floor(z * 32D));
        ReflectionUtils.setField(packet, "e", count);

        sendPacket(packet, viewer);
    }

}