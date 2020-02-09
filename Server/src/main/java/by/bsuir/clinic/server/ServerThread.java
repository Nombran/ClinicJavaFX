package by.bsuir.clinic.server;

import by.bsuir.clinic.adapter.HibernateProxyTypeAdapter;
import by.bsuir.clinic.controller.StartMenuController;
import by.bsuir.clinic.model.Branch;
import by.bsuir.clinic.model.Customer;
import by.bsuir.clinic.model.Doctor;
import by.bsuir.clinic.model.Visit;
import by.bsuir.clinic.service.BranchService;
import by.bsuir.clinic.service.CustomerService;
import by.bsuir.clinic.service.DoctorService;
import by.bsuir.clinic.service.VisitService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.print.Doc;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerThread  extends Thread  {

    private PrintStream os;
    private BufferedReader is;
    private InetAddress addr;

    public ServerThread(Socket s) throws IOException {
        os = new PrintStream(s.getOutputStream());
        is = new BufferedReader(new InputStreamReader(s.getInputStream()));
        addr = s.getInetAddress();
    }

    @Override
    public void run() {
        String gson;
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson json = builder.create();
        BranchService branchService = new BranchService();
        CustomerService customerService = new CustomerService();
        DoctorService doctorService = new DoctorService();
        VisitService visitService = new VisitService();
        try {
            while ((gson = is.readLine()) != null) {
                visitService.cleanEmptyVisits();
                Packet packet = new Gson().fromJson(gson, Packet.class);
                String command = packet.getCommand();
                String data = packet.getData();
                StartMenuController.getInstance().addLog(addr.getHostName() + ":" + command);
                switch (command) {
                    case "login":if(customerService.login(data)==null) {
                        packet.setCommand("User not found");
                    }
                    else {
                        packet.setData(json.toJson(customerService.login(data)));
                        packet.setCommand("successful");
                    }
                        os.println(json.toJson(packet));
                     break;
                    case "loginDoctor":if(doctorService.login(json.fromJson(data,Doctor.class))==null) {
                        packet.setCommand("User not found");
                        os.println(json.toJson(packet));
                    }
                    else {
                        Doctor doctor = doctorService.login(json.fromJson(data,Doctor.class));
                        if(doctor.isAdmin()) {
                            packet.setCommand("Admin");
                        }
                        else {
                            packet.setData(json.toJson(doctorService.login(json.fromJson(data, Doctor.class))));
                            packet.setCommand("successful");
                        }
                        os.println(new Gson().toJson(packet));
                    }
                        break;
                    case "registration": packet.setData(new Gson().toJson(customerService.save(new Gson().fromJson(data, Customer.class))));
                     os.println(new Gson().toJson(packet));
                     break;
                    case "addBranch":packet.setData(new Gson().toJson(branchService.save(new Gson().fromJson(data, Branch.class))));
                     os.println(new Gson().toJson(packet));
                    break;
                    case "getBranchList":packet.setData(new Gson().toJson(branchService.findAll()));
                        os.println(new Gson().toJson(packet));
                        break;
                    case "getDoctors":packet.setData(json.toJson(doctorService.findByBranch(json.fromJson(packet.getData(),Branch.class))));
                        os.println(json.toJson(packet));
                        break;
                    case "updateBranch":branchService.update(json.fromJson(packet.getData(),Branch.class));
                        os.println("successful");
                        break;
                    case "deleteBranch":List<Doctor> doctors;
                         Branch branch = json.fromJson(packet.getData(),Branch.class);
                         doctors = doctorService.findByBranch(branch);
                         List<Visit> visitsToDelete;
                        for(Doctor doctor:doctors){
                            if(doctor.getBranch().getId()==branch.getId()) {
                                visitsToDelete = visitService.findVisits(doctor);
                                for(Visit visitForDoctor : visitsToDelete) {
                                    visitService.delete(visitForDoctor);
                                }
                                doctorService.delete(doctor);
                            }
                        }
                        branchService.delete(branch);
                        os.println("successful");
                        break;
                    case "addDoctor":Doctor doctor = json.fromJson(packet.getData(),Doctor.class);
                    packet.setData(json.toJson(doctorService.save(doctor)));
                    os.println(json.toJson(packet));
                    break;
                    case "updateDoctor" :packet.setData(json.toJson(doctorService.update(json.fromJson(packet.getData(),Doctor.class))));
                    os.println(json.toJson(packet));
                    break;
                    case "deleteDoctor": List<Visit> doctorVisits = visitService.findVisits(json.fromJson(packet.getData(),Doctor.class));
                    for(Visit elem : doctorVisits) {
                        visitService.delete(elem);
                    }
                        doctorService.delete(json.fromJson(packet.getData(),Doctor.class));
                    os.println("successful");
                    break;
                    case "getVisits" : List<Visit> visits = visitService.findAll();
                    packet.setData(json.toJson(visits));
                    os.println(json.toJson(packet));
                    break;
                    case "addVisit" : Visit visit = json.fromJson(data,Visit.class);
                    packet.setData(json.toJson(visitService.checkDates(visit)));
                    os.println(json.toJson(packet));
                    break;
                    case "deleteVisit" : Visit deletedVisit = json.fromJson(data,Visit.class);
                    visitService.delete(deletedVisit);
                    os.println("successful");
                    break;
                    case "updateVisit" : Visit updateVisit = json.fromJson(data,Visit.class);
                    visitService.update(updateVisit);
                    os.println("successful");
                    default:break;
                }
            }
        } catch (IOException e) {
            System.out.println("Disconnect");
        } finally {
            disconnect();
        }
    }


    public void disconnect() {
        try {
            System.out.println(addr.getHostName() + " disconnected");
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.interrupt();
        }
    }
}
