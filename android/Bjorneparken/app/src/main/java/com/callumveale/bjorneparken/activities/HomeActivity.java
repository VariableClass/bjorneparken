package com.callumveale.bjorneparken.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.callumveale.bjorneparken.R;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    public static final String API_KEY = "AIzaSyCuD2dk_XFcn512V5JxAZbFlAK9dgNlQ9c";
    public static final String ROOT_URL = "https://bjorneparkappen.appspot.com/_ah/api/";

    public static String sDefSystemLanguage;

    // Home Activity Layout
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    // Navigation Drawer and options
    private ListView mDrawerList;
    private String[] mNavigationOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set language
        sDefSystemLanguage = Locale.getDefault().getLanguage();

        // Build toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Build navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationOptions = new String[]{getString(R.string.my_visit), getString(R.string.animals),
                getString(R.string.attractions), getString(R.string.amenities), getString(R.string.park_map),
                getString(R.string.restaurant_menu), getString(R.string.settings), getString(R.string.contact)};

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Add the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // Build navigation drawer list items
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mNavigationOptions));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            selectNavigationItem(position);
        }
    }

    private void selectNavigationItem(int position){

        // Highlight the selected item
        mDrawerList.setItemChecked(position, true);

        Intent intent = null;

        switch (position){

            case 0: // If selection is 'My Visit'
                break;

            case 1: // If selection is 'Animals'
                intent = new Intent(this, ListSpeciesActivity.class);
                break;

            case 2: // If selection is 'Attractions'
                break;

            case 3: // If selection is 'Amenities'
                intent = new Intent(this, ListAmenitiesActivity.class);
                break;

            case 4: // If selection is 'Park Map'
                break;

            case 5: // If selection is 'Restaurant Menu'
                break;

            case 6: // If selection is 'Settings'
                break;

            case 7: // If selection is 'Contact'
                break;

            default:
                break;
        }

        if (intent != null){

            startActivity(intent);
        }

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_bar_item).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    /**
     * Used to change app language if phone language changed
     */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
}