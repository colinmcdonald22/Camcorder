package net.frozenorb.camcorder.utils;

import net.minecraft.util.io.netty.buffer.ByteBuf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// This code is taken from another private project of mine, which
// was inspired by the code on wiki.vg and in the Glowstone project.
public final class EntityMetadata {

    public static final int BYTE_ID = 0;
    public static final int SHORT_ID = 1;
    public static final int INT_ID = 2;
    public static final int FLOAT_ID = 3;
    public static final int STRING_ID = 4;

    private final Map<Integer, Object> data = new ConcurrentHashMap<>();

    public void read(ByteBuf in) {
        while (true) {
            int item = in.readUnsignedByte();

            if (item == 127) {
                break;
            }

            int index = item & 0x1F;
            int type = item >> 5;

            switch (type) {
                case BYTE_ID:
                    data.put(index, in.readByte());
                    break;
                case SHORT_ID:
                    data.put(index, in.readShort());
                    break;
                case INT_ID:
                    data.put(index, in.readInt());
                    break;
                case FLOAT_ID:
                    data.put(index, in.readFloat());
                    break;
                case STRING_ID:
                    data.put(index, ByteBufUtils.readString(in));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown metadata type: " + type);
            }
        }
    }

    public void write(ByteBuf out) {
        for (Map.Entry<Integer, Object> entry : data.entrySet()) {
            int index = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Byte) {
                out.writeByte((BYTE_ID << 5 | index & 0x1F) & 0xFF);
                out.writeByte((Byte) value);
            } else if (value instanceof Short) {
                out.writeByte((SHORT_ID << 5 | index & 0x1F) & 0xFF);
                out.writeShort((Short) value);
            } else if (value instanceof Integer) {
                out.writeByte((INT_ID << 5 | index & 0x1F) & 0xFF);
                out.writeInt((Integer) value);
            } else if (value instanceof Float) {
                out.writeByte((FLOAT_ID << 5 | index & 0x1F) & 0xFF);
                out.writeFloat((Float) value);
            } else if (value instanceof String) {
                out.writeByte((STRING_ID << 5 | index & 0x1F) & 0xFF);
                ByteBufUtils.writeString((String) value, out);
            } else {
                throw new IllegalArgumentException("Unknown metadata type: " + value);
            }
        }

        out.writeByte(127);
    }

    public byte readByte(int index) {
        return (Byte) data.get(index);
    }

    public void writeByte(int index, byte value) {
        data.put(index, value);
    }

    public short readShort(int index) {
        return (Short) data.get(index);
    }

    public void writeShort(int index, short value) {
        data.put(index, value);
    }

    public int readInt(int index) {
        return (Integer) data.get(index);
    }

    public void writeInt(int index, int value) {
        data.put(index, value);
    }

    public float readFloat(int index) {
        return (Float) data.get(index);
    }

    public void writeFloat(int index, float value) {
        data.put(index, value);
    }

    public String readString(int index) {
        return (String) data.get(index);
    }

    public void writeString(int index, String value) {
        data.put(index, value);
    }

    @Override
    public EntityMetadata clone() {
        try {
            return (EntityMetadata) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof EntityMetadata && ((EntityMetadata) other).data.equals(data);
    }

}