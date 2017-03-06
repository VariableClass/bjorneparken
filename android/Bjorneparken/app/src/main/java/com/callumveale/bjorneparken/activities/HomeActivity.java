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
import com.callumveale.bjorneparken.file.ResponseConverter;
import com.callumveale.bjorneparken.fragments.DetailFragment;
import com.callumveale.bjorneparken.fragments.DialogListFragment;
import com.callumveale.bjorneparken.fragments.HomeFragment;
import com.callumveale.bjorneparken.fragments.ListFragment;
import com.callumveale.bjorneparken.models.Amenity;
import com.callumveale.bjorneparken.models.Animal;
import com.callumveale.bjorneparken.models.Enclosure;
import com.callumveale.bjorneparken.models.Event;
import com.callumveale.bjorneparken.models.Feeding;
import com.callumveale.bjorneparken.models.NavigationDrawerItem;
import com.callumveale.bjorneparken.models.Species;
import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.notifications.NotificationEventReceiver;
import com.callumveale.bjorneparken.requests.RequestsModule;

import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Date;

import none.bjorneparkappen_api.model.MainAreaListResponse;
import none.bjorneparkappen_api.model.MainEventListResponse;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;

public class HomeActivity extends AppCompatActivity implements ListFragment.OnListItemSelectionListener, DetailFragment.OnItemStarredListener, NavigationDrawerAdapter.INavigationDrawerListener {

    //region Properties

    //region UI Components

    // Toolbar
    private Toolbar mToolbar;

    // Home Activity Layout
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    // Navigation Drawer and options
    private RecyclerView mDrawerList;
    private NavigationDrawerItem[] mNavigationOptions;
    private int currentNavigationDrawerIndex;

    // Progress Wheel
    private ProgressBar mProgressBar;

    //endregion UI Components

    //region Data Retrieval

    // Request Maker
    private RequestsModule mRequester;

    // File Writer
    private FileWriter mFileWriter;

    // Version Information
    private DateTime mVersion;

    // Visitor Data
    private long mVisitorId;
    private DateTime mVisitStart;
    private DateTime mVisitEnd;
    private ArrayList<Event> mItinerary;
    private ArrayList<Species> mStarredSpecies;

    // Park Data
    private ArrayList<Amenity> mAmenities;
    private ArrayList<Amenity> mAttractions;
    private ArrayList<Feeding> mFeedings;
    private ArrayList<Species> mSpecies;

    //endregion Data Retrieval

    //endregion Properties

    //region Methods

    //region Initialisation

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Change theme from splash screen to application theme
        setTheme(R.style.Bjorneparken);

        super.onCreate(savedInstanceState);

        // Build and display UI components
        buildUIComponents();

