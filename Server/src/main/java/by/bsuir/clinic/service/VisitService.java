package by.bsuir.clinic.service;

import by.bsuir.clinic.dao.DAO;
import by.bsuir.clinic.model.*;
import by.bsuir.clinic.util.HibernateSessionFactoryUtil;
import com.google.gson.reflect.TypeToken;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class VisitService implements DAO<Visit> {

    public Visit findById(int id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Visit visit = session.get(Visit.class,id);
        session.close();
        return visit;

    }

    public boolean save(Visit visit) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        try {
            session.save(visit);
            tx1.commit();
            session.close();
            return true;
        }
        catch (ConstraintViolationException e)
        {
            return false;
        }
    }


    public boolean update(Visit visit) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(visit);
        tx1.commit();
        session.close();
        return true;
    }


    public void delete(Visit visit) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(visit);
        tx1.commit();
        session.close();
    }


    public List<Visit> findAll() {
        List<Visit> visits;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Type itemsListType = new TypeToken<List<Visit>>() {}.getType();
        visits = session.createQuery("From Visit").list();
        session.close();
        return visits;
    }

    public List<Visit> findVisits(Doctor doctor) {
        List<Visit> visits;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Type itemsListType = new TypeToken<List<Branch>>() {}.getType();
        visits = session.createQuery("From Visit where doctor.id = '" + doctor.getId() + "'").list();
        session.close();
        return visits;
    }

    public Boolean checkDates(Visit visit) {
        List<Visit> visits;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        visits = findAll();
        for (Visit elem : visits) {
            if(visit.getDate().after(elem.getDate())) {
                long diff = visit.getDate().getTime() - elem.getDate().getTime();
                System.out.println(diff/60000);
                if(diff<15*60000) {
                    return false;
                }
            }
        }
        save(visit);
        return true;
    }

    public void cleanEmptyVisits() {
        List<Visit> visits  = findAll();
        Date currentDate = new Date();
        for(Visit visit :visits) {
            if(visit.getDate().before(currentDate) && visit.getCustomer() == null) {
                delete(visit);
            }
        }
    }

}
