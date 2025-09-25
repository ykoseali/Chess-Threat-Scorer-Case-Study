package com.example.chess.board;

import com.example.chess.model.Piece;
import com.example.chess.model.PieceType;
import com.example.chess.model.Color;
import com.example.chess.model.Square;

public class Board {
    // board[row][col] with row 0 = rank1, row7 = rank8
    private final Piece[][] board = new Piece[8][8];

    public void setPiece(int row, int col, Piece piece) {
        board[row][col] = piece;
    }
    public Piece getPiece(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) return null;
        return board[row][col];
    }
    public Piece getPiece(Square s) { return getPiece(s.row(), s.col()); }

    public boolean inBounds(int r, int c) { return r >=0 && r<8 && c>=0 && c<8; }
    public Square square(int row, int col) { return new Square(row,col); }
}
