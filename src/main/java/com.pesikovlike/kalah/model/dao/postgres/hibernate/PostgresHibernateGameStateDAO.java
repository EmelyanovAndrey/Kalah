package com.pesikovlike.kalah.model.dao.postgres.hibernate;

import com.pesikovlike.kalah.game.bid.GameBid;
import com.pesikovlike.kalah.model.dao.GameStateDAO;
import com.pesikovlike.kalah.model.entity.GameState;
import com.pesikovlike.kalah.model.entity.User;
import com.pesikovlike.kalah.model.hibernate.PostgresHibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by Igor on 01.11.2016.
 */
public class PostgresHibernateGameStateDAO implements GameStateDAO {
    public void insertGameState(GameState gameState) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.save(gameState);
        t.commit();
        s.close();
    }

    public void updateGameState(GameState gameState) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.update(gameState);
        t.commit();
        s.close();
    }

    public void deleteGameState(GameState gameState) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.delete(gameState);
        t.commit();
        s.close();
    }

    public GameState getGameStateById(long id) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("select gs from GameState gs where gs.id = :ID").setParameter("ID", id);
        List<GameState> gs = query.list();
        t.commit();
        s.close();
        return gs.isEmpty() ? null : gs.get(0);

    }

    public List<GameState> getGameStatesByUser(User user) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("select gs from GameState gs where gs.user1 = :userId or gs.user2 = :userId").setParameter("userId", user);
        List<GameState> gameStates = query.list();
        t.commit();
        s.close();
        return gameStates;
    }
}
