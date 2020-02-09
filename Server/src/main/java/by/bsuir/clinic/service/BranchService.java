package by.bsuir.clinic.service;

import by.bsuir.clinic.dao.DAO;
import by.bsuir.clinic.model.Branch;
import by.bsuir.clinic.util.HibernateSessionFactoryUtil;
import com.google.gson.reflect.TypeToken;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.lang.reflect.Type;
import java.util.List;

public class BranchService implements DAO<Branch> {
    public Branch findById(int id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Branch branch = session.get(Branch.class,id);
        session.close();
        return branch;
    }

    public boolean save(Branch branch) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        try {
            session.save(branch);
            tx1.commit();
            session.close();
            return true;
        }
        catch (ConstraintViolationException e)
        {
            return false;
        }
    }


    public boolean update(Branch branch) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(branch);
        tx1.commit();
        session.close();
        return true;
    }


    public void delete(Branch branch) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(branch);
        tx1.commit();
        session.close();
    }


    public List<Branch> findAll() {
        List<Branch> branches;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        branches = session.createQuery("From Branch").list();
        session.close();
        return branches;
    }

}
