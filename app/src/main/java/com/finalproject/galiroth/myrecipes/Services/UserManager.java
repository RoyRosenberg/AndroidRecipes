package com.finalproject.galiroth.myrecipes.Services;

import android.content.Context;

import com.finalproject.galiroth.myrecipes.DM.ApplicationData;
import com.finalproject.galiroth.myrecipes.DM.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.EventListener;

/**
 * Created by Gali.Roth on 17/04/2016.
 * UserManager for User operations such as add user, save user, login, etc.
 */
public class UserManager {

    private Firebase _userFb;
    private final String _firebaseUrl = ApplicationData.FIREBASE_BASE_URL + "users";

    public UserManager(Context appContext) {
        Firebase.setAndroidContext(appContext);
        _userFb = new Firebase(_firebaseUrl);
    }

    public void Login(final User user, final LoginEvents event) {
        //Create query for user by email address for login
        Query queryRef = _userFb.orderByChild("Email").equalTo(user.Email);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0)
                    event.onLoginFailed(user);
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    User u = (User) messageSnapshot.getValue(User.class);
                    u.Key = messageSnapshot.getKey();
                    if (u.Password.compareTo(user.Password) == 0) {
                        if (event != null)
                            event.onLoginSucceeded(u);
                    } else event.onLoginFailed(u);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void Register(final User user, final RegisterEvents event) {
        if (!user.isValid())
            throw new IllegalArgumentException("User data is not valid");
        if (event == null)
            throw new IllegalArgumentException("event parameter cannot be null");

        //check if email address exists in database before adding it
        Query queryRef = _userFb.orderByChild("Email").equalTo(user.Email);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if no users found is db, it safe to add the new user
                if (dataSnapshot.getChildrenCount() == 0) {
                    _userFb.push().setValue(user, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            user.Key = firebase.getKey();
                            if (event != null) {
                                if (firebaseError != null) {
                                    event.onRegisterFailed(user, firebaseError.getMessage());
                                } else event.onRegisternSucceeded(user);
                            }
                        }
                    });
                } else {
                    event.onRegisterFailed(user, String.format("User with email %s already exists", user.Email));
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    public void SaveProfile(final User user, final  SaveProfileEvents event) {
        if (!user.isValid())
            throw new IllegalArgumentException("User data is not valid");
        if (event == null)
            throw new IllegalArgumentException("event parameter cannot be null");

        //Create query for user by email address for login
        Query queryRef = _userFb.orderByChild("Email").equalTo(user.Email);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0)
                    return;
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    messageSnapshot.getRef().setValue(user);
                    event.onSaveFinished(user);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public interface LoginEvents extends EventListener {
        void onLoginSucceeded(User u);

        void onLoginFailed(User u);
    }

    public interface RegisterEvents extends EventListener {
        void onRegisternSucceeded(User u);

        void onRegisterFailed(User u, String errorMessage);
    }

    public interface SaveProfileEvents extends EventListener {
        void onSaveFinished(User u);

        //void onSaveFailed(User u);
    }

}
