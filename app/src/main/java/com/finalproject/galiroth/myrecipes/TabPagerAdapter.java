package com.finalproject.galiroth.myrecipes;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class TabPagerAdapter extends FragmentPagerAdapter {

    int tabCount;

    public TabPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        tabCount = numberOfTabs;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabProfile tab1 = new TabProfile();
                return tab1;
            case 1:
                TabFavorites tab2 = new TabFavorites();
                return tab2;
            case 2:
                TabSearch tab3 = new TabSearch();
                return tab3;
            case 3:
                TabMyRecipes tab4 = new TabMyRecipes();
                return  tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
