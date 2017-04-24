package net.frozenorb.camcorder.utils;

import com.google.common.base.Charsets;
import lombok.experimental.UtilityClass;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.buffer.Unpooled;
import net.minecraft.util.org.apache.commons.io.IOUtils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@UtilityClass
public final class ByteBufUtils {

    // IO //

    public void writeGzipped(File file, Consumer<ByteBuf> writer) {
        try {
            OutputStream fileOutputStream = new GZIPOutputStream(new FileOutputStream(file));
            ByteBuf buffer = Unpooled.buffer();

            writer.accept(buffer);

            byte[] bytes = new byte[buffer.readableBytes()];
            buffer.getBytes(buffer.readerIndex(), bytes);

            fileOutputStream.write(bytes);
            fileOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ByteBuf readGzipped(File file) {
        try (InputStream fileInputStream = new GZIPInputStream(new FileInputStream(file))) {
            byte[] data = IOUtils.toByteArray(fileInputStream);
            return Unpooled.wrappedBuffer(data);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    // VarInt //

    public static int readVarInt(ByteBuf in) {
        int out = 0;
        int bytes = 0;
        byte bIn;

        while (true) {
            bIn = in.readByte();

            out |= (bIn & 0x7F) << (bytes++ * 7);

            // 5 is the max size of a VarInt.
            if (bytes > 5) {
                throw new RuntimeException("VarInt too big");
            }

            if ((bIn & 0x80) != 0x80) {
                break;
            }
        }

        return out;
    }

    public static void writeVarInt(int value, ByteBuf out) {
        int part;

        while (true) {
            part = value & 0x7F;
            value >>>= 7;

            if (value != 0) {
                part |= 0x80;
            }

            out.writeByte(part);

            if (value == 0) {
                break;
            }
        }
    }

    // String //

    public static String readString(ByteBuf in) {
        int len = readVarInt(in);
        byte[] b = new byte[len];
        in.readBytes(b);

        return new String(b, Charsets.UTF_8);
    }

    public static void writeString(String value, ByteBuf out) {
        byte[] b = value.getBytes(Charsets.UTF_8);

        writeVarInt(b.length, out);
        out.writeBytes(b);
    }

    // UUID //

    public static UUID readUuid(ByteBuf in) {
        long mostSig = in.readLong();
        long leastSig = in.readLong();

        return new UUID(mostSig, leastSig);
    }

    public static void writeUuid(UUID value, ByteBuf out) {
        out.writeLong(value.getMostSignificantBits());
        out.writeLong(value.getLeastSignificantBits());
    }

    // Our weird simplistic item stack //
    // we just store data, type, and has enchants

    public static ItemStack readSimplisticItem(ByteBuf in) {
        ItemStack result = new ItemStack(
            Material.getMaterial(readVarInt(in)),
            in.readByte()
        );

        if (in.readBoolean()) {
            result.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }

        return result;
    }

    public static void writeSimplisticItem(ItemStack value, ByteBuf out) {
        if (value == null) {
            value = new ItemStack(Material.AIR);
        }

        writeVarInt(value.getTypeId(), out);
        out.writeByte(value.getData().getData());
        out.writeBoolean(value.getEnchantments().size() != 0);
    }

}