package com.github.eutkin;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

public class ArrayRecords implements Records {

    public static final int DEFAULT_SIZE = 5;

    public static final int DEFAULT_FREE_SLOTS = 4;

    private static final int INITIAL_SIZE = Integer.getInteger("history.initial_size", DEFAULT_SIZE);

    private static final int FREE_SLOTS = Integer.getInteger("history.free_slots", DEFAULT_FREE_SLOTS);

    private long[] timestamps = new long[INITIAL_SIZE];

    private UUID[] ids = new UUID[INITIAL_SIZE];

    private byte[][] values = new byte[INITIAL_SIZE][];

    private int size = 0;

    public static Records create() {
        return new ArrayRecords();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void add(long timestamp, UUID id, byte[] value) {
        if (this.size + FREE_SLOTS - 2 >= this.timestamps.length) {
            final int newLength = this.timestamps.length + FREE_SLOTS;
            this.timestamps = Arrays.copyOf(this.timestamps, newLength);
            this.ids = Arrays.copyOf(this.ids, newLength);
            this.values = Arrays.copyOf(this.values, newLength);
        }

        if (timestamp >= this.timestamps[this.size]) {
            this.timestamps[this.size] = timestamp;
            this.ids[this.size] = id;
            this.values[this.size] = value;
            this.size++;
            return;
        }
        final int index = search(timestamp);
        System.arraycopy(this.timestamps, 0, this.timestamps, 0, index);
        System.arraycopy(this.timestamps, index, this.timestamps, index + 1, this.size - index);
        this.timestamps[index] = timestamp;

        System.arraycopy(this.ids, index, this.ids, index + 1, this.size - index);
        System.arraycopy(this.ids, 0, this.ids, 0, index);
        this.ids[index] = id;

        System.arraycopy(this.values, index, this.values, index + 1, this.size - index);
        values[index] = value;
        System.arraycopy(this.values, 0, this.values, 0, index);
        this.size++;
    }

    @Override
    public void truncate(long timestampFrom) {
        final int index = this.search(timestampFrom);
        this.timestamps = Arrays.copyOfRange(this.timestamps, index, this.size + FREE_SLOTS);
        this.ids = Arrays.copyOfRange(this.ids, index, this.size + FREE_SLOTS);
        this.values = Arrays.copyOfRange(this.values, index, this.size + FREE_SLOTS);
        this.size -= index;
    }

    @Override
    public void delete(UUID id) {
        final int lastIndex = this.size - 1;
        if (Objects.equals(id, this.ids[lastIndex])) {
            this.ids[lastIndex] = null;
            this.values[lastIndex] = null;
            this.timestamps[lastIndex] = 0;
            this.size--;
        }
        for (int index = lastIndex - 1; index > 0; index--) {
            if (Objects.equals(id, this.ids[index])) {
                System.arraycopy(this.timestamps, index + 1, this.timestamps, index, lastIndex - index);
                System.arraycopy(this.ids, index + 1, this.ids, index, lastIndex - index);
                System.arraycopy(this.values, index + 1, this.values, index, lastIndex - index);
                this.ids[lastIndex] = null;
                this.values[lastIndex] = null;
                this.timestamps[lastIndex] = 0;
                this.size--;
            }
        }
    }

    private int search(long key) {
        int low = 0;
        int high = this.size - 1;
        if (key >= this.timestamps[high]) {
            return high + 1;
        } else if (key <= this.timestamps[low]) {
            return low;
        }

        while (low <= high) {
            int mid = (low + high) >>> 1;
            long midValue = this.timestamps[mid];
            if (key < midValue) {
                high = mid - 1;
                if (this.timestamps[high] < key) {
                    return mid;
                }
            } else if (key > midValue) {
                low = mid + 1;
                if (this.timestamps[low] > key) {
                    return low;
                }
            } else {
                return mid;
            }
        }

        return (-low + 1);
    }


    @Override
    public String toString() {
        if (this.timestamps == null) {
            return "[]";
        }
        final var str = new StringBuilder("[");
        for (int i = 0; i < this.timestamps.length; i++) {
            str
                    .append("{")
                    .append(this.timestamps[i])
                    .append(", ")
                    .append(this.ids[i])
                    .append(", ")
                    .append(Arrays.toString(this.values[i]))
                    .append("}");
        }
        return str.append(']').toString();
    }

    @Override
    public Iterator<byte[]> iterator() {
        return new Iterator<>() {

            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return ArrayRecords.this.size != this.cursor;
            }

            @Override
            public byte[] next() {
                if (this.cursor >= size)
                    throw new NoSuchElementException();
                return ArrayRecords.this.values[this.cursor++];
            }
        };
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(this.size);
        out.writeInt(FREE_SLOTS);
        for (int i = 0; i < this.size; i++) {
            out.writeLong(this.timestamps[i]);
        }
        for (int i = 0; i < this.size; i++) {
            out.writeLong(this.ids[i].getMostSignificantBits());
            out.writeLong(this.ids[i].getLeastSignificantBits());
        }
        for (int i = 0; i < this.size; i++) {
            final byte[] value = this.values[i];
            out.writeInt(value.length);
            for (byte b : value) {
                out.writeByte(b);
            }
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        final int size = in.readInt();
        final int initialFreeSlots = in.readInt();
        this.timestamps = new long[size + initialFreeSlots];
        this.ids = new UUID[size + initialFreeSlots];
        this.values = new byte[size + initialFreeSlots][];
        for (int i = 0; i < size; i++) {
            this.timestamps[i] = in.readLong();
        }
        for (int i = 0; i < size; i++) {
            this.ids[i] = new UUID(in.readLong(), in.readLong());
        }
        for (int i = 0; i < size; i++) {
            final int valueSize = in.readInt();
            final byte[] value = new byte[valueSize];
            for (int j = 0; j < valueSize; j++) {
                value[j] = in.readByte();
            }
            this.values[i] = value;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayRecords records = (ArrayRecords) o;
        return size == records.size &&
                Arrays.equals(timestamps, records.timestamps) &&
                Arrays.equals(ids, records.ids) &&
                Arrays.equals(values, records.values);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(timestamps);
        result = 31 * result + Arrays.hashCode(ids);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }
}
