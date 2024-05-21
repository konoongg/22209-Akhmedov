package org.example.connection.states;

public enum MessageIdE {
    CHOKE(0),
    UNCHOKE(1),
    INTERESTED(2),
    NOT_INTERESTED( 3),
    HAVE(4),
    BITFIELD(5),
    REQUEST(6),
    PIECE(7),
    CANCEL(8);

    private final int value;

    MessageIdE(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
