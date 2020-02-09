package by.bsuir.clinicFX.model;

public class Doctor extends User{
    private Branch branch;

    private String specialization;

    private String position;

    public Doctor(){
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

    @Override
    public String toString() {
        return "Doctor{" +
                "branch=" + branch +
                ", specialization='" + specialization + '\'' +
                ", position='" + position + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o.getClass()==Doctor.class)) return false;

        Doctor doctor = (Doctor) o;

        if (getBranch() != null ? !getBranch().equals(doctor.getBranch()) : doctor.getBranch() != null) return false;
        if (getSpecialization() != null ? !getSpecialization().equals(doctor.getSpecialization()) : doctor.getSpecialization() != null)
            return false;
        return getPosition() != null ? getPosition().equals(doctor.getPosition()) : doctor.getPosition() == null;
    }

    @Override
    public int hashCode() {
        int result = getBranch() != null ? getBranch().hashCode() : 0;
        result = 31 * result + (getSpecialization() != null ? getSpecialization().hashCode() : 0);
        result = 31 * result + (getPosition() != null ? getPosition().hashCode() : 0);
        return result;
    }
}

