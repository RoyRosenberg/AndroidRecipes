package com.finalproject.galiroth.myrecipes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.finalproject.galiroth.myrecipes.DM.Recipe;
import com.finalproject.galiroth.myrecipes.DM.User;
import com.finalproject.galiroth.myrecipes.R;
import com.finalproject.galiroth.myrecipes.Services.RecipeManager;
import com.finalproject.galiroth.myrecipes.Utils.MessageBox;

import java.util.ArrayList;


/**
 * Created by Gali.Roth on 17/04/2016.
 */
public class TabSearch extends Fragment {
    private RecipeManager _recipeMngr;
    private Button _btnSearch;
    private EditText _txtSearch;
    private ProgressBar _progBar;
    private View _fragement;
    private RecyclerView _recyclerView;
    private RecipesAdapter _recipesAdapter;
    private User _currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _fragement = inflater.inflate(R.layout.tab_search, container, false);

        LoadControls();
        _recipeMngr = new RecipeManager(getContext());

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        _currentUser = (User) bundle.get("UserObj");

        _btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _progBar.setVisibility(View.VISIBLE);
               String search= _txtSearch.getText().toString();
                if(search.isEmpty()){
                    MessageBox msg = new MessageBox(getContext());
                    msg.Show("Please fill search text", "Search", MessageBox.MessageBoxButtons.OK, null);
                }
                else {
                    _recipeMngr.SearchRecipe(search, new RecipeManager.RecipeResultEvents() {
                        @Override
                        public void onComplete(ArrayList<Recipe> result) {

                            InputMethodManager inputManager = (InputMethodManager)
                                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                            _progBar.setVisibility(View.GONE);
                            _recipesAdapter = new RecipesAdapter(result, _recipeMngr, _currentUser, true, false, false);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            _recyclerView.setLayoutManager(layoutManager);
                            _recyclerView.setAdapter(_recipesAdapter);


                        }
                    });
                }
            }
        });



        return _fragement;
    }

    private void LoadControls() {
        _txtSearch = (EditText)_fragement.findViewById(R.id.txtSearch);
        _btnSearch = ( Button)_fragement.findViewById(R.id.btnSearch);
        _progBar = (ProgressBar)_fragement.findViewById(R.id.progBar);
        _recyclerView = (RecyclerView) _fragement.findViewById(R.id.recycler_view);
    }
}
