package za.co.wethinkcode.mmayibo.fixme.core.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import lombok.Getter;

public class HibernateUtil {
    private static final HibernateUtil ourInstance = new HibernateUtil();
    @Getter
    private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Getter
    private Session session = sessionFactory.openSession();

    public static HibernateUtil getInstance() {
        return ourInstance;
    }

    private void close() {
        sessionFactory.close();
    }
}