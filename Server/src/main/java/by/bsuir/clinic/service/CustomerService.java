package by.bsuir.clinic.service;

import by.bsuir.clinic.dao.DAO;
import by.bsuir.clinic.model.Customer;
import by.bsuir.clinic.util.HibernateSessionFactoryUtil;
import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class CustomerService implements DAO<Customer> {


    public Customer findById(int id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Customer customer = session.get(Customer.class,id);
        session.close();
        return customer;

    }

    public boolean save(Customer customer) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
       try {
           session.save(customer);
           tx1.commit();
           session.close();
           return true;
       }
       catch (ConstraintViolationException e)
       {
           return false;
       }
    }


    public boolean update(Customer customer) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(customer);
        tx1.commit();
        session.close();
        return true;
    }


    public void delete(Customer customer) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(customer);
        tx1.commit();
        session.close();
    }


    public List<Customer> findAll() {
        List<Customer> customers;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        customers = session.createQuery("From Customer").list();
        session.close();
        return customers;
    }


    public Customer login(String data){
        Customer customer = new Gson().fromJson(data,Customer.class);
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Customer> customerFromDb = session.createQuery("From Customer where username = '"+customer.getUsername()+"'").list();
        session.close();
        if(customerFromDb.size()==0)
            return null;
        if(customer.getUsername().equals(customerFromDb.get(0).getUsername()) && customer.getPassword().equals(customerFromDb.get(0).getPassword()))
            return customerFromDb.get(0);
        else{
            return null;
        }
    }
}
