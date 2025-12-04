package com.schoolmgmt.repository;

import java.util.Collection;
import java.util.Optional;

public interface CrudRepository<ID, T> {
    void save(T entity);

    Optional<T> findById(ID id);

    Collection<T> findAll();

    boolean deleteById(ID id);
}
