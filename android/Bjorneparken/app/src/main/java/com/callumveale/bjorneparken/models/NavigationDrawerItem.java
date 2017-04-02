package com.callumveale.bjorneparken.models;

import android.content.Context;

import com.callumveale.bjorneparken.R;

/**
 * Created by callum on 27/02/2017.
 */
public class NavigationDrawerItem {

    //region Properties

    public int icon;
    public String name;

    //endregion Properties

    //region Constructors

    public NavigationDrawerItem(String name, int iconResId) {
        this.name = name;
        this.icon = iconResId;
    }

    public NavigationDrawerItem(String name){
        this.name = name;
    }

    //endregion Constructors

    //region Methods

    public static NavigationDrawerItem[] build(Context context){

        NavigationDrawerItem[] drawerItems = new NavigationDrawerItem[11];

        drawerItems[0] = new NavigationDrawerItem(context.getString(R.string.home), R.drawable.nav_home);
        drawerItems[1] = new NavigationDrawerItem(context.getString(R.string.my_visit), R.drawable.nav_my_visit);
        drawerItems[2] = new NavigationDrawerItem(context.getString(R.string.animals), R.drawable.nav_animals);
        drawerItems[3] = new NavigationDrawerItem(context.getString(R.string.events), R.drawable.nav_events);
        drawerItems[4] = new NavigationDrawerItem(context.getString(R.string.attractions), R.drawable.nav_attractions);
        drawerItems[5] = new NavigationDrawerItem(context.getString(R.string.amenities), R.drawable.nav_amenities);
        drawerItems[6] = new NavigationDrawerItem(context.getString(R.string.park_map), R.drawable.nav_park_map);
        drawerItems[7] = new NavigationDrawerItem(context.getString(R.string.restaurant_menu), R.drawable.nav_restaurant_menu);
        drawerItems[8] = new NavigationDrawerItem(context.getString(R.string.social), R.drawable.nav_social);
        drawerItems[9] = new NavigationDrawerItem(context.getString(R.string.settings), R.drawable.nav_settings);
        drawerItems[10] = new NavigationDrawerItem(context.getString(R.string.help), R.drawable.nav_help);

        return drawerItems;
    }

    //endregion Methods
}

