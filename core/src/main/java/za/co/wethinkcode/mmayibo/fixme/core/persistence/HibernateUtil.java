package za.co.wethinkcode.mmayibo.fixme.core.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import lombok.Getter;

public class HibernateUtil {
    @Getter
    private final SessionFactory sessionFactory;

    @Getter
    private Session session;

    public HibernateUtil() {
        sessionFactory = new Configuration().configure().buildSessionFactory();;
        session = sessionFactory.openSession();
    }

    private void close() {
        sessionFactory.close();
    }
}