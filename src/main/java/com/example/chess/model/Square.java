package com.example.chess.model;

public class Square {
    private final int row; // 0..7 where 0 = rank1, 7 = rank8
    private final int col; // 0..7 where 0 = file a, 7 = file h

    public Square(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public int row() { return row; }
    public int col() { return col; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Square)) return false;
        Square s = (Square) o;
        return s.row == row && s.col == col;
    }
    @Override
    public int hashCode() { return row*31 + col; }
    @Override
    public String toString() {
        return String.format("%c%d", 'a' + col, row + 1);
    }
}
