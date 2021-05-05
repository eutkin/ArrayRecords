package com.github.eutkin.codec;

public class BooleanCodec implements Codec<Boolean> {

    @Override
    public byte[] encode(Boolean arg) {
        return new byte[arg ? 1 : 0];
    }

    @Override
    public Boolean decode(byte[] arg) {
        return arg[0] == 1;
    }
}
