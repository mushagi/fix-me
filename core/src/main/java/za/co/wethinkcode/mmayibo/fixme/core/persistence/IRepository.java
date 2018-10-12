package za.co.wethinkcode.mmayibo.fixme.core.persistence;

import za.co.wethinkcode.mmayibo.fixme.core.model.MarketModel;

import java.util.Collection;

public interface IRepository {
    <T> Collection<T> getAll();

    <T> T getByID(String id);

    <T> boolean update(T entity);

    <T> boolean create(T entity);

    <T> boolean delete(T entity);

    <T> boolean createAll(Collection<T> markets);
}