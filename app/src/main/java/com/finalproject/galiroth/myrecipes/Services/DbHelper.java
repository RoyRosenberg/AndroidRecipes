package com.finalproject.galiroth.myrecipes.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.finalproject.galiroth.myrecipes.DM.Recipe;

import java.util.ArrayList;

/**
 * Created by Roey on 06/05/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "myFavorites";
    private static final String TABLE_NAME = "tblMyFavorites";
    private static final String RECIPE_ID = "recipeID";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating Table for favorites
        String Create_Table = "CREATE TABLE " + TABLE_NAME + "(" + RECIPE_ID + " TEXT )";

        db.execSQL(Create_Table);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop Current Database
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create updated table
        onCreate(db);
    }

    public void saveRecipeToFavorites(Recipe recipe){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues myValues = new ContentValues();
        myValues.put(RECIPE_ID, recipe.Key);
        db.insert(TABLE_NAME, null, myValues);
        db.close();
    }

    public ArrayList<String> getAllMyFavorites() {

        ArrayList<String> myRecipes = new ArrayList<String>();

        // Get all records from table
        String selectQry = "SELECT * FROM " + TABLE_NAME;

        // DEFINE READABLE db
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor myCursor = db.rawQuery(selectQry, null);

        // Iterate throgh the List
        if (myCursor.moveToFirst()) {
            do {
                myRecipes.add(myCursor.getString(0));
            }
            while (myCursor.moveToNext());
        }
        myCursor.close();
        db.close();

        return myRecipes;
    }

}
