package com.example.chess.logic;

import com.example.chess.board.Board;
import com.example.chess.model.Color;
import com.example.chess.model.Piece;
import com.example.chess.model.Square;

import java.util.Set;

public class Scorer {
    private final Board board;

    public Scorer(Board board) {
        this.board = board;
    }

    public double score(Color color) {
        AttackGenerator ag = new AttackGenerator(board); // <-- we're only giving the board
        Set<Square> attackedByOpponent = ag.attackedSquares(color.opposite());

        double total = 0.0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(r, c);
                if (p == null || p.getColor() != color) continue;

                double val = p.getType().value;
                if (attackedByOpponent.contains(new Square(r, c))) {
                    val /= 2.0;
                }
                total += val;
            }
        }
        return total;
    }
}
