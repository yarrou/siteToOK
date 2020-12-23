package org.alex.konon.sol.siteToOK.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @Column(name = "id")
    private Long id;
    @OneToOne
    @MapsId
    private User user;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateBirthday;
    private String name;
    private String soName;
    private int weight;


    public Profile() {
    }

    public Profile(Long id){
        this.id=id;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(LocalDate dateBirthday) {
        this.dateBirthday = dateBirthday;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSoName() {
        return soName;
    }

    public void setSoName(String soName) {
        this.soName = soName;
    }
}



