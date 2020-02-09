package by.bsuir.clinic.server;


import by.bsuir.clinic.model.Branch;
import by.bsuir.clinic.model.Doctor;
import by.bsuir.clinic.model.Visit;
import by.bsuir.clinic.service.BranchService;
import by.bsuir.clinic.service.DoctorService;
import by.bsuir.clinic.service.VisitService;
import by.bsuir.clinic.util.HibernateSessionFactoryUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Runner {
    public static void main(String[] args)  {
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
            System.out.println(InetAddress.getByName(InetAddress.getLocalHost().getHostAddress()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
