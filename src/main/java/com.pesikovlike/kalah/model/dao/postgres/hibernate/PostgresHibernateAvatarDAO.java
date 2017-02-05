package com.pesikovlike.kalah.model.dao.postgres.hibernate;

import com.pesikovlike.kalah.model.dao.AvatarDAO;
import com.pesikovlike.kalah.model.entity.Avatar;
import com.pesikovlike.kalah.model.entity.Hole;
import com.pesikovlike.kalah.model.entity.User;
import com.pesikovlike.kalah.model.hibernate.PostgresHibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by Igor on 01.11.2016.
 */
public class PostgresHibernateAvatarDAO implements AvatarDAO {
    public void insertAvatar(Avatar avatar) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.save(avatar);
        t.commit();
        s.close();
    }

    public void updateAvatar(Avatar avatar) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.update(avatar);
        t.commit();
        s.close();
    }

    public void deleteAvatar(Avatar avatar) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.delete(avatar);
        t.commit();
        s.close();
    }

    public Avatar getAvatarById(long id) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("select a from Avatar a where a.id = :ID").setParameter("ID", id);
        List<Avatar> avatars = query.list();
        t.commit();
        s.close();
        return avatars.isEmpty() ? null : avatars.get(0);
    }

    public List<Avatar> getAvatars() {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("from Avatar a");
        List<Avatar> avatars = query.list();
        t.commit();
        s.close();
        return avatars;
    }
}
