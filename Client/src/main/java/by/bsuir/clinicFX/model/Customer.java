package by.bsuir.clinicFX.model;


import java.io.Serializable;
import java.time.LocalDate;


public class Customer extends User implements Serializable{
    private LocalDate birthday;

    private String socialStatus;

    private String phoneNumber;

    public Customer() {
        super();
    }

    public Customer(String username, String password, String firstName,String secondName, String lastName, LocalDate birthday, String socialStatus, String phoneNumber)  {
        super(username,password,firstName,secondName,lastName);
        this.birthday = birthday;
        this.socialStatus = socialStatus;
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getSocialStatus() {
        return socialStatus;
    }

    public void setSocialStatus(String socialStatus) {
        this.socialStatus = socialStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
