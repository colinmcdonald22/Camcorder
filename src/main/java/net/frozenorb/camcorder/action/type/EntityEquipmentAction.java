package net.frozenorb.camcorder.action.type;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.playback.Playback;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@ToString
@EqualsAndHashCode
public final class EntityEquipmentAction extends Action {

    private int entityId;
    private byte slot;

    // This is our hack to not have to serialize the whole ItemStack.
    // Since we're viewing from 3rd person, this is all we need.
    private Material itemType;
    private byte itemData;
    private boolean itemHasEnchants;

    public EntityEquipmentAction() {}

    public EntityEquipmentAction(LivingEntity entity, int slot, ItemStack item) {
        this.entityId = entity.getEntityId();
        this.slot = (byte) slot;

        if (item == null || item.getType() == Material.AIR) {
            this.itemType = Material.AIR;
            this.itemData = 0;
            this.itemHasEnchants = false;
        } else {
            this.itemType = item.getType();
            this.itemData = item.getData().getData();
            this.itemHasEnchants = item.getEnchantments().size() >= 1;
        }
    }

    public void read(ByteBuf in) {
        entityId = ByteBufUtils.readVarInt(in);
        slot = in.readByte();
        itemType = Material.getMaterial(ByteBufUtils.readVarInt(in));
        itemData = in.readByte();
        itemHasEnchants = in.readBoolean();
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entityId, out);
        out.writeByte(slot);
        ByteBufUtils.writeVarInt(itemType.getId(), out);
        out.writeByte(itemData);
        out.writeBoolean(itemHasEnchants);
    }

    public void play(Playback playback, Player viewer) {
        ItemStack item = new ItemStack(itemType, 1, (short) 0, itemData);

        if (itemHasEnchants) {
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }

        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(entityId + Camcorder.ENTITY_ID_OFFSET, slot, CraftItemStack.asNMSCopy(item));
        sendPacket(packet, viewer);
    }

}