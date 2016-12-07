package com.pesikovlike.kalah.model.dao.postgres.hibernate;

import com.pesikovlike.kalah.model.dao.UserDAO;
import com.pesikovlike.kalah.model.entity.User;
import com.pesikovlike.kalah.model.hibernate.PostgresHibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by Igor on 01.11.2016.
 */
public class PostgresHibernateUserDAO implements UserDAO {
    public void insertUser(User user) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.save(user);
        t.commit();
        s.close();
    }

    public void updateUser(User user) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.update(user);
        t.commit();
        s.close();
    }

    public void deleteUser(User user) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.delete(user);
        t.commit();
        s.close();
    }

    public User getUserById(long id) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        User u = s.byId(User.class).load(id);
        t.commit();
        s.close();
        return u;
    }

    public User getUserByLogin(String login) {
        Session s = PostgresHibernateSessionFactory.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        Query query = s.createQuery("select u from User u where lower(u.login) LIKE lower(:LOGIN)").setParameter("LOGIN", login);
        List<User> users = query.list();
        t.commit();
        s.close();
        return users.isEmpty() ? null : users.get(0);
    }
}
