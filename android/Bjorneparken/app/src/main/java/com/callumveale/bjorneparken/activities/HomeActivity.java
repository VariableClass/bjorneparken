package com.callumveale.bjorneparken.activities;

import android.content.res.Configuration;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
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
import com.callumveale.bjorneparken.file.FileWriter;
import com.callumveale.bjorneparken.fragments.DetailFragment;
import com.callumveale.bjorneparken.fragments.HomeFragment;
import com.callumveale.bjorneparken.fragments.ListFragment;
import com.callumveale.bjorneparken.models.Amenity;
import com.callumveale.bjorneparken.models.Event;
import com.callumveale.bjorneparken.models.NavigationDrawerItem;
import com.callumveale.bjorneparken.models.Species;
import com.callumveale.bjorneparken.models.Visitor;
import com.callumveale.bjorneparken.requests.GetAllAmenities;
import com.callumveale.bjorneparken.requests.GetAllAttractions;
import com.callumveale.bjorneparken.requests.GetAllSpecies;
import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.requests.GetVersion;
import com.callumveale.bjorneparken.requests.GetVisitorId;
import com.callumveale.bjorneparken.requests.GetVisitorItinerary;
import com.callumveale.bjorneparken.requests.RequestsModule;
import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import none.bjorneparkappen_api.model.MainAreaListResponse;
import none.bjorneparkappen_api.model.MainEventListResponse;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;

public class HomeActivity extends AppCompatActivity implements HomeFragment.OnItemSelectionListener, ListFragment.OnListItemSelectionListener, DetailFragment.OnItemStarredListener, NavigationDrawerAdapter.INavigationDrawerListener {

    // Toolbar
    private Toolbar mToolbar;

    // Home Activity Layout
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    // Navigation Drawer and options
    private RecyclerView mDrawerList;
    private NavigationDrawerItem[] mNavigationOptions;
    private int currentNavigationDrawerIndex = 0;

    // Progress Wheel
    private ProgressBar mProgressBar;

    // File Writer
    private FileWriter mFileWriter;

    // Version Information
    private DateTime mVersion;

