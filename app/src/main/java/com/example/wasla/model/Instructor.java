package com.example.wasla.model;

import java.io.Serializable;

/**
 * Created by MahmoudAbdelFatah on 23-Oct-17.
 */

public class Instructor implements Serializable {
    private String name, email;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
}
