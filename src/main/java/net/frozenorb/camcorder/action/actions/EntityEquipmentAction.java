package net.frozenorb.camcorder.action.actions;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class EntityEquipmentAction extends Action {

    private int entityId;
    private byte slot;
    private ItemStack item;

    public EntityEquipmentAction(LivingEntity entity, Slot slot, ItemStack item) {
        this.entityId = entity.getEntityId();
        this.slot = slot.getValue();
        this.item = item;
    }

    @Override
    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        slot = in.readByte();
        item = ByteBufUtils.readSimplisticItem(in);
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeByte(slot);
        ByteBufUtils.writeSimplisticItem(item, out);
    }

    @Override
    public void play(Player viewer) {
        sendPacket(viewer, new PacketPlayOutEntityEquipment(entityId + Camcorder.ENTITY_ID_OFFSET, slot, CraftItemStack.asNMSCopy(item)));
    }

    public enum Slot {

        HAND(0),
        BOOTS(1),
        LEGGINGS(2),
        CHESTPLATE(3),
        HELMET(4);

        @Getter private byte value;

        Slot(int value) {
            this.value = (byte) value;
        }

    }

}