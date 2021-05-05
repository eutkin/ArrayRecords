package com.github.eutkin.codec;

import java.nio.charset.StandardCharsets;

public class StringCodec implements Codec<String> {

    @Override
    public byte[] encode(String arg) {
        return arg.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String decode(byte[] arg) {
        return new String(arg, StandardCharsets.UTF_8);
    }
}