        // Retrieve data with which to populate the application
        getApplicationData();
    }

    private void buildUIComponents(){

        setContentView(R.layout.activity_home);

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
        mDrawerList.setAdapter(new NavigationDrawerAdapter(mNavigationOptions, this));

        mDrawerList.setLayoutManager(new LinearLayoutManager(this));

        // Build progress wheel
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void getApplicationData(){

        // Create new FileWriter to interact with local data
        mFileWriter = new FileWriter(getApplicationContext());

        // Retrieve version and visitor ID
        getBaseDataFromFile();

        // Initialise requester to perform calls to the server
        mRequester = new RequestsModule(getString(R.string.app_name), this);

        // Get version from server
        checkVersion();

        // Decide whether to retrieve new ID or populate from local data
        checkUserId();
    }

    private void getBaseDataFromFile(){

        // Attempt to retrieve data version from file
        mVersion = mFileWriter.getVersionFromFile();

        // Attempt to retrieve visitor ID from file
        mVisitorId = mFileWriter.getIdFromFile();

        getParkDataFromFile();
    }

    //endregion Initialisation

    //region Park Data

    private void checkVersion(){

        // Obtain version from server to compare with local version
        mRequester.getVersion();
    }

    public void compareVersions(DateTime serverVersion){

        // If no version stored or version has changed
        if ((mVersion == null) || (mVersion.getValue() != serverVersion.getValue())) {

            // Perform update
            updateParkData();

            // If a visitor exists
            if (mVisitorId != 0) {

                // Fetch user's data again, as some starred items may not currently be available
                getVisitorDataFromServer();
            }

            // Update stored version number
            mVersion = serverVersion;
            mFileWriter.writeVersionToFile(mVersion);
        }
    }

    public void updateParkData(){

        // Fetch amenities
        mRequester.getAllAmenities();

        // Fetch attractions
        mRequester.getAllAttractions();

        // Fetch feedings
        mRequester.getAllFeedings();

        // Fetch species
        mRequester.getAllSpecies();
    }

    public void getParkDataFromFile(){

        mAmenities = mFileWriter.getAmenitiesFromFile();
        mAttractions = mFileWriter.getAttractionsFromFile();
        mFeedings = mFileWriter.getFeedingsFromFile();
        mSpecies = mFileWriter.getSpeciesFromFile();
    }

    private ArrayList<Feeding> getFeedingsForSpecies(Species species){

        ArrayList<Feeding> returnList = new ArrayList<>();

        //TODO Amend backend to add feedings to Species return list
        for (Feeding feeding : mFeedings){

            for (Animal animal : ((Enclosure) feeding.getLocation()).getAnimals()){

                if (animal.getSpecies().getId() == species.getId()){

                    returnList.add(feeding);
                    break;
                }
            }
        }

        return returnList;
    }

    public void saveAmenities(MainAreaListResponse amenities){

        // Cache response
        mAmenities = ResponseConverter.convertAmenityListResponse(amenities);

        // Write response to file
        mFileWriter.writeAmenitiesToFile(amenities);
    }

    public void saveAttractions(MainAreaListResponse attractions){

        // Cache response
        mAttractions = ResponseConverter.convertAmenityListResponse(attractions);

        // Write response to file
        mFileWriter.writeAttractionsToFile(attractions);
    }

    public void saveFeedings(MainEventListResponse feedings){

        // Cache response
        mFeedings = ResponseConverter.convertFeedingListResponse(feedings);

        // Write response to file
        mFileWriter.writeFeedingsToFile(feedings);
    }

    public void saveSpecies(MainSpeciesListResponse species){

        // Cache response
        mSpecies = ResponseConverter.convertSpeciesListResponse(species);

        // Write response to file
        mFileWriter.writeSpeciesToFile(species);
    }

    //endregion Park Data

    //region User Data

    public void checkUserId(){

        // TODO Retrieve visit of dates from user
        mVisitStart = new DateTime(new Date());
        mVisitEnd = new DateTime(new Date());

        // If not found
        if (mVisitorId == 0){

            // Obtain a visitor ID from server
            getNewId();

        } else {

            // Retrieve visitor data file
            getVisitorDataFromFile();

            // Perform visitor initialise action
            onVisitorInitialise();
        }
    }

    public void getNewId(){

        // Obtain new user ID from server
        mRequester.getVisitorId(mVisitStart, mVisitEnd);
    }

    public void setId(long visitorId){

        // Store ID
        mVisitorId = visitorId;
        mFileWriter.writeIdToFile(mVisitorId);

        // Fetch user data for new ID
        getVisitorDataFromServer();
    }

    private void getVisitorDataFromFile(){

        mItinerary = mFileWriter.getItineraryFromFile();
        mStarredSpecies = mFileWriter.getStarredSpeciesFromFile();

        onVisitorInitialise();
    }

    private void getVisitorDataFromServer(){

        // Update itinerary
        mRequester.getItinerary(mVisitorId);

        // Update starred species
        mRequester.getStarredSpecies(mVisitorId);
    }

    public void saveItinerary(MainEventListResponse itineraryResponse){

        // Cache response
        mItinerary = ResponseConverter.convertEventListResponse(itineraryResponse);

        // Write response to file
        mFileWriter.writeItineraryToFile(itineraryResponse);

        // Create notification service
        NotificationEventReceiver.setupAlarm(getApplicationContext(), mItinerary, mVisitStart, mVisitEnd);
    }

    public void saveStarredSpecies(MainSpeciesListResponse starredSpeciesResponse){

        // Cache response
        mStarredSpecies = ResponseConverter.convertSpeciesListResponse(starredSpeciesResponse);

        // Write response to file
        mFileWriter.writeStarredSpeciesToFile(starredSpeciesResponse);
    }

    public boolean isInItinerary(Event event){

        boolean isStarred = false;

        for (Event starredEvent : mItinerary){

            // If the event is starred
            if ((starredEvent.getId() == event.getId()) &&
                    (starredEvent.getLocation().getId() == event.getLocation().getId())){

                isStarred = true;
                break;
            }
        }

        return isStarred;
    }

    public boolean isStarred(Species species){

        boolean isStarred = false;

        for (Species starredSpecies : mStarredSpecies){

            // If the species is starred
            if (starredSpecies.getId() == species.getId()){

                isStarred = true;
                break;
            }
        }

        return isStarred;
    }

    public ArrayList<Species> getStarredSpecies(){

        return mStarredSpecies;
    }

    //endregion User Data

    //region UI

    private void setFragment(Fragment fragment){

        // Replace existing fragment with newly passed fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
    }

    public void updateProgress(boolean complete) {

        // If task has completed
        if (complete) {

            // Hide progress wheel
            mProgressBar.setVisibility(View.GONE);

        } else {

            // Show progress bar
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void navigateToPosition(int position){

        // If we are currently on that page
        if (position == currentNavigationDrawerIndex){

            // Close the drawer
            mDrawerLayout.closeDrawer(mDrawerList);
            return;
        }

        // Set new drawer index
        currentNavigationDrawerIndex = position;

        // Display home page by default
        String title = getString(R.string.app_name);
        Fragment fragment = HomeFragment.newInstance(mItinerary, mVisitStart, mVisitEnd);

        // Open the appropriate page
        switch (position){

            case 0: // If selection is 'Home'
                break;

            case 1: // If selection is 'My Visit'
                // Retrieve title for page
                title = mNavigationOptions[1].name;

                // Retrieve new list fragment, populating from visitor itinerary
                fragment = ListFragment.newInstance(mItinerary, 1, Event.class.getSimpleName(), true);
                break;

            case 2: // If selection is 'Animals'
                // Retrieve title for page
                title = mNavigationOptions[2].name;

                // Retrieve new list fragment, populating from list of species
                fragment = ListFragment.newInstance(mSpecies, 1, Species.class.getSimpleName(), true);
                break;

            case 3: // If selection is 'Attractions'
                // Retrieve title for page
                title = mNavigationOptions[3].name;

                // Retrieve new list fragment, populating from list of attractions
                fragment = ListFragment.newInstance(mAttractions, 1, Amenity.class.getSimpleName());
                break;

            case 4: // If selection is 'Amenities'
                // Retrieve title for page
                title = mNavigationOptions[4].name;

                // Retrieve new list fragment, populating from list of amenities
                fragment = ListFragment.newInstance(mAmenities, 1, Amenity.class.getSimpleName());
                break;

            case 5: // If selection is 'Park Map'
                // Retrieve title for page
                title = mNavigationOptions[5].name;
                break;

            case 6: // If selection is 'Restaurant Menu'
                // Retrieve title for page
                title = mNavigationOptions[6].name;
                break;

            case 7: // If selection is 'Share'
                // Retrieve title for page
                title = mNavigationOptions[7].name;
                break;

            case 8: // If selection is 'Settings'
                // Retrieve title for page
                title = mNavigationOptions[8].name;
                break;

            case 9: // If selection is 'Help'
                // Retrieve title for page
                title = mNavigationOptions[9].name;
                break;

            default:
                break;
        }

        // Set new title
        setTitle(title);

        // Change to new fragment
        setFragment(fragment);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void onVisitorInitialise(){

        // Create new home fragment, populating from visitor itinerary
        HomeFragment homeFragment = HomeFragment.newInstance(mItinerary, mVisitStart, mVisitEnd);

        // Change to new fragment
        setFragment(homeFragment);
    }

    //region Fragment Listener Methods

    @Override
    public void onItemSelection(Parcelable item) {

        DetailFragment detailFragment = DetailFragment.newInstance(item);
        setFragment(detailFragment);
    }

    @Override
    public void onStarredItemSelection(Parcelable item, Boolean isStarred){

        DetailFragment detailFragment = DetailFragment.newInstance(item, isStarred);
        setFragment(detailFragment);
    }

    @Override
    public void onItemStarred(Parcelable parcelable) {

        // If star action was against a species
        if (parcelable.getClass() == Species.class){

            // Perform species starred action
            onSpeciesStarred((Species) parcelable);

        } else if (parcelable.getClass() == Event.class || parcelable.getClass().getSuperclass() == Event.class) {

            // Perform event starred action
            onEventStarred((Event) parcelable);
        }
    }

    private void onSpeciesStarred(Species speciesToStar){

        int speciesIndex = -1;

        // Attempt to retrieve the index of the passed species
        for (Species species : mStarredSpecies) {

            if (species.getId() == speciesToStar.getId()) {

                speciesIndex = mStarredSpecies.indexOf(species);
                break;
            }
        }

        // If index was found
        if (speciesIndex != -1){

            // Remove the species from the local starred species list
            mStarredSpecies.remove(speciesIndex);

            // Remove the species from the datastore starred species list
            mRequester.unstarSpecies(mVisitorId, speciesToStar);

        } else {

            // Add the species to the local starred species list
            mStarredSpecies.add(speciesToStar);

            // Retrieve feedings for the species
            ArrayList<Feeding> feedings = getFeedingsForSpecies(speciesToStar);

            // Remove any inactive feedings
            for (Feeding feeding : feedings){

                if (!feeding.isActive()){

                    feedings.remove(feeding);
                }
            }

            // If there are feedings to show
            if (feedings.size() > 0) {

                // Display a dialog box to select them
                DialogListFragment options = DialogListFragment.newInstance(feedings);
                options.show(getSupportFragmentManager(), "DialogListFragment");
            }

            // Add the species to the datastore starred species list
            mRequester.starSpecies(mVisitorId, speciesToStar);
        }
    }

    private void onEventStarred(Event eventToStar){

        int eventIndex = -1;

        // Attempt to retrieve the index of the passed event
        for (Event event : mItinerary) {

            if (event.getId() == eventToStar.getId()) {

                if (event.getLocation().getId() == eventToStar.getLocation().getId()) {

                    eventIndex = mItinerary.indexOf(event);
                    break;
                }
            }
        }

        // If index was found
        if (eventIndex != -1){

            // Remove the event from the local itinerary
            mItinerary.remove(eventIndex);

            // Remove the event from the datastore itinerary
            mRequester.removeFromItinerary(mVisitorId, eventToStar);

        } else {

            // Add the event to the local itinerary
            mItinerary.add(eventToStar);

            // Add the species to the datastore itinerary
            mRequester.addToItinerary(mVisitorId, eventToStar);
        }

        // Update notification service
        NotificationEventReceiver.setupAlarm(getApplicationContext(), mItinerary, mVisitStart, mVisitEnd);
    }

    //endregion Fragment Listener Methods

    //endregion UI

    //region Activity Overridden Methods

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
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
        if (item.getItemId() == R.id.action_my_visit) {
            navigateToPosition(1);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){

        // If drawer is open
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){

            // Close it
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {

            // Get currently displayed fragment
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);

            // If it is a detail fragment, return to the list which provided it
            if ((currentFragment.getClass() == DetailFragment.class)) {

                super.onBackPressed();

            } else {

                // Else, navigate home
                navigateToPosition(0);
            }
        }
    }

    //endregion Activity Overridden Methods

    //endregion Methods
}