package com.example.royrosenberg.loginappfb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Gali's Kitchen - Welcome Page");
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
