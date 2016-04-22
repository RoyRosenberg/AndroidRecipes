package com.example.royrosenberg.loginappfb.Services;

import android.content.Context;

import com.example.royrosenberg.loginappfb.DM.ApplicationData;
import com.example.royrosenberg.loginappfb.DM.Recipe;
import com.example.royrosenberg.loginappfb.DM.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Created by Roy.Rosenberg on 21/04/2016.
 */
public class RecipeManager {
    private Firebase _recipesFb;
    private final String _firebaseUrl = ApplicationData.FIREBASE_BASE_URL + "recipes";

    public RecipeManager(Context appContext) {
        Firebase.setAndroidContext(appContext);
        _recipesFb = new Firebase(_firebaseUrl);
    }

    public void AddRecipe(final Recipe recipe, final RecipeCreationEvents event) {
        if(recipe == null)
            throw new IllegalArgumentException("Invalid Recipe parameter");

        _recipesFb.push().setValue(recipe, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (event != null) {
                    if (firebaseError != null) {
                        event.onFailed(recipe, firebaseError.getMessage());
                    } else event.onSucceeded(recipe);
                }
            }
        });
    }

    public ArrayList<Recipe> GetUserRecipes(User user, final RecipeResultEvents event){
        if(user == null)
            throw new IllegalArgumentException("Invalid user parameter");
        if(event == null)
            throw new IllegalArgumentException("Invalid event parameter");

        final ArrayList<Recipe> resultList = new ArrayList<Recipe>();

        //Create query for user by email address for login
        Query queryRef = _recipesFb.orderByChild("CreatedUser").equalTo(user.Email);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Recipe r = (Recipe) messageSnapshot.getValue(Recipe.class);
                    //messageSnapshot.getRef().getParent().
                    r.Key = messageSnapshot.getKey();
                    resultList.add(r);
                }
                event.onComplete(resultList);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return resultList;
    }

    public void DeleteRecipe(Recipe recipe){
        if(recipe == null)
            throw new IllegalArgumentException("Invalid Recipe parameter");

        String rec_url = _firebaseUrl + "/" + recipe.Key;
        Firebase fb = new Firebase(rec_url);
        fb.removeValue();
    }

    public interface RecipeCreationEvents extends EventListener {
        void onSucceeded(Recipe recipe);

        void onFailed(Recipe recipe, String message);
    }

    public interface RecipeResultEvents extends EventListener {
        void onComplete(ArrayList<Recipe> result);

    }

}
