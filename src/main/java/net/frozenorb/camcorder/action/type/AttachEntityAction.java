package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.camcorder.utils.ReflectionUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutAttachEntity;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@ToString
@EqualsAndHashCode
public final class AttachEntityAction extends Action {

    private int entityId;
    private int vehicleId;
    private boolean leashed;

    // TODO: Verify how the packet is sent out over the wire (and NMS code) - it's weird.

    public AttachEntityAction() {}

    public AttachEntityAction(Entity entity, Entity vehicle, boolean leashed) {
        this.entityId = entity == null ? -1 : entity.getEntityId();
        this.vehicleId = vehicle.getEntityId();
        this.leashed = leashed;
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        vehicleId = ByteBufUtils.readVarInt(in);
        leashed = in.readBoolean();
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        ByteBufUtils.writeVarInt(vehicleId, out);
        out.writeBoolean(leashed);
    }

    /*
        public PacketPlayOutAttachEntity(int var1, Entity var2, Entity var3) {
        this.a = var1;
        this.b = var2.getId();
        this.c = var3 != null?var3.getId():-1;
    }

    public void a(PacketDataSerializer var1) {
        this.b = var1.readInt();
        this.c = var1.readInt();
        this.a = var1.readUnsignedByte();
    }

    public void b(PacketDataSerializer var1) {
        var1.writeInt(this.b);
        var1.writeInt(this.c);
        var1.writeByte(this.a);
    }
     */

    public void play(Playback playback, Player viewer) {
        PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity();

        ReflectionUtils.setField(packet, "a", entityId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "b", vehicleId + Camcorder.ENTITY_ID_OFFSET);
        ReflectionUtils.setField(packet, "c", leashed ? 1 : 0);

        sendPacket(packet, viewer);
    }

}