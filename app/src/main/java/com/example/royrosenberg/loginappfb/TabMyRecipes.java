package com.example.royrosenberg.loginappfb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.royrosenberg.loginappfb.DM.Recipe;
import com.example.royrosenberg.loginappfb.DM.User;
import com.example.royrosenberg.loginappfb.Services.RecipeManager;
import com.example.royrosenberg.loginappfb.Utils.MessageBox;

import java.util.ArrayList;

/**
 * Created by Roy.Rosenberg on 21/04/2016.
 */
public class TabMyRecipes extends Fragment {

    private RecipeManager _recipeMngr;
    private User _currentUser;
    private RecyclerView _recyclerView;
    private View _fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        _fragmentView = inflater.inflate(R.layout.tab_my_recipes, container, false);

        _recipeMngr = new RecipeManager(getContext());

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        _currentUser = (User) bundle.get("UserObj");

        _recyclerView = (RecyclerView) _fragmentView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        _recyclerView.setLayoutManager(layoutManager);

        _recipeMngr.GetUserRecipes(_currentUser, new RecipeManager.RecipeResultEvents() {
            @Override
            public void onComplete(ArrayList<Recipe> result) {
                //MessageBox msg = new MessageBox(getContext());
                //msg.Show("Found " + result.size() + " recipes!", "result!", MessageBox.MessageBoxButtons.OK);
                RecipesAdapter adapter = new RecipesAdapter(result, _recipeMngr);
                _recyclerView.setAdapter(adapter);
            }
        });

        return _fragmentView;
    }
}
