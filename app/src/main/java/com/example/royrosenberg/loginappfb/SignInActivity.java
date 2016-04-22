package com.example.royrosenberg.loginappfb;

import android.app.Service;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.royrosenberg.loginappfb.DM.User;
import com.example.royrosenberg.loginappfb.Services.UserManager;

public class SignInActivity extends AppCompatActivity {

    private UserManager _usrMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle("Gali's Kitchen - Login Page");

        _usrMgr = new UserManager(this);
    }

    private User getUserFromControl() {
        User user = new User();
        EditText edUser = (EditText) findViewById(R.id.etUserName);
        EditText edPsw = (EditText) findViewById(R.id.etPass);

        user.Email = edUser.getText().toString();
        user.Password = edPsw.getText().toString();
        return user;
    }

    public  void login_click(View view){
        final ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);

        User user = getUserFromControl();
        _usrMgr.Login(user, new UserManager.LoginEvents() {
            @Override
            public void onLoginSucceeded(User userOK) {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                intent.putExtra("UserObj", userOK);
                startActivity(intent);
            }

            @Override
            public void onLoginFailed(User u) {
                pb.setVisibility(View.INVISIBLE);
                TextView tvNotification = (TextView)findViewById(R.id.tvNotification);
                tvNotification.setText("Login Failed, Try again");
                tvNotification.setVisibility(View.VISIBLE);
            }
        });

    }
}
