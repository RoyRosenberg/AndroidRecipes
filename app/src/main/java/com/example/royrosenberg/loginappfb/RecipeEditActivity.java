package com.example.royrosenberg.loginappfb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.royrosenberg.loginappfb.DM.Recipe;
import com.example.royrosenberg.loginappfb.DM.User;
import com.example.royrosenberg.loginappfb.Services.RecipeManager;
import com.example.royrosenberg.loginappfb.Utils.MessageBox;

public class RecipeEditActivity extends AppCompatActivity {

    private Button _button;
    private EditText _etName, _etDesc, _etSteps;
    private RecipeManager _recipeMngr;
    private int MODE;//0 - add, 1 - edit
    private User _currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_edit);

        LoadControls();

        _recipeMngr = new RecipeManager(this);

        //if recipe is set -> in edit mode, else in add mode
        MODE = 0; //default: add mode
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            _currentUser = (User)bundle.get("UserObj");
            if (bundle.get("RecipeObj") != null) {
                //edit mode
                MODE = 1;
            }
        }

        if(MODE == 0){
            setTitle("Add Recipe");
        } else {
            setTitle("Edit Recipe");
        }
    }

    private void LoadControls() {
        _button = (Button)findViewById(R.id.btnSaveRecipe);
        _button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave_click(v);
            }
        });
        _etName = (EditText)findViewById(R.id.etRecipeName);
        _etDesc = (EditText)findViewById(R.id.etRecipeDescription);
        _etSteps = (EditText)findViewById(R.id.etRecipeSteps);
    }

    public void btnSave_click(View view){
        //add
        Recipe r = getRecipeFromControls();
        final MessageBox msgBx = new MessageBox(this);

        _recipeMngr.AddRecipe(r, new RecipeManager.RecipeCreationEvents() {
            @Override
            public void onSucceeded(Recipe recipe) {
                msgBx.Show("Recipe saved", "Recipe Saved", MessageBox.MessageBoxButtons.OK, new MessageBox.MessageBoxEvents() {
                    @Override
                    public void onOKClick() {
                        goBackToMainPage();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
            }

            @Override
            public void onFailed(Recipe recipe, String message) {
                msgBx.Show("Recipe did not saved\n" + message, "Failed", MessageBox.MessageBoxButtons.OK);
            }
        });

        //edit
    }

    private Recipe getRecipeFromControls(){
        Recipe recipe = new Recipe();
        recipe.Name = _etName.getText().toString();
        recipe.ShortDescription = _etDesc.getText().toString();
        recipe.Steps = _etSteps.getText().toString();
        recipe.CreatedUser = _currentUser.Email;
        return recipe;
    }

    private void goBackToMainPage(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.putExtra("UserObj", _currentUser);
        startActivity(intent);
    }
}
