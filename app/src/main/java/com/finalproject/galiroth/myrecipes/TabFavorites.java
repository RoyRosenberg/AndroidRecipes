package com.finalproject.galiroth.myrecipes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.galiroth.myrecipes.DM.Recipe;
import com.finalproject.galiroth.myrecipes.R;
import com.finalproject.galiroth.myrecipes.Services.DbHelper;
import com.finalproject.galiroth.myrecipes.Services.RecipeManager;

import java.util.ArrayList;

/**
 * Created by Gali.Roth on 17/04/2016.
 */
public class TabFavorites extends Fragment {

    private DbHelper _dbHelper;
    private View _fragement;
    private RecipesAdapter _recipesAdapter;
    private RecyclerView _recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _fragement = inflater.inflate(R.layout.tab_favorites, container, false);

        _dbHelper = new DbHelper(_fragement.getContext());

        ArrayList<String> keys = _dbHelper.getAllMyFavorites();
        LoadRecipies(keys);


        return _fragement;
    }

    private void LoadRecipies(ArrayList<String> keys) {
        RecipeManager recipesMngr = new RecipeManager(getContext());

        _recyclerView = (RecyclerView) _fragement.findViewById(R.id.recycler_view);
        final ArrayList<Recipe> resultList = new ArrayList<Recipe>();

        _recipesAdapter = new RecipesAdapter(resultList, recipesMngr, null, false, false, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        _recyclerView.setLayoutManager(layoutManager);
        _recyclerView.setAdapter(_recipesAdapter);

        for (String key:keys) {
            recipesMngr.GetRecipeByKey(key, new RecipeManager.RecipeSingleResultEvents() {
                @Override
                public void onComplete(Recipe recipe) {
                    resultList.add(recipe);
                    _recipesAdapter.notifyDataSetChanged();
                }
            });
        }




    }
}
