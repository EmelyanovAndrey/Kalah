package com.pesikovlike.kalah.ai;

public abstract class Player {
    protected Player other;

    public void setOpponent(Player other) {
        this.other = other;
    }    

    public abstract int getSowIndex(Board board);
}
