package com.pesikovlike.kalah.model.entity;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Igor on 31.10.2016.
 */
@Entity
@Table(name = "user", schema = "kalah", catalog = "kalah")
public class User {

    private long userId;
    private String login;
    private String password;
    private String email;
    private Set<GameState> gameStatesOfUser1 = new LinkedHashSet<GameState>();
    private Set<GameState> gameStatesOfUser2 = new LinkedHashSet<GameState>();
    private Avatar avatar;



    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "kalah.sequence_user", allocationSize=1)
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "login", nullable = false, length = -1)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "password", nullable = false, length = -1)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "email", nullable = true, length = -1)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

        if (userId != that.userId) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user1")
    public Set<GameState> getGameStatesOfUser1() {
        return gameStatesOfUser1;
    }

    public void setGameStatesOfUser1(Set<GameState> gameStatesOfUser1) {
        this.gameStatesOfUser1 = gameStatesOfUser1;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user2")
    public Set<GameState> getGameStatesOfUser2() {
        return gameStatesOfUser2;
    }

    public void setGameStatesOfUser2(Set<GameState> gameStatesOfUser2) {
        this.gameStatesOfUser2 = gameStatesOfUser2;
    }

    @ManyToOne
    @JoinColumn(name = "avatar_id", referencedColumnName = "avatar_id")
    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }
}
