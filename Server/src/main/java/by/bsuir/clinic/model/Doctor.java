package by.bsuir.clinic.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Table(name = "doctors")
public class Doctor extends User implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "position")
    private String position;

    public Doctor() {
    }

    public Doctor(String username, String password, String firstName, String secondName, String lastName, String specialization, String position) {
        super(username, password, firstName, secondName, lastName);
        this.specialization = specialization;
        this.position = position;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}