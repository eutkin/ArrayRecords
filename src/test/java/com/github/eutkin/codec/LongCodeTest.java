package com.github.eutkin.codec;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongCodeTest {

    private final LongCodec codec = new LongCodec();
    
    @ParameterizedTest
    @ValueSource(longs = {1, 255, -1, 15, Long.MAX_VALUE, Long.MIN_VALUE, 0}) // six numbers
    void encodeAndDecode(long expected) {
        final byte[] raw = codec.encode(expected);
        final long actual = this.codec.decode(raw);
        assertEquals(expected, actual);
    }
}