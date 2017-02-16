package com.pesikovlike.kalah.ai;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Board {
    private int numHouses;
    private int stonesCount;
    private int allStonesCount;
    private Side p1side, p2side;
    private Player p1, p2;
    private Player current;
    private static final Logger LOGGER = Logger.getLogger("Board");
    public Board(boolean prior, int numHouses, int stonesCount, int minmaxDepth) {
        this.stonesCount = stonesCount;
        this.numHouses = numHouses;
        p1side = new Side(numHouses, stonesCount);
        p2side = new Side(numHouses, stonesCount);
        this.p1 = new EmptyPlayer();
        this.p2 = new AIPlayer(numHouses, minmaxDepth);
        p1.setOpponent(p2);
        p2.setOpponent(p1);
        current = prior ? p1 : p2;
        allStonesCount = numHouses * stonesCount * 2;
    }

    private Board(Board board) {
        this.allStonesCount = board.allStonesCount;
        this.numHouses = board.numHouses;
        this.p1side = new Side(board.p1side);
        this.p2side = new Side(board.p2side);
        this.p1 = board.p1;
        this.p2 = board.p2;
        this.current = board.current;
    }

    public Player getCurrentPlayer() {
        return current;
    }

    public String getInfo() {
        String res = "";

        for (int i = 0; i < numHouses; i++) {
            res += p1side.getHouse(i) + " ";
        }
        res += ":" + p1side.getStore() + ": ";
        for (int i = 0; i < numHouses; i++) {
            res += p2side.getHouse(i) + " ";
        }
        res += ":" + p2side.getStore() + ":";
        return res;
    }

    public Board getBoardForMove(int index) {

        Board board = new Board(this);
        Side ownSide = current == p1 ? board.p1side : board.p2side;
        Side otherSide = current == p1 ? board.p2side : board.p1side;

        int numToSow = ownSide.getHouse(index);
        ownSide.zeroHouse(index);

        if (numToSow == 0) {
            throw new RuntimeException("You can't sow from an empty house");
        }

        for (int i = index + 1; i < numHouses; i++) {
            numToSow--;
            ownSide.incHouse(i);
            if (numToSow == 0) {
                board.switchPlayers();
                return board;
            }
        }

        while (numToSow != 0) {
            ownSide.incStore();
            numToSow--;

            if (numToSow == 0) {
                board.switchPlayers();
                return board;
            }

            for (int i = 0; i < numHouses; i++) {
                numToSow--;
                otherSide.incHouse(i);
                if (numToSow == 0) {
                    if (otherSide.getHouse(i) == 2 || otherSide.getHouse(i) == 3) {
                        while (i >= 0 && (otherSide.getHouse(i) == 2 || otherSide.getHouse(i) == 3)) {
                            ownSide.addStore(otherSide.getHouse(i));
                            otherSide.zeroHouse(i);
                            i--;
                        }
                    }
                    board.switchPlayers();
                    return board;

                }
            }
            for (int i = 0; i < numHouses; i++) {
                numToSow--;
                ownSide.incHouse(i);
                if (numToSow == 0) {
                    if (ownSide.getHouse(i) != 1) {
                        numToSow = ownSide.getHouse(i);
                        ownSide.zeroHouse(i);
                        if (i + numToSow - 1 < numHouses) {
                            for (i++; i < numHouses; i++) {
                                numToSow--;
                                ownSide.incHouse(i);
                                if (numToSow == 0) {
                                    board.switchPlayers();
                                    return board;
                                }
                            }
                        }
                    } else {
                        board.switchPlayers();
                        return board;
                    }
                }
            }
        }

        throw new RuntimeException("Something's gone horribly, horribly wrong.");
    }

    public Side getSide(Player player) {
        if (player == p1) {
            return p1side;
        } else {
            return p2side;
        }
    }

    public Side getSide(int p) {
        if (p == 1) {
            return p1side;
        } else {
            return p2side;
        }
    }

    public boolean isGameOver() {
        if (p1side.getStore() > allStonesCount / 2 || p2side.getStore() > allStonesCount / 2)
            return true;
        return false;
    }

    public void finalize() {
        if (!isGameOver()) return;
        p1side.moveAllHousesToStore();
        p2side.moveAllHousesToStore();
    }

    private void switchPlayers() {
        current = current == p1 ? p2 : p1;
    }

    public static class Side {
        private int stones;
        private int numHouses;

        private int[] houses;
        private int store;

        public Side(int numHouses, int stonesCount) {
            this.stones = stonesCount;
            this.numHouses = numHouses;
            houses = new int[numHouses];
            for (int i = 0; i < numHouses; i++) {
                houses[i] = stones;
            }
            store = 0;
        }

        public Side(Side side) {
            this.stones = side.stones;
            this.numHouses = side.numHouses;
            houses = new int[numHouses];
            for (int i = 0; i < numHouses; i++) {
                houses[i] = side.houses[i];
            }
            store = side.store;
        }

        public boolean hasAllHousesEmpty() {
            for (int house : houses) {
                if (house != 0) {
                    return false;
                }
            }
            return true;
        }

        public void setStore(int numSeeds) {
            store = numSeeds;
        }

        public void addStore(int numSeeds) {
            store += numSeeds;
        }

        public void incStore() {
            store++;
        }

        public void setHouse(int index, int numSeeds) {
            houses[index] = numSeeds;
        }

        public void incHouse(int index) {
            setHouse(index, getHouse(index) + 1);
        }

        public void zeroHouse(int index) {
            setHouse(index, 0);
        }

        public int getStore() {
            return store;
        }

        public int getHouse(int index) {
            return houses[index];
        }

        public boolean isHouseEmpty(int index) {
            return getHouse(index) == 0;
        }

        public void moveAllHousesToStore() {
            for (int i = 0; i < numHouses; i++) {
                store += houses[i];
                houses[i] = 0;
            }
        }
    }
}
