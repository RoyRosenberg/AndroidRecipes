package com.example.royrosenberg.loginappfb.DM;

/**
 * Created by Roy.Rosenberg on 21/04/2016.
 */
public class ApplicationData {
    //public static final String FIREBASE_BASE_URL = "https://radiant-torch-1155.firebaseio.com/";
    public static final String FIREBASE_BASE_URL = "https://looksgoodandontime.firebaseio.com/";

    //For Imgur image service
    public static final String MY_IMGUR_CLIENT_ID = "77c459c520c9727";
    public static final String MY_IMGUR_CLIENT_SECRET = "00df8ea61e8c28073adf2bf0fce0f8627c214ccb";
    public static final String MY_IMGUR_REDIRECT_URL = "http://android";
    public static String getClientAuth() {
        return "Client-ID " + MY_IMGUR_CLIENT_ID;
    }

}
