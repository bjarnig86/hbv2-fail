package is.hi.hbv501g.hbv1.Persistence.DTOs;

/**
 * Helper Object for working with request bodies for daycareworker insertions
 */

public class DaycareWorkerDTO {
    private String email;
    private String ssn;
    private String firstName;
    private String lastName;
    private String mobile;
    private int experienceInYears;
    private String address;
    private String location;
    private String locationCode;
    private String password;

    private final String ROLE = "rol_idVtgstQXiZu1OLv";

    public DaycareWorkerDTO(String email, String ssn, String firstName, String lastName, String mobile, int experienceInYears, String address, String location, String locationCode, String password) {
        this.email = email;
        this.ssn = ssn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.experienceInYears = experienceInYears;
        this.address = address;
        this.location = location;
        this.locationCode = locationCode;
        this.password = password;
    }

    public DaycareWorkerDTO() {
    }

    public String getROLE() {
        return ROLE;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getExperienceInYears() {
        return experienceInYears;
    }

    public void setExperienceInYears(int experienceInYears) {
        this.experienceInYears = experienceInYears;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
