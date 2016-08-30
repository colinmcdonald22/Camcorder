package net.frozenorb.camcorder.recording;

import lombok.Getter;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.action.ActionRegistry;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.frozenorb.qlib.qLib;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.buffer.Unpooled;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public final class Recording {

    @Getter private String id = String.valueOf(qLib.RANDOM.nextInt(1000));
    private List<Action> actions = new LinkedList<>();
    @Getter private Date started = new Date();
    private int tickStarted = Camcorder.getInstance().getTick();
    private String world;
    private int minX, minZ, maxX, maxZ;

    // Purposely no access level, so it becomes package-private.
    Recording(Location a, Location b) {
        world = a.getWorld().getName();
        minX = Math.min(a.getBlockX(), b.getBlockX());
        minZ = Math.min(a.getBlockZ(), b.getBlockZ());
        maxX = Math.max(a.getBlockX(), b.getBlockX());
        maxZ = Math.max(a.getBlockZ(), b.getBlockZ());
    }

    public World getWorld() {
        return Camcorder.getInstance().getServer().getWorld(world);
    }

    private boolean contains(String world, int x, int z) {
        return minX <= x && maxX >= x && minZ <= z && maxZ >= z && world.equals(this.world);
    }

    public boolean contains(Location location) {
        return contains(location.getWorld().getName(), location.getBlockX(), location.getBlockZ());
    }

    public boolean contains(Block block) {
        return contains(block.getLocation());
    }

    public boolean contains(Entity entity) {
        return contains(entity.getLocation());
    }

    public void recordAction(Action action) {
        action.setTickOffset(Camcorder.getInstance().getTick() - tickStarted);
        actions.add(action);
    }

    public boolean write(File file) {
        try {
            OutputStream fileOutputStream = new GZIPOutputStream(new FileOutputStream(file));
            ByteBuf buffer = Unpooled.buffer();

            write(buffer);

            byte[] bytes = new byte[buffer.readableBytes()];
            buffer.getBytes(buffer.readerIndex(), bytes);

            fileOutputStream.write(bytes);
            fileOutputStream.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void write(ByteBuf out) {
        ByteBufUtils.writeVarInt(Camcorder.RECORDING_VERSION, out);
        ByteBufUtils.writeString(id, out);
        out.writeLong(started.getTime());

        ByteBufUtils.writeVarInt(actions.size(), out);

        for (Action action : actions) {
            ByteBufUtils.writeVarInt(ActionRegistry.findId(action.getClass()), out);
            ByteBufUtils.writeVarInt(action.getTickOffset(), out);
            action.write(out);
        }
    }

}