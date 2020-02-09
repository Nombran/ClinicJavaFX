package by.bsuir.clinic.dao;

import by.bsuir.clinic.model.Customer;

import java.util.List;

public interface DAO<E> {

     E findById(int id);
     boolean save(E e);
     boolean update(E e);
     void delete(E e);
     List<E> findAll();
}
