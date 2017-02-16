package com.pesikovlike.kalah.ai;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AIPlayer extends Player {
    private int numHouses;
    private int minmaxDepth;

    private static final Logger LOGGER = Logger.getLogger("AIPlayer");

    public AIPlayer(int numHouses, int minmaxDepth) {
        this.numHouses = numHouses;
        this.minmaxDepth = minmaxDepth;
    }

    @Override
    public int getSowIndex(Board board) {
        LOGGER.log(Level.SEVERE, "sow index start");
        MoveInfo info = alphaBeta(board, minmaxDepth);
        LOGGER.log(Level.SEVERE, "sow index: " + info.getMoveIndex());
        return info.getMoveIndex();

    }

    private MoveInfo alphaBeta(Board origin, int maxDepth) {
        return alphaBeta(origin, maxDepth,
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
    }

    private MoveInfo alphaBeta(Board node, int depth, double alpha, double beta,
                               boolean maximizing) {
        if(node.getSide(node.getCurrentPlayer()).hasAllHousesEmpty() ||  node.isGameOver() || depth == 0) {
            //переписал
            return new MoveInfo(-1, node.getSide(this).getStore() - node.getSide(other).getStore());
        }
        if(maximizing) {
            int bestIndex = -1;
            for(int i = 0; i < numHouses; i++) {
                if(node.getSide(this).isHouseEmpty(i)) {
                    continue;
                }
                Board child = node.getBoardForMove(i);
                boolean maxNext = node.getCurrentPlayer() == child.getCurrentPlayer();
                MoveInfo info = alphaBeta(child, depth-1, alpha, beta, maxNext);
                if(info.getMoveQuality() > alpha) {
                    alpha = info.getMoveQuality();
                    bestIndex = i;
                }
                if(beta <= alpha) {
                    break;
                }
            }
            return new MoveInfo(bestIndex, alpha);
        } else {
            int bestIndex = -1;
            for(int i = 0; i < numHouses; i++) {
                if(node.getSide(other).isHouseEmpty(i)) {
                    continue;
                }
                Board child = node.getBoardForMove(i);
                boolean maxNext = node.getCurrentPlayer() != child.getCurrentPlayer();
                MoveInfo info = alphaBeta(child, depth-1, alpha, beta, maxNext);
                if(info.getMoveQuality() < beta) {
                    beta = info.getMoveQuality();
                    bestIndex = i;
                }
                if(beta <= alpha) {
                    break;
                }
            }
            return new MoveInfo(bestIndex, beta);
        }
    }
    private static class MoveInfo {
        private int moveIndex;
        private double moveQuality;

        public MoveInfo(int index, double quality) {
            moveIndex = index;
            moveQuality = quality;
        }

        public int getMoveIndex() {
            return moveIndex;
        }

        public double getMoveQuality() {
            return moveQuality;
        }
    }
}
