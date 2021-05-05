package com.github.eutkin.codec;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.nio.ByteBuffer;

public class BigDecimalCodec implements Codec<BigDecimal> {

    @Override
    public byte[] encode(BigDecimal arg) {
        final int scale = arg.scale();
        final int precision = arg.precision();
        final byte[] bytes = arg.unscaledValue().toByteArray();
        final ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES * 3 + bytes.length);
        return buffer.putInt(scale).putInt(precision).putInt(bytes.length).put(bytes).rewind().array();
    }

    @Override
    public BigDecimal decode(byte[] arg) {
        final ByteBuffer buffer = ByteBuffer.wrap(arg);
        final int scale = buffer.getInt();
        final int precision = buffer.getInt();
        final int size = buffer.getInt();
        byte[] bytes = new byte[size];
        buffer.get(bytes, 0, size);
        final BigInteger number = new BigInteger(bytes, 0, size);
        return new BigDecimal(number, scale, new MathContext(precision));
    }
}
