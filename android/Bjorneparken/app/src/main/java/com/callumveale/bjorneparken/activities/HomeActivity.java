package com.callumveale.bjorneparken.activities;

import android.app.DownloadManager;
import android.content.res.Configuration;

import android.os.Parcel;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.callumveale.bjorneparken.requests.GetVisitorStarredSpecies;
import com.callumveale.bjorneparken.requests.RequestsModule;
import com.callumveale.bjorneparken.requests.StarSpecies;
import com.callumveale.bjorneparken.requests.UnstarSpecies;
import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import none.bjorneparkappen_api.model.MainAreaListResponse;
import none.bjorneparkappen_api.model.MainEventListResponse;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;
import none.bjorneparkappen_api.model.MainSpeciesResponse;

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

            List itinerary = RequestsModule.convertListResponseToList(mFileWriter.getItineraryFromFile());
            List starredSpecies = RequestsModule.convertListResponseToList(mFileWriter.getStarredSpeciesFromFile());
            mVisitor = new Visitor(visitorId, mVisitStart, mVisitEnd, itinerary, starredSpecies);

            // Display home fragment
            HomeFragment homeFragment = HomeFragment.newInstance(mVisitor.getParcelableItinerary());
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, homeFragment).addToBackStack(null).commit();
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
                HomeFragment homeFragment = HomeFragment.newInstance(RequestsModule.convertListResponseToList(homeItinerary));
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, homeFragment).addToBackStack(null).commit();
                break;

            case 1: // If selection is 'My Visit'
                setTitle(mNavigationOptions[1].name);
                MainEventListResponse listItinerary = mFileWriter.getItineraryFromFile();
                ListFragment itineraryFragment = ListFragment.newInstance(RequestsModule.convertListResponseToList(listItinerary), 1, Event.class.getSimpleName(), true);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, itineraryFragment).addToBackStack(null).commit();
                break;

            case 2: // If selection is 'Animals'
                setTitle(mNavigationOptions[2].name);
                MainSpeciesListResponse species = mFileWriter.getSpeciesFromFile();
                ListFragment speciesFragment = ListFragment.newInstance(RequestsModule.convertListResponseToList(species), 1, Species.class.getSimpleName(), true);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, speciesFragment).addToBackStack(null).commit();
                break;

            case 3: // If selection is 'Attractions'
                setTitle(mNavigationOptions[3].name);
                MainAreaListResponse attractions = mFileWriter.getAttractionsFromFile();
                ListFragment attractionsFragment = ListFragment.newInstance(RequestsModule.convertListResponseToList(attractions), 1, Amenity.class.getSimpleName());
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, attractionsFragment).addToBackStack(null).commit();
                break;

            case 4: // If selection is 'Amenities'
                setTitle(mNavigationOptions[4].name);
                MainAreaListResponse amenities = mFileWriter.getAmenitiesFromFile();
                ListFragment amenitiesFragment = ListFragment.newInstance(RequestsModule.convertListResponseToList(amenities), 1, Amenity.class.getSimpleName());
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, amenitiesFragment).addToBackStack(null).commit();
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
        mVisitor = new Visitor(visitorId, mVisitStart, mVisitEnd);

        // Fetch user data for ID
        updateUserData();
    }

    public void checkVersion(DateTime version){

        Log.d("VERSIONS", "Checking versions");

        // If no version stored or version has changed
        if ((mVersion == null) || (mVersion.getValue() != version.getValue())) {

            Log.d("VERSIONS", "Versions differ");

            // Perform update
            updateParkData();

            // Fetch user's data again, as some starred items may not currently be available
            updateUserData();

            // Update stored version number
            mVersion = version;
            mFileWriter.writeVersionToFile(mVersion);
        }
    }

    public void updateParkData(){

        Log.d("PARK DATA", "Updating park data (x3)");

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

    public void saveItinerary(MainEventListResponse itineraryResponse){

        // Cache response
        List itinerary = RequestsModule.convertListResponseToList(itineraryResponse);
        mVisitor.setItinerary(itinerary);

        // Write response to file
        mFileWriter.writeItineraryToFile(itineraryResponse);

        // If no current fragment, display new Home Fragment
        if (getSupportFragmentManager().findFragmentById(R.id.content_frame) == null){


            HomeFragment homeFragment = HomeFragment.newInstance(mVisitor.getParcelableItinerary());
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, homeFragment).addToBackStack(null).commit();
        }
    }

    public void saveStarredSpecies(MainSpeciesListResponse starredSpeciesResponse){

        // Cache response
        List starredSpecies = RequestsModule.convertListResponseToList(starredSpeciesResponse);
        mVisitor.setStarredSpecies(starredSpecies);

        // Write response to file
        mFileWriter.writeStarredSpeciesToFile(starredSpeciesResponse);
    }

    public void updateUserData(){

        Log.d("VISITOR", "Checking visitor value");

        // If user initialised yet
        if (mVisitor != null) {

            Log.d("VISITOR", "Updating visitor (x2)");

            // Update itinerary
            GetVisitorItinerary getVisitorItineraryTask = new GetVisitorItinerary(this, mVisitor.getId());
            getVisitorItineraryTask.execute();

            // Update starred species
            GetVisitorStarredSpecies getVisitorStarredSpeciesTask = new GetVisitorStarredSpecies(this, mVisitor.getId());
            getVisitorStarredSpeciesTask.execute();
        }
    }

    public void getNewId(){

        GetVisitorId getVisitorIdTask = new GetVisitorId(this, mVisitStart, mVisitEnd);
        getVisitorIdTask.execute();
    }

    @Override
    public void onItemSelection(Event event) {

        DetailFragment fragment = DetailFragment.newInstance(event);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onListItemSelection(Parcelable item) {

        DetailFragment fragment = DetailFragment.newInstance(item);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onStarredListItemSelection(Parcelable item, Boolean isStarred){

        DetailFragment fragment = DetailFragment.newInstance(item, isStarred);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onItemStarred(Parcelable parcelable) {

        // If star action was against a species
        if (parcelable.getClass() == Species.class){

            // Retrieve visitor's list of starred species
            mVisitor.getStarredSpecies();

            int speciesIndex = -1;

            // Attempt to retrieve the index of the passed species
            for (Species species : mVisitor.getStarredSpecies()) {

                if (species.getId() == ((Species)parcelable).getId()) {

                    speciesIndex = mVisitor.getStarredSpecies().indexOf(species);
                    break;
                }
            }

            // If index was found
            if (speciesIndex != -1){

                // Remove the species from the local starred species list
                mVisitor.getStarredSpecies().remove(speciesIndex);

                // Remove the species from the datastore starred species list
                UnstarSpecies unstarSpeciesTask = new UnstarSpecies(this, mVisitor.getId(), (Species) parcelable);
                unstarSpeciesTask.execute();

            } else {

                // Add the species to the local starred species list
                mVisitor.getStarredSpecies().add((Species) parcelable);

                // Add the species to the datastore starred species list
                StarSpecies starSpeciesTask = new StarSpecies(this, mVisitor.getId(), (Species) parcelable);
                starSpeciesTask.execute();
            }

            // TODO replace with integrated read/write of lists
            MainSpeciesListResponse starredSpeciesResponse = new MainSpeciesListResponse();

            List<MainSpeciesResponse> speciesToSet = new ArrayList<>();

            for (Species species : mVisitor.getStarredSpecies()){

                MainSpeciesResponse speciesResponse = new MainSpeciesResponse();
                speciesResponse.setId(species.getId());
                speciesResponse.setCommonName(species.getCommonName());
                speciesResponse.setLatin(species.getLatin());
                speciesResponse.setDescription(species.getDescription());

                speciesToSet.add(speciesResponse);
            }

            starredSpeciesResponse.setSpecies(speciesToSet);
            mFileWriter.writeStarredSpeciesToFile(starredSpeciesResponse);

        } else if (parcelable.getClass() == Event.class) {

            // AddToItinerary addToItineraryTask = new AddToItinerary(mVisitor.getId(), (Event) parcelable);
            // addToItineraryTask.execute();
        }
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

    public Visitor getVisitor(){

        return mVisitor;
    }
}