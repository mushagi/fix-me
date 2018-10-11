package za.co.wethinkcode.mmayibo.fixme.core.persistence;

import java.util.Collection;

public interface IRepository {
    <T> Collection<T> getAll(int id);

    <T> T getByID(String id);

    <T> boolean update(T entity);

    <T> boolean create(T entity);

    <T> boolean delete(T entity);
}