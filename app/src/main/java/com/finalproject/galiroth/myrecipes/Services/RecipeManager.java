package com.finalproject.galiroth.myrecipes.Services;

import android.content.Context;

import com.finalproject.galiroth.myrecipes.DM.ApplicationData;
import com.finalproject.galiroth.myrecipes.DM.Recipe;
import com.finalproject.galiroth.myrecipes.DM.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;

/**
 * Created by Gali.Roth on 21/04/2016.
 */
public class RecipeManager {
    private Firebase _recipesFb;
    private final String _firebaseUrl = ApplicationData.FIREBASE_BASE_URL + "recipes";

    public RecipeManager(Context appContext) {
        Firebase.setAndroidContext(appContext);
        _recipesFb = new Firebase(_firebaseUrl);
    }

    public void AddRecipe(final Recipe recipe, final RecipeCreationEvents event) {
        if (recipe == null)
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

    public void SearchRecipe(String recipeName, final RecipeResultEvents event) {
        if (recipeName == null)
            throw new IllegalArgumentException("Invalid recipeName parameter");
        if (recipeName.isEmpty())
            throw new IllegalArgumentException("Invalid recipeName parameter");

        final ArrayList<Recipe> resultList = new ArrayList<Recipe>();

        //Create query for recipe search
        Query queryRef = _recipesFb.orderByChild("Name").startAt(recipeName);
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
    }

    public void GetUserRecipes(User user, final RecipeResultEvents event) {
        if (user == null)
            throw new IllegalArgumentException("Invalid user parameter");
        if (event == null)
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
    }

    public void DeleteRecipe(Recipe recipe) {
        if (recipe == null)
            throw new IllegalArgumentException("Invalid Recipe parameter");

        String rec_url = _firebaseUrl + "/" + recipe.Key;
        Firebase fb = new Firebase(rec_url);
        fb.removeValue();
    }

    public void EditRecipe(final Recipe recipe, final RecipeCreationEvents event) {
        if (recipe == null)
            throw new IllegalArgumentException("Invalid Recipe parameter");

        String rec_url = _firebaseUrl + "/" + recipe.Key;
        Firebase fb = new Firebase(rec_url);
        fb.setValue(recipe, new Firebase.CompletionListener() {
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

    public void GetRecipeByKey(String key, final RecipeSingleResultEvents event) {
        String rec_url = _firebaseUrl + "/" + key;
        Firebase fb = new Firebase(rec_url);
        final Recipe recipe = null;
        fb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Recipe recipe = (Recipe) dataSnapshot.getValue(Recipe.class);
                if (event != null)
                    event.onComplete(recipe);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public interface RecipeCreationEvents extends EventListener {
        void onSucceeded(Recipe recipe);

        void onFailed(Recipe recipe, String message);
    }

    public interface RecipeResultEvents extends EventListener {
        void onComplete(ArrayList<Recipe> result);

    }

    public interface RecipeSingleResultEvents extends EventListener {
        void onComplete(Recipe result);
    }
}
