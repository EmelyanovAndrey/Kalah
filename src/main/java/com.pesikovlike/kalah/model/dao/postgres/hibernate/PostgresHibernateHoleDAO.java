package com.pesikovlike.kalah.model.dao.postgres.hibernate;

import com.pesikovlike.kalah.model.dao.interfaces.HoleDAO;
import com.pesikovlike.kalah.model.entity.GameState;
import com.pesikovlike.kalah.model.entity.Hole;
import com.pesikovlike.kalah.model.hibernate.PostgresHibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Igor on 01.11.2016.
 */
@Stateless
public class PostgresHibernateHoleDAO implements HoleDAO {
    public void insertHole(Hole hole) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.save(hole);
        t.commit();
        s.close();
    }

    public void updateHole(Hole hole) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.update(hole);
        t.commit();
        s.close();
    }

    public void deleteHole(Hole hole) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.delete(hole);
        t.commit();
        s.close();
    }

    public Hole getHoleById(long id) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        Hole h = s.byId(Hole.class).load(id);
        t.commit();
        s.close();
        return h;
    }

    public List<Hole> getHoleByGameState(GameState gameState) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("select h from Hole h where h.gameState = :gameStateId").setParameter("gameStateId", gameState.getGameStateId());
        List<Hole> holes = query.list();
        t.commit();
        s.close();
        return holes;
    }
}
