package com.github.eutkin.codec;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BigDecimalCodecTest {

    private final BigDecimalCodec codec = new BigDecimalCodec();

    @ParameterizedTest
    @MethodSource("arguments")
    void encodeAndDecode(BigDecimal expected) {
        final byte[] raw = this.codec.encode(expected);
        final BigDecimal actual = this.codec.decode(raw);
        assertEquals(expected, actual);
    }

    static Stream<BigDecimal> arguments() {
        return Stream.of(
                BigDecimal.ZERO,
                new BigDecimal("255.21")
        );
    }
}