package is.hi.hbv501g.hbv1.Persistence.DTOs;

import is.hi.hbv501g.hbv1.Persistence.Entities.Relation;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ParentDTO {
    private String ssn;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private Relation relation;
    private String password;
    private final String ROLE = "rol_dkEGajViV24c6fD2";

    public ParentDTO(String ssn, String firstName, String lastName, String email, String mobile, Relation relation, String password) {
        this.ssn = ssn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.relation = relation;
        this.password = password;
    }

    public String getROLE() {
        return ROLE;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}