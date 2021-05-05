package com.github.eutkin.codec;

public class IntegerCodec implements Codec<Integer> {

    @Override
    public byte[] encode(Integer arg) {
        int value = arg;
        return new byte[]{
                (byte) ((value >> 24) & 0xff),
                (byte) ((value >> 16) & 0xff),
                (byte) ((value >> 8) & 0xff),
                (byte) ((value) & 0xff)
        };
    }

    @Override
    public Integer decode(byte[] arg) {
        return (0xff & arg[0]) << 24 |
                (0xff & arg[1]) << 16 |
                (0xff & arg[2]) << 8 |
                (0xff & arg[3]);
    }
}
