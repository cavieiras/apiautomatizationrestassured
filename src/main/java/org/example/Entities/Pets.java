package org.example.Entities;

public class Pets {
    
    private String name;
    private String race;
    private String type;
    private String age;

    
    public Pets(String name, String race, String type, String age) {
        this.name = name;
        this.race = race;
        this.type = type;
        this.age = age;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getRace() {
        return race;
    }


    public void setRace(String race) {
        this.race = race;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getAge() {
        return age;
    }


    public void setAge(String age) {
        this.age = age;
    }
}
