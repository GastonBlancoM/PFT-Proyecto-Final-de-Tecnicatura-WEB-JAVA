package com.repositories;

import java.util.List;

import com.exceptions.ServiciosException;


public interface Repository<T> {
    T get(long id);
    
    List<T> getAll();
    
    void save(T t) throws ServiciosException;
    
    void update(T t) throws ServiciosException;
    
    void delete(T t) throws ServiciosException;
}
