package com.example.chess;

import com.example.chess.board.Board;
import com.example.chess.board.Parser;
import com.example.chess.logic.Scorer;
import com.example.chess.model.Color;

public class App {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -jar chess-scorer.jar <board-file>");
            return;
        }
        try {
            Board b = Parser.parseFromFile(args[0]);
            Scorer s = new Scorer(b);
            double black = s.score(Color.BLACK);
            double white = s.score(Color.WHITE);
            System.out.printf("Siyah:%.1f Beyaz:%.1f%n", black, white);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
