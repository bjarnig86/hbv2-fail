package is.hi.hbv501g.hbv1.Persistence.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * DaycareWorker entity. Table: "daycareworkers"
 * Relations:
 * * OneToMany with childrens table.
 * * OneToMany with dayreports table.
 * </pre>
 */
@Entity
@Table(name= "daycareworkers")
public class DaycareWorker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) // unique to prevent duplicates
    private String ssn;
    private String fullName;
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private String auth0Id;
    private int experienceInYears;
    private String address;
    private String location;
    private String locationCode;

    private final int MAXCHILDREN = 5;
    private int childrenCount = 0;

    @OneToMany(mappedBy = "daycareWorker", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Child> children = new ArrayList<>();

    @OneToMany(mappedBy = "daycareWorkerId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> waitingList = new ArrayList<>();

    @OneToMany(mappedBy = "daycareWorker", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DayReport> dayReports = new ArrayList<>();

    public DaycareWorker() {

    }
    public DaycareWorker(
            String ssn,
            String firstName,
            String lastName,
            String mobile,
            String email,
            String auth0Id,
            int experienceInYears,
            String address,
            String location,
            String locationCode
    ) {
        this.ssn = ssn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.email = email;
        this.auth0Id = auth0Id;
        this.experienceInYears = experienceInYears;
        this.address = address;
        this.location = location;
        this.locationCode = locationCode;
        this.email = email;
    }

    public String getAuth0Id() {
        return auth0Id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public int getFreeSpots() {
        return MAXCHILDREN - childrenCount;
    }

    public int getMAXCHILDREN() {
        return MAXCHILDREN;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }

    public List<DayReport> getDayReports() {
        return dayReports;
    }

    public void setDayReports(List<DayReport> dayReports) {
        this.dayReports = dayReports;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return "dcw";
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
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
    
    public void setEmail(String email) {
        this.email = email;
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

    public List<Application> getWaitingList() {
        return waitingList;
    }

    public void setWaitingList(List<Application> waitingList) {
        this.waitingList = waitingList;
    }

    public void addChildToList(Child child) {
        this.childrenCount++;
        this.children.add(child);
    }

    public void removeChildFromList(Child child) {
        this.childrenCount--;
        this.children.remove(child);
    }

    public Object getPassword() {
        return "password";
    }
}
