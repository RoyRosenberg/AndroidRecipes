package com.example.royrosenberg.loginappfb.DM;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by Roy.Rosenberg on 17/04/2016.
 */
public class User implements Serializable {
    public String Email;
    public String Password;
    public String FullName;
    public String Description;
    @JsonIgnore
    public String Key;

    @Override
    public String toString() {
        return FullName + " " + Email + " " + Password;
    }

    @JsonIgnore
    public boolean isValid(){
        return !Email.isEmpty() && !FullName.isEmpty() && !Password.isEmpty();
    }
}
