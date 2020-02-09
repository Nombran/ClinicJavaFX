package by.bsuir.clinic.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "branches")
public class Branch implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    public Branch(String name, String description, String phoneNumber) {
        this.name = name;
        this.description = description;
        this.phoneNumber = phoneNumber;
    }

    public Branch() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
