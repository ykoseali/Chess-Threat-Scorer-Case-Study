package com.example.chess.logic;

import com.example.chess.board.Board;
import com.example.chess.model.Color;
import com.example.chess.model.Piece;
import com.example.chess.model.PieceType;
import com.example.chess.model.Square;

import java.util.HashSet;
import java.util.Set;

public class AttackGenerator {
    private final Board board;

    public AttackGenerator(Board board) {
        this.board = board;
    }

    public Set<Square> attackedSquares(Color byColor) {
        Set<Square> attacked = new HashSet<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(r, c);
                if (p == null || p.getColor() != byColor) continue;
                attacked.addAll(attacksFromSquare(r, c, p));
            }
        }
        return attacked;
    }

    private Set<Square> attacksFromSquare(int r, int c, Piece p) {
        Set<Square> res = new HashSet<>();
        PieceType t = p.getType();

        switch (t) {
            case P: { // pawn
                int dir = (p.getColor() == Color.WHITE) ? 1 : -1;
                addIfInBounds(res, r + dir, c - 1);
                addIfInBounds(res, r + dir, c + 1);
                break;
            }
            case A: { // knight
                int[][] KNIGHT_JUMPS = {
                        {1, 2}, {2, 1}, {2, -1}, {1, -2},
                        {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}
                };
                for (int[] j : KNIGHT_JUMPS) addIfInBounds(res, r + j[0], c + j[1]);
                break;
            }
            case F: { // bishop — DO NOT include capture square
                int[][] BISHOP_DIRS = {{1,1},{1,-1},{-1,1},{-1,-1}};
                for (int[] d : BISHOP_DIRS)
                    slideAdd(res, r, c, d[0], d[1], /*includeCaptureOnBlock=*/false);
                break;
            }
            case K: { // rook — include capture square
                int[][] ROOK_DIRS = {{1,0},{-1,0},{0,1},{0,-1}};
                for (int[] d : ROOK_DIRS)
                    slideAdd(res, r, c, d[0], d[1], /*includeCaptureOnBlock=*/true);
                break;
            }
            case V: { // queen — include capture square
                int[][] QUEEN_DIRS = {
                        {1,0},{-1,0},{0,1},{0,-1},
                        {1,1},{1,-1},{-1,1},{-1,-1}
                };
                for (int[] d : QUEEN_DIRS)
                    slideAdd(res, r, c, d[0], d[1], /*includeCaptureOnBlock=*/true);
                break;
            }
            case S: { // king
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) continue;
                        addIfInBounds(res, r + dr, c + dc);
                    }
                }
                break;
            }
        }
        return res;
    }

    private void addIfInBounds(Set<Square> s, int r, int c) {
        if (r >= 0 && r < 8 && c >= 0 && c < 8) s.add(new Square(r, c));
    }

    /**
     * Slider movement generator.
     * includeCaptureOnBlock == true → ilk bloklayan taşın KAResi tehditlere eklenir (kale/vezir).
     * includeCaptureOnBlock == false → ilk bloklayan taşta durulur, karesi eklenmez (fil).
     */
    private void slideAdd(Set<Square> s, int r0, int c0, int dr, int dc, boolean includeCaptureOnBlock) {
        int r = r0 + dr, c = c0 + dc;
        while (r >= 0 && r < 8 && c >= 0 && c < 8) {
            Piece blocker = board.getPiece(r, c);
            if (blocker == null) {
                s.add(new Square(r, c)); // boş kare tehdit edilir
            } else {
                if (includeCaptureOnBlock) {
                    s.add(new Square(r, c)); // yakalama karesi eklenir
                }
                break; // ilk taşta dur
            }
            r += dr; c += dc;
        }
    }
}
