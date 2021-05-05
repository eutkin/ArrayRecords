package com.github.eutkin.codec;

public interface Codec<T> {

    byte[] encode(T arg);

    T decode(byte[] arg);
}
