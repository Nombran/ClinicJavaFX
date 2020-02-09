package by.bsuir.clinic.service;

import by.bsuir.clinic.dao.DAO;
import by.bsuir.clinic.model.Branch;
import by.bsuir.clinic.model.Customer;
import by.bsuir.clinic.model.Doctor;
import by.bsuir.clinic.model.Visit;
import by.bsuir.clinic.util.HibernateSessionFactoryUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.lang.reflect.Type;
import java.util.List;

public class DoctorService implements DAO<Doctor> {
    public Doctor findById(int id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Doctor doctor= session.get(Doctor.class,id);
        session.close();
        return doctor;

    }

    public boolean save(Doctor doctor) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        try {
            session.save(doctor);
            tx1.commit();
            session.close();
            return true;
        }
        catch (ConstraintViolationException e)
        {
            return false;
        }
    }


    public boolean update(Doctor doctor) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        try {
            session.update(doctor);
            tx1.commit();
            return true;
        }
        catch (ConstraintViolationException e)
        {
            return false;
        }
        finally {
            session.close();
        }
    }


    public void delete(Doctor doctor) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(doctor);
        tx1.commit();
        session.close();
    }


    public List<Doctor> findAll() {
        List<Doctor> doctors;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        doctors = session.createQuery("From Branch").list();
        session.close();
        return doctors;
    }

    public List<Doctor> findByBranch(Branch branch) {
        List<Doctor> doctors;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        doctors = session.createQuery("From Doctor where branch.id = '" + branch.getId() + "'").list();
        session.close();
        return doctors;
    }

    public Doctor login(Doctor doctor){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Doctor> doctorFromDb = session.createQuery("From Doctor where username = '"+doctor.getUsername()+"'").list();
        session.close();
        if(doctorFromDb.size()==0)
            return null;
        if(doctor.getUsername().equals(doctorFromDb.get(0).getUsername()) && doctor.getPassword().equals(doctorFromDb.get(0).getPassword()))
            return doctorFromDb.get(0);
        else return null;
    }
}


