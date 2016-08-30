package net.frozenorb.camcorder.utils;

import com.google.common.base.Charsets;
import lombok.experimental.UtilityClass;
import net.minecraft.util.io.netty.buffer.ByteBuf;

import java.util.UUID;

@UtilityClass
public final class ByteBufUtils {

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
    
}