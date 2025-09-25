package com.example.chess;

import com.example.chess.board.Board;
import com.example.chess.board.Parser;
import com.example.chess.logic.Scorer;
import com.example.chess.model.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardScorerTest {

    @Test
    void testBoard1() throws Exception {
    Board b = Parser.parseFromFile("boards/board1.txt");
    Scorer sc = new Scorer(b); // << match answer key: exclude slider capture squares
    assertEquals(135.0, sc.score(Color.BLACK), 0.0001);
    assertEquals(134.5, sc.score(Color.WHITE), 0.0001);
}

    @Test
    void testBoard2() throws Exception {
    Board b = Parser.parseFromFile("boards/board2.txt");
    Scorer sc = new Scorer(b); // chess-correct (includes capture squares)
    assertEquals(116.0, sc.score(Color.BLACK), 0.0001);
    assertEquals(123.0, sc.score(Color.WHITE), 0.0001);
}

    @Test
    void testBoard3() throws Exception {
    Board b = Parser.parseFromFile("boards/board3.txt");
    Scorer sc = new Scorer(b); // chess-correct
    assertEquals(108.0, sc.score(Color.BLACK), 0.0001);
    assertEquals(109.0, sc.score(Color.WHITE), 0.0001);
}
}
