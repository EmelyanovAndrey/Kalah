package com.pesikovlike.kalah.model.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Collection;

/**
 * Created by Igor on 31.10.2016.
 */
@Entity
@Table(name = "game_state", schema = "kalah", catalog = "kalah")
public class GameState {
    private long gameStateId;
    private Date lastSaveDate;
    private int stoneCountOfUser1;
    private int stoneCountOfUser2;
    private int initialHoleCount;
    private int initialStoneCount;
    private boolean priority;
    private User user1;
    private User user2;
    private Collection<Hole> holes;

    public GameState(){}
    public GameState(User user1, User user2, Date lastSaveDate, int stoneCountOfUser1,
                     int stoneCountOfUser2, int initialHoleCount, int initialStoneCount,
                     boolean priority) {
        gameStateId = System.currentTimeMillis();
        this.user1 = user1;
        this.user2 = user2;
        this.lastSaveDate = lastSaveDate;
        this.stoneCountOfUser1 = stoneCountOfUser1;
        this.stoneCountOfUser2 = stoneCountOfUser2;
        this.initialHoleCount = initialHoleCount;
        this.initialStoneCount = initialStoneCount;
        this.priority = priority;
    }
    public GameState(User user1, User user2, Date lastSaveDate, int stoneCountOfUser1,
                     int stoneCountOfUser2, int initialHoleCount, int initialStoneCount,
                     boolean priority, Collection<Hole> holes) {
        gameStateId = System.currentTimeMillis();
        this.user1 = user1;
        this.user2 = user2;
        this.lastSaveDate = lastSaveDate;
        this.stoneCountOfUser1 = stoneCountOfUser1;
        this.stoneCountOfUser2 = stoneCountOfUser2;
        this.initialHoleCount = initialHoleCount;
        this.initialStoneCount = initialStoneCount;
        this.priority = priority;
        this.holes = holes;
    }

    @Id
    @Column(name = "game_state_id", nullable = false)
    public long getGameStateId() {
        return gameStateId;
    }

    public void setGameStateId(long gameStateId) {
        this.gameStateId = gameStateId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_save_date", nullable = false)
    public Date getLastSaveDate() {
        return lastSaveDate;
    }

    public void setLastSaveDate(Date lastSaveDate) {
        this.lastSaveDate = lastSaveDate;
    }

    @Basic
    @Column(name = "stone_count_of_user_1", nullable = false)
    public int getStoneCountOfUser1() {
        return stoneCountOfUser1;
    }

    public void setStoneCountOfUser1(int stoneCountOfUser1) {
        this.stoneCountOfUser1 = stoneCountOfUser1;
    }

    @Basic
    @Column(name = "stone_count_of_user_2", nullable = false)
    public int getStoneCountOfUser2() {
        return stoneCountOfUser2;
    }

    public void setStoneCountOfUser2(int stoneCountOfUser2) {
        this.stoneCountOfUser2 = stoneCountOfUser2;
    }

    @Basic
    @Column(name = "initial_hole_count", nullable = false)
    public int getInitialHoleCount() {
        return initialHoleCount;
    }

    public void setInitialHoleCount(int initialHoleCount) {
        this.initialHoleCount = initialHoleCount;
    }

    @Basic
    @Column(name = "initial_stone_count", nullable = false)
    public int getInitialStoneCount() {
        return initialStoneCount;
    }

    public void setInitialStoneCount(int initialStoneCount) {
        this.initialStoneCount = initialStoneCount;
    }

    @Basic
    @Column(name = "priority", nullable = false)
    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameState that = (GameState) o;

        if (gameStateId != that.gameStateId) return false;
        if (stoneCountOfUser1 != that.stoneCountOfUser1) return false;
        if (stoneCountOfUser2 != that.stoneCountOfUser2) return false;
        if (initialHoleCount != that.initialHoleCount) return false;
        if (initialStoneCount != that.initialStoneCount) return false;
        if (priority != that.priority) return false;
        if (lastSaveDate != null ? !lastSaveDate.equals(that.lastSaveDate) : that.lastSaveDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (gameStateId ^ (gameStateId >>> 32));
        result = 31 * result + (lastSaveDate != null ? lastSaveDate.hashCode() : 0);
        result = 31 * result + stoneCountOfUser1;
        result = 31 * result + stoneCountOfUser2;
        result = 31 * result + initialHoleCount;
        result = 31 * result + initialStoneCount;
        result = 31 * result + (priority ? 1 : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "user_1_id", referencedColumnName = "user_id", nullable = false)
    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    @ManyToOne
    @JoinColumn(name = "user_2_id", referencedColumnName = "user_id", nullable = false)
    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    @OneToMany(mappedBy = "gameState")
    public Collection<Hole> getHoles() {
        return holes;
    }

    public void setHoles(Collection<Hole> holes) {
        this.holes = holes;
    }
}
