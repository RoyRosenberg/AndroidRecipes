package com.example.royrosenberg.loginappfb.DM;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Roy.Rosenberg on 21/04/2016.
 */
public class Recipe implements Serializable {
    public String Name;
    public String CreatedUser;
    public Date CreatedAt;
    public String ShortDescription;
    public String Steps;
    @JsonIgnore
    public String Key;

    public String ImageUrl;

    public Recipe() {
        CreatedAt = new Date();
    }
}
