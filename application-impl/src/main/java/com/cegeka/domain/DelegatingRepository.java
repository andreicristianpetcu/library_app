package com.cegeka.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class DelegatingRepository<T, ID extends Serializable>
        implements JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    @PersistenceContext
    protected EntityManager entityManager;
    private SimpleJpaRepository<T, ID> target;    

    @Override
    public long count() {
        return target.count();
    }

    @Override
    public void delete(ID id) {
        target.delete(id);
    }

    @Override
    public long count(Specification<T> s) {
        return target.count(s);
    }

    @Override
    public void delete(T t) {
        target.delete(t);
    }

    @Override
    public void delete(Iterable<? extends T> itrbl) {
        target.delete(itrbl);
    }

    @Override
    public void deleteAll() {
        target.deleteAll();
    }

    @Override
    public void deleteInBatch(Iterable<T> itrbl) {
        target.deleteInBatch(itrbl);
    }

    @Override
    public void deleteAllInBatch() {
        target.deleteAllInBatch();
    }

    @Override
    public boolean exists(ID id) {
        return target.exists(id);
    }

    @Override
    public List<T> findAll() {
        return target.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return target.findAll(sort);
    }

    @Override
    public List<T> findAll(Iterable<ID> ids) {
        return target.findAll(ids);
    }

    @Override
    public <S extends T> S save(S entity) {
        return target.save(entity);
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        return target.save(entities);
    }

    @Override
    public Page<T> findAll(Pageable pgbl) {
        return target.findAll(pgbl);
    }

    @Override
    public List<T> findAll(Specification<T> s) {
        return target.findAll(s);
    }

    @Override
    public Page<T> findAll(Specification<T> s, Pageable pgbl) {
        return target.findAll(s, pgbl);
    }

    @Override
    public List<T> findAll(Specification<T> spec, Sort sort) {
        return target.findAll(spec, sort);
    }

    @Override
    public T findOne(ID id) {
        return target.findOne(id);
    }

    @Override
    public T findOne(Specification<T> s) {
        return target.findOne(s);
    }

    @Override
    public void flush() {
        target.flush();
    }

    @PostConstruct
    void init() {
        // this is needed to retrieve the Class instance associated with the generic definition T
        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> type = (Class<T>) superclass.getActualTypeArguments()[0];
        target = new SimpleJpaRepository<T, ID>(JpaEntityInformationSupport.getMetadata(type, entityManager), entityManager);
    }

    @Override
    public T saveAndFlush(T t) {
        return target.saveAndFlush(t);
    }
}
