package net.frozenorb.camcorder.action.actions;

import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class EntityDestroyAction extends Action {

    private int[] entities;

    public EntityDestroyAction(Entity... destroyed) {
        entities = new int[destroyed.length];

        for (int i = 0; i < destroyed.length; i++) {
            entities[i] = destroyed[i].getEntityId();
        }
    }

    @Override
    public void read(ByteBuf in) {
        entities = new int[ByteBufUtils.readVarInt(in)];

        for (int i = 0; i < entities.length; i++) {
            entities[i] = ByteBufUtils.readVarInt(in);
        }
    }

    @Override
    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(entities.length, out);

        for (int val : entities) {
            ByteBufUtils.writeVarInt(val, out);
        }
    }

    @Override
    public void play(Player viewer) {
        int[] entitiesCopy = entities.clone();

        for (int i = 0; i < entitiesCopy.length; i++) {
            entitiesCopy[i] += Camcorder.ENTITY_ID_OFFSET;
        }

        sendPacket(viewer, new PacketPlayOutEntityDestroy(entitiesCopy));
    }

}