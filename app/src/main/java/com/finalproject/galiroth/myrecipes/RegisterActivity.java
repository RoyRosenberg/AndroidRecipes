package com.finalproject.galiroth.myrecipes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.finalproject.galiroth.myrecipes.DM.User;
import com.finalproject.galiroth.myrecipes.R;
import com.finalproject.galiroth.myrecipes.Services.UserManager;
import com.finalproject.galiroth.myrecipes.Utils.MessageBox;

public class RegisterActivity extends AppCompatActivity {

    private UserManager _userMngr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _userMngr = new UserManager(this);
    }

    public void register_click(View view){
        ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
        User registerUser = getUserFromControl();

        if(arePasswordsSame() == false){
            SetNotification("Password fields are not the same");
            return;
        }

        if(!registerUser.isValid()){
            SetNotification("Please fill all data");
            return;
        }
        _userMngr.Register(registerUser, new UserManager.RegisterEvents() {
            @Override
            public void onRegisternSucceeded(User u) {
                MessageBox msgBx = new MessageBox(RegisterActivity.this);
                msgBx.Show("Welcome " + u.FullName + "!\nClick OK to continue", "User Created", MessageBox.MessageBoxButtons.OK_CANCEL, new MessageBox.MessageBoxEvents() {
                    @Override
                    public void onOKClick() {
                        Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                        User registerUser = getUserFromControl();
                        intent.putExtra("UserObj", registerUser);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelClick() {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            };

            @Override
            public void onRegisterFailed(User u, String errorMessage) {
                SetNotification("Error! " + errorMessage);
            }
        });
    }

    private  void SetNotification(String message){
        TextView tvNotification = (TextView)findViewById(R.id.tvNotification);
        tvNotification.setText(message);
        tvNotification.setVisibility(View.VISIBLE);
    }

    private boolean arePasswordsSame(){
        EditText etPsw = (EditText)findViewById(R.id.etPass);
        EditText etPsw2 = (EditText)findViewById(R.id.etPass2);

        return etPsw.getText().toString().compareTo(etPsw2.getText().toString()) == 0;
    }

    private User getUserFromControl() {
        User user = new User();
        EditText etFullName = (EditText)findViewById(R.id.etFullName);
        EditText etEmail = (EditText)findViewById(R.id.etEmail);
        //EditText etUsername = (EditText)findViewById(R.id.etUserName);//unused
        EditText etPsw = (EditText)findViewById(R.id.etPass);

        user.FullName = etFullName.getText().toString();
        user.Email = etEmail.getText().toString();
        user.Password = etPsw.getText().toString();

        return user;
    }
}
