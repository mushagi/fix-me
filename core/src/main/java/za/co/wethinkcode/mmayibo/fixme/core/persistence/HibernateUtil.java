package za.co.wethinkcode.mmayibo.fixme.core.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import lombok.Getter;

import java.util.logging.Level;
import java.util.logging.Logger;

class HibernateUtil {
    @Getter
    private final SessionFactory sessionFactory;

    @Getter
    private final Session session;

    public HibernateUtil(String username) {
        Logger log = Logger.getLogger("org.hibernate");
        log.setLevel(Level.OFF);
        sessionFactory = new Configuration().configure().setProperty(
                "hibernate.connection.url", "jdbc:sqlite:"+username+".db"
        ).buildSessionFactory();
        session = sessionFactory.openSession();

    }

    private void close() {
        sessionFactory.close();
    }
}