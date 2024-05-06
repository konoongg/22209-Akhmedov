package org.example.connection;

public enum MessageIdE {
    CHOKE((byte) 0),
    UNCHOKE((byte) 1),
    INTERESTED((byte) 2),
    NOT_INTERESTED((byte) 3),
    HAVE((byte) 4),
    BITFIELD((byte) 5),
    REQUEST((byte) 6),
    PIECE((byte) 7),
    CANCEL((byte) 8);

    private final byte value;

    MessageIdE(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
