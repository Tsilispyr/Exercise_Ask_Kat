package com.example.Ask.Entities;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import com.example.Ask.Entities.Gender;

@Entity
@Table
public class Animal {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private int req;
    @Column
    private int age;
    @Enumerated(EnumType.STRING)
    private Gender Gender;
    @Column
    private String type;

    @Column
    private String name;


    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return Gender;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getReq() {
        return req;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setGender(Gender gender) {
        this.Gender = gender;
    }

    public void setReq(Integer req) { this.req = req; }
    public Animal(int id, int age, Gender gender, String type, String name) {
        this.id = id;
        this.age = age;
        this.Gender = gender;
        this.type = type;
        this.name = name;
    }
    public Animal() {

    }


}
