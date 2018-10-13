package za.co.wethinkcode.mmayibo.fixme.core.persistence;

import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.Session;
import org.hibernate.Transaction;
import za.co.wethinkcode.mmayibo.fixme.core.model.InstrumentModel;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collection;

public class RepositoryImp implements IRepository {
    private final Session session;

    public RepositoryImp(String resource) {
        session =  new HibernateUtil(resource).getSession();
    }

    @Override
    public <T> Collection<T> getAll(Class<T> type)  {
        Collection<T> entities = null;
        try {
            Transaction transaction = session.beginTransaction();
            entities = session.createCriteria(type).list();
            transaction.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return entities;
    }

    @Override
    public <TEntity> TEntity getByID(String id, Class<TEntity> type) {

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
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
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

    @Override
    public <T> Collection<T> getMultipleByIds(Class<T> type, Collection<String> ids) {
        Collection<T> entities = null;
        try {
            Transaction transaction = session.beginTransaction();
            MultiIdentifierLoadAccess<T> multiLoadAccess = session.byMultipleIds(type);
            entities = multiLoadAccess.multiLoad(ids.toArray());
            transaction.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return entities;
    }
}