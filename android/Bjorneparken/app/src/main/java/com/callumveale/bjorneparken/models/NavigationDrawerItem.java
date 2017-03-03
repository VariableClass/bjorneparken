package com.callumveale.bjorneparken.models;

/**
 * Created by callum on 27/02/2017.
 */
public class NavigationDrawerItem {

    //region Properties

    public int icon;
    public String name;

    //endregion Properties

    //region Constructors

    public NavigationDrawerItem(String name)
    {
        this.name = name;
    }

    //endregion Constructors

    //region Methods

    public static NavigationDrawerItem[] build(String[] options){

        NavigationDrawerItem[] drawerItems = new NavigationDrawerItem[options.length];

        for (int i = 0; i < options.length; i++){

            drawerItems[i] = new NavigationDrawerItem(options[i]);
        }

        return drawerItems;
    }

    //endregion Methods
}

