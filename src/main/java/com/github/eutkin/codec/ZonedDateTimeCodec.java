package com.github.eutkin.codec;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZonedDateTimeCodec implements Codec<ZonedDateTime> {

    @Override
    public byte[] encode(ZonedDateTime arg) {
        final String id = arg.getZone().getId();
        final byte[] zoneRaw = id.getBytes(StandardCharsets.UTF_8);
        final long seconds = arg.toEpochSecond();
        final ByteBuffer buffer = ByteBuffer.allocate(zoneRaw.length + 1 + Long.BYTES);
        return buffer.put((byte) zoneRaw.length).put(zoneRaw).putLong(seconds).rewind().array();
    }

    @Override
    public ZonedDateTime decode(byte[] arg) {
        final ByteBuffer buffer = ByteBuffer.wrap(arg);
        final int zoneSize = buffer.get();
        byte[] zone = new byte[zoneSize];
        buffer.get(zone);
        String zoneId = new String(zone, StandardCharsets.UTF_8);
        final long timestamp = buffer.getLong();

        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of(zoneId));
    }
}
