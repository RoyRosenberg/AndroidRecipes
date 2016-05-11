package com.finalproject.galiroth.myrecipes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.finalproject.galiroth.myrecipes.DM.User;
import com.finalproject.galiroth.myrecipes.R;
import com.finalproject.galiroth.myrecipes.Services.UserManager;

public class SignInActivity extends AppCompatActivity {

    private UserManager _usrMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

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

    public void login_click(View view) {
        final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);

        User user = getUserFromControl();
        _usrMgr.Login(user, new UserManager.LoginEvents() {
            @Override
            public void onLoginSucceeded(User userOK) {
                //if keep me logged in is set, save in preferences
                CheckBox chBx = (CheckBox)findViewById(R.id.chbxKeepLogged);
                if(chBx.isChecked()) {
                    SharedPreferences preferencesObj =
                            PreferenceManager.getDefaultSharedPreferences(SignInActivity.this);
                    SharedPreferences.Editor editor = preferencesObj.edit();
                    editor.putString("UserKey", userOK.Key);
                    editor.putString("UserEmail", userOK.Email);
                    editor.putString("UserPassword", userOK.Password);
                    editor.commit();
                }
                //go to main screen
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                intent.putExtra("UserObj", userOK);
                startActivity(intent);
            }

            @Override
            public void onLoginFailed(User u) {
                pb.setVisibility(View.INVISIBLE);
                TextView tvNotification = (TextView) findViewById(R.id.tvNotification);
                tvNotification.setText("Login Failed, Try again");
                tvNotification.setVisibility(View.VISIBLE);
            }
        });

    }
}
