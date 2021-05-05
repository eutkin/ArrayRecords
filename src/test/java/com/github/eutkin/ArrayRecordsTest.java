package com.github.eutkin;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayRecordsTest {

    @Test
    void size() {
    }

    @Test
    void add() {
        final Records records = ArrayRecords.create();

        final var timestampNow = LocalDateTime.now().minusDays(1);

        final List<Byte> values = IntStream.range(1, 51).mapToObj(i -> (byte) i).collect(toList());
        Collections.shuffle(values, ThreadLocalRandom.current());

        byte counter = 1;
        for (byte value : values) {
            records.add(
                    timestampNow.plusMinutes(value).toEpochSecond(ZoneOffset.UTC),
                    UUID.randomUUID(),
                    new byte[]{counter++}
            );
        }

        final List<Byte> actual = StreamSupport.stream(records.spliterator(), false)
                .map(array -> array[0])
                .collect(toList());

        Collections.sort(values);

        assertEquals(values, actual);

    }

    @Test
    void truncate() {

    }

    @Test
    void delete() {
    }

    @Test
    void testToString() {
    }

    @Test
    void iterator() {
    }

    @Test
    void writeExternal() {
    }

    @Test
    void readExternal() {
    }

}