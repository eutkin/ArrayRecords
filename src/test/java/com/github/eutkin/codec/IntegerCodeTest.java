package com.github.eutkin.codec;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegerCodeTest {

    private final IntegerCodec codec = new IntegerCodec();

    @ParameterizedTest
    @ValueSource(ints = {1, 255, -1, 15, Integer.MAX_VALUE, Integer.MIN_VALUE, 0}) // six numbers
    void encodeAndDecode(int expected) {
        final byte[] raw = codec.encode(expected);
        final Integer actual = this.codec.decode(raw);
        assertEquals(expected, actual);
    }
}