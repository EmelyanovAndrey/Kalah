package com.pesikovlike.kalah.ai;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AI {
    private Board board;
    int holeCount;
    int stoneCount;
    Player player;
    private static final Logger LOGGER = Logger.getLogger("AI");
    public AI(int holeCount, int stoneCount, int level, boolean prior) {
        LOGGER.log(Level.SEVERE, "AI was created");
        this.holeCount = holeCount;
        this.stoneCount = stoneCount;
        board = new Board(prior, holeCount, stoneCount, level * 2);
        player = board.getCurrentPlayer();
        LOGGER.log(Level.SEVERE, "board start: " + board.getInfo());
    }

    public boolean isGameOver() {
        return board.isGameOver();
    }

    public int AIStep() {
        Player current = board.getCurrentPlayer();
        int index = current.getSowIndex(board);
        board = board.getBoardForMove(index);
        LOGGER.log(Level.SEVERE, "AI step: " + index);
        LOGGER.log(Level.SEVERE, "board AI: " + board.getInfo());
        return holeCount + 1 + index;
    }

    public void playerStep(int index) {
        board = board.getBoardForMove(index);
        LOGGER.log(Level.SEVERE, "player step: " + index);
        LOGGER.log(Level.SEVERE, "board player: " + board.getInfo());
    }
}
