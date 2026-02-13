package com.schoolmgmt.repository;

import com.schoolmgmt.model.Identifiable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, T extends Identifiable<ID>> implements CrudRepository<ID, T>, Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<ID, T> store = new HashMap<>();

    @Override
    public void save(T entity) {
        store.put(entity.getId(), entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Collection<T> findAll() {
        return Collections.unmodifiableCollection(store.values());
    }

    @Override
    public boolean deleteById(ID id) {
        return store.remove(id) != null;
    }

    public boolean existsById(ID id) {
        return store.containsKey(id);
    }

    public Map<ID, T> asMapView() {
        return Collections.unmodifiableMap(store);
    }
}
