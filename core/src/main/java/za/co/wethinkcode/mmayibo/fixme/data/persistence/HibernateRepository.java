package za.co.wethinkcode.mmayibo.fixme.data.persistence;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;

public class HibernateRepository implements IRepository {
    private final Session session;

    public HibernateRepository() {
        session =  new HibernateUtil().getSession();
    }

    @Override
    public <T> Collection<T> getAll(Class<T> type)  {
        Collection<T> entities = null;
        try {

            Transaction transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteria = builder.createQuery(type);
            Root<T> root = criteria.from(type);
            criteria.select(root);
            entities = session.createQuery(criteria).getResultList();

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
    public <T> T create(T entity) {
        try {
            Transaction transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return entity;
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
            if (create(entity) == null)
                return false;
        return true;
    }

    @Override
    public <T> Collection<T> getMultipleByIds(Class<T> type, Collection<String> ids) {
        ArrayList<T> entities = new ArrayList<>();
        for (String id: ids)
            entities.add(getByID(id, type));
        return entities;
    }
}