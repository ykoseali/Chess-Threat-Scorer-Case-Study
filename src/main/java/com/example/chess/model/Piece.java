package com.example.chess.model;

public class Piece {
    private final PieceType type;
    private final Color color;

    public Piece(PieceType type, Color color) {
        this.type = type;
        this.color = color;
    }
    public PieceType getType() { return type; }
    public Color getColor() { return color; }
}
