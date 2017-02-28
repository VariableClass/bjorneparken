package com.callumveale.bjorneparken.activities;

import android.content.Context;
import android.content.res.Configuration;

import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.callumveale.bjorneparken.adapters.NavigationDrawerAdapter;
import com.callumveale.bjorneparken.fragments.ListFragment;
import com.callumveale.bjorneparken.models.NavigationDrawerItem;
import com.callumveale.bjorneparken.models.Species;
import com.callumveale.bjorneparken.models.Visitor;
import com.callumveale.bjorneparken.requests.GetAllAmenities;
import com.callumveale.bjorneparken.requests.GetAllSpecies;
import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.requests.GetVisitorId;
import com.callumveale.bjorneparken.requests.GetVisitorItinerary;
import com.callumveale.bjorneparken.requests.RequestsModule;
import com.google.api.client.util.DateTime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements ListFragment.OnListFragmentInteractionListener, NavigationDrawerAdapter.INavigationDrawerListener {

    // Toolbar
    private Toolbar mToolbar;

    // Home Activity Layout
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    // Navigation Drawer and options
    private RecyclerView mDrawerList;
    private NavigationDrawerItem[] mNavigationOptions;

    // Progress Wheel
    private ProgressBar mProgressBar;

    // Visitor Information
    private Visitor mVisitor;
    private DateTime mVisitStart;
    private DateTime mVisitEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Build and display UI components
        buildUI();

        mVisitStart = new DateTime(new Date());
        mVisitEnd = new DateTime(new Date());

        // Attempt to retrieve visitor ID from file
        long visitorId = getIdFromFile();

        // If found
        if (visitorId != 0){

            mVisitor = new Visitor(visitorId, mVisitStart, mVisitEnd, new Species[0]);

            // Retrieve visitor itinerary from server
            GetVisitorItinerary getVisitorItineraryTask = new GetVisitorItinerary(this, visitorId);
            getVisitorItineraryTask.execute();

        } else {

            // Obtain a visitor ID from server
            GetVisitorId getVisitorIdTask = new GetVisitorId(this, mVisitStart, mVisitEnd);
            getVisitorIdTask.execute();
        }
    }

    private void buildUI(){

        setContentView(R.layout.activity_home);

        // Set language
        RequestsModule.LANGUAGE = Locale.getDefault().getLanguage();

        // Build toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Build navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        String[] options = {
                getString(R.string.home),
                getString(R.string.my_visit),
                getString(R.string.animals),
                getString(R.string.attractions),
                getString(R.string.amenities),
                getString(R.string.park_map),
                getString(R.string.restaurant_menu),
                getString(R.string.share),
                getString(R.string.settings),
                getString(R.string.help)};

        mNavigationOptions = NavigationDrawerItem.build(options);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {

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
        mDrawerList = (RecyclerView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new NavigationDrawerAdapter(this, R.layout.drawer_list_item, mNavigationOptions));

        mDrawerList.setLayoutManager(new LinearLayoutManager(this));

        // Build progress wheel
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    public void onListFragmentInteraction(Parcelable item) {

    }

    public void selectNavigationItem(int position){

        GetVisitorItinerary getVisitorItineraryTask = new GetVisitorItinerary(this, mVisitor.getId());

        // Open the appropriate page
        switch (position){

            case 0: // If selection is 'Home'
                setTitle(R.string.app_name);
                getVisitorItineraryTask.execute();
                break;

            case 1: // If selection is 'My Visit'
                setTitle(mNavigationOptions[1].name);
                getVisitorItineraryTask.execute();
                break;

            case 2: // If selection is 'Animals'
                setTitle(mNavigationOptions[2].name);
                GetAllSpecies getAllSpeciesTask = new GetAllSpecies(this);
                getAllSpeciesTask.execute();
                break;

            case 3: // If selection is 'Attractions'
                setTitle(mNavigationOptions[3].name);
                break;

            case 4: // If selection is 'Amenities'
                setTitle(mNavigationOptions[4].name);
                GetAllAmenities getAllAmenitiesTask = new GetAllAmenities(this);
                getAllAmenitiesTask.execute();
                break;

            case 5: // If selection is 'Park Map'
                setTitle(mNavigationOptions[5].name);
                break;

            case 6: // If selection is 'Restaurant Menu'
                setTitle(mNavigationOptions[6].name);
                break;

            case 7: // If selection is 'Share'
                setTitle(mNavigationOptions[7].name);
                break;

            case 8: // If selection is 'Settings'
                setTitle(mNavigationOptions[8].name);
                break;

            case 9: // If selection is 'Help'
                setTitle(mNavigationOptions[9].name);
                break;

            default:
                break;
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

    public void createListFragment(ArrayList<Parcelable> listFromResponse, Class dataType){

        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();

        args.putParcelableArrayList(ListFragment.ARG_LIST, listFromResponse);
        args.putInt(ListFragment.ARG_COLUMN_COUNT, 1);

        args.putString(ListFragment.ARG_DATA_TYPE, dataType.getSimpleName());

        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
    }

    public void updateProgress(boolean complete) {

        if (complete) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public long getIdFromFile(){

        long idFromFile = 0;

        try {
            InputStream inputStream = getApplicationContext().openFileInput("visitor_id");

            if (inputStream != null) {

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                idFromFile = Long.parseLong(bufferedReader.readLine());

                inputStream.close();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return idFromFile;
    }

    public void setId(long visitorId){

        String filename = "visitor_id";
        String string = String.valueOf(visitorId);
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mVisitor = new Visitor(visitorId, mVisitStart, mVisitEnd, new Species[0]);

        GetVisitorItinerary getVisitorItineraryTask = new GetVisitorItinerary(this, visitorId);
        getVisitorItineraryTask.execute();
    }

    public void getNewId(){

        GetVisitorId getVisitorIdTask = new GetVisitorId(this, mVisitStart, mVisitEnd);
        getVisitorIdTask.execute();
    }
}