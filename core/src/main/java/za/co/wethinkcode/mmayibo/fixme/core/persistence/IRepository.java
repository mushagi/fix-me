package za.co.wethinkcode.mmayibo.fixme.core.persistence;

import java.util.Collection;

public interface IRepository {

    <T> Collection<T> getAll(Class<T> type);

    <T> T getByID(String id, Class<T> type);

    <T> void update(T entity);

    <T> T create(T entity);

    <T> boolean delete(T entity);

    <T> void createAll(Collection<T> markets);

    <T> Collection<T> getMultipleByIds(Class<T> type, Collection<String> ids);
}