package com.github.eutkin.codec;

public class LongCodec implements Codec<Long> {
    @Override
    public byte[] encode(Long arg) {
        long value = arg;
        return new byte[]{
                (byte) value,
                (byte) (value >> 8),
                (byte) (value >> 16),
                (byte) (value >> 24),
                (byte) (value >> 32),
                (byte) (value >> 40),
                (byte) (value >> 48),
                (byte) (value >> 56)};
    }

    @Override
    public Long decode(byte[] arg) {
        return ((long) arg[7] << 56)
                | ((long) arg[6] & 0xff) << 48
                | ((long) arg[5] & 0xff) << 40
                | ((long) arg[4] & 0xff) << 32
                | ((long) arg[3] & 0xff) << 24
                | ((long) arg[2] & 0xff) << 16
                | ((long) arg[1] & 0xff) << 8
                | ((long) arg[0] & 0xff);
    }
}
