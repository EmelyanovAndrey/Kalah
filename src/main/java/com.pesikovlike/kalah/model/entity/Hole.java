package com.pesikovlike.kalah.model.entity;

import javax.persistence.*;

/**
 * Created by Igor on 31.10.2016.
 */
@Entity
@Table(name = "hole", schema = "kalah", catalog = "kalah")
public class Hole {
    private long holeId;
    private int number;
    private int stoneCount;
    private GameState gameState;

    public Hole(){}
    public Hole(int number, int stoneCount){
        this.holeId = System.currentTimeMillis();
        this.number = number;
        this.stoneCount = stoneCount;
    }

    @Id
    @Column(name = "hole_id", nullable = false)
    public long getHoleId() {
        return holeId;
    }

    public void setHoleId(long holeId) {
        this.holeId = holeId;
    }

    @Basic
    @Column(name = "number", nullable = false)
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Basic
    @Column(name = "stone_count", nullable = false)
    public int getStoneCount() {
        return stoneCount;
    }

    public void setStoneCount(int stoneCount) {
        this.stoneCount = stoneCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hole that = (Hole) o;

        if (holeId != that.holeId) return false;
        if (number != that.number) return false;
        if (stoneCount != that.stoneCount) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (holeId ^ (holeId >>> 32));
        result = 31 * result + number;
        result = 31 * result + stoneCount;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "game_state_id", referencedColumnName = "game_state_id", nullable = false)
    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
