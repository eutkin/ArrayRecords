package com.github.eutkin.codec;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ZonedDateTimeTest {

    private final ZonedDateTimeCodec codec = new ZonedDateTimeCodec();

    @ParameterizedTest
    @MethodSource("arguments")
    void encodeAndDecode(ZonedDateTime expected) {
        final byte[] raw = this.codec.encode(expected);
        final ZonedDateTime actual = this.codec.decode(raw);
        assertEquals(expected, actual);
    }

    static Stream<ZonedDateTime> arguments() {
        return Stream.of(ZonedDateTime.now().truncatedTo(SECONDS));
    }
}