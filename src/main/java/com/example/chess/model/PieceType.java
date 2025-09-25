package com.example.chess.model;

public enum PieceType {
    P('p', 1), // pawn (piyon)
    A('a', 3), // knight (at)
    F('f', 3), // bishop (fil)
    K('k', 5), // rook (kale)
    V('v', 9), // queen (vezir)
    S('s', 100); // king (sah)

    public final char code;
    public final double value;

    PieceType(char code, double value) {
        this.code = code;
        this.value = value;
    }

    public static PieceType fromChar(char c) {
        for (PieceType pt : values()) {
            if (pt.code == c) return pt;
        }
        throw new IllegalArgumentException("Unknown piece code: " + c);
    }
}
