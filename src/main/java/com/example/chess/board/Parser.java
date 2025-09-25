package com.example.chess.board;

import com.example.chess.model.Piece;
import com.example.chess.model.PieceType;
import com.example.chess.model.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    /**
     * The input has 8 lines, each with 8 tokens separated by spaces.
     * First line corresponds to rank 8, last line to rank 1.
     * Tokens: two characters like "pb" (pawn black), "as" (knight white), or "--" for empty.
     * Second char: 'b' = black, 's' = white (from input description).
     */
    public static Board parseFromFile(String path) throws IOException {
        Board board = new Board();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            int fileRank = 7; // start at rank8 -> row 7
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.trim().split("\\s+");
                if (tokens.length != 8) {
                    throw new IllegalArgumentException("Each line must have 8 tokens: " + line);
                }
                for (int col = 0; col < 8; col++) {
                    String tk = tokens[col];
                    if (tk.equals("--")) {
                        // empty
                    } else {
                        // e.g. "pb" or "as"
                        char pieceChar = tk.charAt(0);
                        char colorChar = tk.charAt(1);
                        PieceType pt = PieceType.fromChar(pieceChar);
                        Color color = (colorChar == 's') ? Color.BLACK : Color.WHITE;
                        // Convert fileRank (7..0) to row index: row = fileRank
                        int row = fileRank;
                        board.setPiece(row, col, new Piece(pt, color));
                    }
                }
                fileRank--;
                if (fileRank < 0) break;
            }
        }
        return board;
    }
}
