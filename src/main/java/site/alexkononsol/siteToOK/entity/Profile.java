package site.alexkononsol.siteToOK.entity;

import site.alexkononsol.siteToOK.SiteToOkApplication;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.IOException;
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
    @Column(name = "content", columnDefinition="bytea")
    byte[] content;


    public Profile() {
        try {
            content= SiteToOkApplication.class.getClassLoader().getResourceAsStream("static/images/user-male-circle.png").readAllBytes();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("file not found");
        }
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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}



