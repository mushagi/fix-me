package za.co.wethinkcode.mmayibo.fixme.core.persistence;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collection;

public class RepositoryImp implements IRepository {
    private final Session session = HibernateUtil.getInstance().getSession();

    @Override
    public <T> Collection<T> getAll()  {
        final Class<T> type = null;
        Collection<T> entities = null;
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(type);

            transaction.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return entities;
    }

    @Override
    public <TEntity> TEntity getByID(String id) {
        final Class<TEntity> type = null;
        TEntity entity = null;
        try {
            Transaction transaction = session.beginTransaction();
            entity = session.get(type, id);
            transaction.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public <T> boolean update(T entity) {
        try {
            Transaction transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public <T> boolean create(T entity) {
        try {
            Transaction transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public <T> boolean delete(T entity) {
        try {
            Transaction transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public <T> boolean createAll(Collection<T> entities) {
        for (T entity: entities)
            if (!create(entity))
                return false;
        return true;
    }
}