    // Visitor Information
    private Visitor mVisitor;
    private DateTime mVisitStart;
    private DateTime mVisitEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Bjorneparken);
        super.onCreate(savedInstanceState);

        // Build and display UI components
        buildUI();

        mFileWriter = new FileWriter(getApplicationContext());

        // Attempt to retrieve data version from file
        mVersion = mFileWriter.getVersionFromFile();

        // Get version to compare from server
        GetVersion getVersionTask = new GetVersion(this);
        getVersionTask.execute();

        // Attempt to retrieve visitor ID from file
        long visitorId = mFileWriter.getIdFromFile();

        // If not found
        if (visitorId == 0){

            mVisitStart = new DateTime(new Date());
            mVisitEnd = new DateTime(new Date());

            // Obtain a visitor ID from server
            GetVisitorId getVisitorIdTask = new GetVisitorId(this, mVisitStart, mVisitEnd);
            getVisitorIdTask.execute();

        } else {

            // TODO Persist visitor data to file
            mVisitor = new Visitor(visitorId, mVisitStart, mVisitEnd, new Species[0]);
            createFragment((RequestsModule.convertListResponseToList(mFileWriter.getItineraryFromFile())), Event.class, new HomeFragment());
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

    public void selectNavigationItem(int position){

        // If we are currently on that page
        if (position == currentNavigationDrawerIndex){

            // Close the drawer
            mDrawerLayout.closeDrawer(mDrawerList);
            return;
        }

        // Open the appropriate page
        switch (position){

            case 0: // If selection is 'Home'
                setTitle(R.string.app_name);
                MainEventListResponse homeItinerary = mFileWriter.getItineraryFromFile();
                createFragment(RequestsModule.convertListResponseToList(homeItinerary), Event.class, new HomeFragment());
                break;

            case 1: // If selection is 'My Visit'
                setTitle(mNavigationOptions[1].name);
                MainEventListResponse listItinerary = mFileWriter.getItineraryFromFile();
                createFragment(RequestsModule.convertListResponseToList(listItinerary), Event.class, new ListFragment());
                break;

            case 2: // If selection is 'Animals'
                setTitle(mNavigationOptions[2].name);
                MainSpeciesListResponse species = mFileWriter.getSpeciesFromFile();
                createFragment(RequestsModule.convertListResponseToList(species), Species.class, new ListFragment());
                break;

            case 3: // If selection is 'Attractions'
                setTitle(mNavigationOptions[3].name);
                MainAreaListResponse attractions = mFileWriter.getAttractionsFromFile();
                createFragment(RequestsModule.convertListResponseToList(attractions), Amenity.class, new ListFragment());
                break;

            case 4: // If selection is 'Amenities'
                setTitle(mNavigationOptions[4].name);
                MainAreaListResponse amenities = mFileWriter.getAmenitiesFromFile();
                createFragment(RequestsModule.convertListResponseToList(amenities), Amenity.class, new ListFragment());
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

        // Set new drawer index
        currentNavigationDrawerIndex = position;

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

    public void createFragment(ArrayList<Parcelable> listFromResponse, Class dataType, Fragment fragment) {

        Bundle args = new Bundle();

        if (fragment.getClass() == ListFragment.class) {

            args.putParcelableArrayList(ListFragment.ARG_LIST, listFromResponse);
            args.putInt(ListFragment.ARG_COLUMN_COUNT, 1);
            args.putString(ListFragment.ARG_DATA_TYPE, dataType.getSimpleName());

        } else if (fragment.getClass() == HomeFragment.class) {

            args.putParcelableArrayList(HomeFragment.ARG_LIST, listFromResponse);
        }

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

    public void setId(long visitorId){

        // Store ID
        mFileWriter.writeIdToFile(visitorId);

        // Create new user
        mVisitor = new Visitor(visitorId, mVisitStart, mVisitEnd, new Species[0]);

        // Fetch user data for ID
        updateUserData();
    }

    public void checkVersion(DateTime version){

        // If the versions do not match
        if (mVersion != version){

            // Perform update
            updateParkData();

            // Fetch user's data again, as some starred items may not currently be available
            updateUserData();

            // Update version number
            mVersion = version;
            mFileWriter.writeVersionToFile(mVersion);
        }
    }

    public void updateParkData(){

        // Fetch amenities
        GetAllAmenities getAllAmenitiesTask = new GetAllAmenities(this);
        getAllAmenitiesTask.execute();

        // Fetch attractions
        GetAllAttractions getAllAttractionsTask = new GetAllAttractions(this);
        getAllAttractionsTask.execute();

        // Fetch species
        GetAllSpecies getAllSpeciesTask = new GetAllSpecies(this);
        getAllSpeciesTask.execute();
    }

    public void saveSpecies(MainSpeciesListResponse response){

        // Write response to file
        mFileWriter.writeSpeciesToFile(response);
    }

    public void saveAmenities(MainAreaListResponse amenities){

        // Write response to file
        mFileWriter.writeAmenitiesToFile(amenities);
    }

    public void saveAttractions(MainAreaListResponse attractions){

        // Write response to file
        mFileWriter.writeAttractionsToFile(attractions);
    }

    public void saveItinerary(MainEventListResponse itinerary){

        // Write response to file
        mFileWriter.writeItineraryToFile(itinerary);
    }

    public void updateUserData(){

        // If user initialised yet
        if (mVisitor != null) {

            // Update and display main page
            GetVisitorItinerary getVisitorItineraryTask = new GetVisitorItinerary(this, mVisitor.getId());
            getVisitorItineraryTask.execute();
        }
    }

    public void getNewId(){

        GetVisitorId getVisitorIdTask = new GetVisitorId(this, mVisitStart, mVisitEnd);
        getVisitorIdTask.execute();
    }

    @Override
    public void onItemSelection(Event event) {

        createDetailFragment(event);
    }

    @Override
    public void onListItemSelection(Parcelable item) {

        createDetailFragment(item);
    }

    private void createDetailFragment(Parcelable item){

        DetailFragment fragment = new DetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(DetailFragment.ARG_ITEM, item);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onItemStarred(Parcelable parcelable) {

    }

    @Override
    public void onBackPressed(){

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){

            // Close the drawer
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);

            if ((currentFragment.getClass() != DetailFragment.class)) {

                selectNavigationItem(0);

            } else {

                super.onBackPressed();
            }
        }
    }
}