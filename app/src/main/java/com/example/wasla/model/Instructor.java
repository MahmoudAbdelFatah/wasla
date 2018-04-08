package com.example.wasla.model;

import java.io.Serializable;

/**
 * Created by MahmoudAbdelFatah on 23-Oct-17.
 */

public class Instructor implements Serializable {
    private String Name;
    private String Email;
    private String Gender;

    public Instructor() {

    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }
}
