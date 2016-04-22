package com.example.royrosenberg.loginappfb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.royrosenberg.loginappfb.DM.User;
import com.example.royrosenberg.loginappfb.Utils.MessageBox;

public class WelcomeActivity extends AppCompatActivity {

    private User _currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        User user = (User)bundle.get("UserObj");
        _currentUser = user;

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        TabLayout.Tab tabProfile = tabLayout.newTab().setIcon(R.drawable.user_profile);
        tabProfile.setTag("My Profile");
        tabLayout.addTab(tabProfile);//android.R.drawable.ic_dialog_email

        TabLayout.Tab tabFavorites = tabLayout.newTab().setIcon(android.R.drawable.star_big_on);
        tabFavorites.setTag("Favorites");
        tabLayout.addTab(tabFavorites);

        TabLayout.Tab tabSearch = tabLayout.newTab().setIcon(R.drawable.search);
        tabSearch.setTag("Search Recipes");
        tabLayout.addTab(tabSearch);

        TabLayout.Tab tabMyRec = tabLayout.newTab().setIcon(R.drawable.contract);
        tabMyRec.setTag("My Recipes");
        tabLayout.addTab(tabMyRec);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final PagerAdapter pagerAdapter =  new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String title = tab.getTag().toString();
                setTitle(title);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
                Intent intent = new Intent(WelcomeActivity.this, RecipeEditActivity.class);
                intent.putExtra("UserObj", _currentUser);
                startActivity(intent);
            }
        });

        mainLayout = (RelativeLayout)findViewById(R.id.layoutView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    private RelativeLayout mainLayout;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.layoutView);
        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);

        if(item.isChecked()) item.setChecked(false);
        else item.setChecked(true);

        switch (item.getItemId()){
            case R.id.menu_profile:
                Snackbar.make(mainLayout, "Profile View", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                viewPager.setCurrentItem(0);
                //mainLayout.setBackgroundColor(Color.RED);
                return  true;
            case R.id.menu_favorites:
                Snackbar.make(mainLayout, "Favorites View", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                viewPager.setCurrentItem(1);
                //mainLayout.setBackgroundColor(Color.BLUE);
                return  true;
            case R.id.menu_search:
                Snackbar.make(mainLayout, "Search View", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                viewPager.setCurrentItem(2);
                //mainLayout.setBackgroundColor(Color.GREEN);
                return  true;
            case R.id.menu_my_recipes:
                Snackbar.make(mainLayout, "My Recipes View", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                viewPager.setCurrentItem(3);
                //mainLayout.setBackgroundColor(Color.GREEN);
                return  true;
            case R.id.menu_exit:
                Snackbar.make(mainLayout, "Exit View", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                MessageBox msgbx = new MessageBox(this);
                msgbx.Show("Are you sure?", "Confirm Exit", MessageBox.MessageBoxButtons.OK_CANCEL, new MessageBox.MessageBoxEvents() {
                    @Override
                    public void onOKClick() {
                        Snackbar.make(mainLayout, "Bye Bye", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    @Override
                    public void onCancelClick() {
                        Snackbar.make(mainLayout, "Great Choice...", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
