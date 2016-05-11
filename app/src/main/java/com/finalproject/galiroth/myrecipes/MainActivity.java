package com.finalproject.galiroth.myrecipes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.finalproject.galiroth.myrecipes.DM.User;
import com.finalproject.galiroth.myrecipes.R;
import com.finalproject.galiroth.myrecipes.Services.UserManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setTitle("Gali's Kitchen - Welcome Page");

        CheckPreferences();
    }

    private void CheckPreferences() {
        //check if user should auto-loggin
        SharedPreferences preferencesObj =
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String key = preferencesObj.getString("UserKey", "");
        String email = preferencesObj.getString("UserEmail", "");
        String psw = preferencesObj.getString("UserPassword", "");

        if(key.isEmpty() || email.isEmpty() || psw.isEmpty())
            return;

        User u = new User();
        u.Email = email;
        u.Password = psw;
        u.Key = key;
        UserManager mng = new UserManager(this);

        //disable controls
        final LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);
        layout.setVisibility(View.INVISIBLE);
        setTitle("Loggin In...");
        mng.Login(u, new UserManager.LoginEvents() {
            @Override
            public void onLoginSucceeded(User userOK) {
                //go to main screen
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                intent.putExtra("UserObj", userOK);
                startActivity(intent);
            }

            @Override
            public void onLoginFailed(User u) {
                layout.setVisibility(View.VISIBLE);
            }
        });



    }

    public void register_click(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login_click(View view){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}
