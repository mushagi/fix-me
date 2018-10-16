package za.co.wethinkcode.mmayibo.fixme.data.persistence;

import java.util.Collection;

public interface IRepository {

    <T> Collection<T> getAll(Class<T> type);

    <T> T getByID(String id, Class<T> type);

    <T> boolean update(T entity);

    <T> T create(T entity);

    <T> boolean delete(T entity);

    <T> boolean createAll(Collection<T> markets);

    <T> Collection<T> getMultipleByIds(Class<T> type, Collection<String> ids);
}