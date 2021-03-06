package com.callumveale.bjorneparken.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.ConnectivityManager;
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
import com.callumveale.bjorneparken.config.Configuration;
import com.callumveale.bjorneparken.file.FileWriter;
import com.callumveale.bjorneparken.file.ResponseConverter;
import com.callumveale.bjorneparken.fragments.DetailFragment;
import com.callumveale.bjorneparken.fragments.DialogConfirmFragment;
import com.callumveale.bjorneparken.fragments.DialogListFragment;
import com.callumveale.bjorneparken.fragments.HelpFragment;
import com.callumveale.bjorneparken.fragments.HomeFragment;
import com.callumveale.bjorneparken.fragments.ImageFragment;
import com.callumveale.bjorneparken.fragments.ListFragment;
import com.callumveale.bjorneparken.fragments.SettingsFragment;
import com.callumveale.bjorneparken.fragments.SocialFragment;
import com.callumveale.bjorneparken.models.Amenity;
import com.callumveale.bjorneparken.models.Animal;
import com.callumveale.bjorneparken.models.Area;
import com.callumveale.bjorneparken.models.Enclosure;
import com.callumveale.bjorneparken.models.Event;
import com.callumveale.bjorneparken.models.Feeding;
import com.callumveale.bjorneparken.models.IModel;
import com.callumveale.bjorneparken.models.NavigationDrawerItem;
import com.callumveale.bjorneparken.models.Species;
import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.notifications.NotificationEventReceiver;
import com.callumveale.bjorneparken.requests.NetworkChangeReceiver;
import com.callumveale.bjorneparken.requests.RequestsModule;

import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import none.bjorneparkappen_api.model.MainAreaListResponse;
import none.bjorneparkappen_api.model.MainEventListResponse;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;

public class HomeActivity
        extends AppCompatActivity
        implements ListFragment.OnListItemSelectionListener,
            DetailFragment.OnItemStarredListener,
            NavigationDrawerAdapter.INavigationDrawerListener,
            SettingsFragment.OnNotificationsChangedListener {

    //region Constants

    private static final String ARG_SERVER_AVAILABLE = "server-available";
    public static final String ARG_VISIT_START = "visit-start";
    public static final String ARG_VISIT_END = "visit-end";
    private static final int INITIALISE_DATES = 0;

    //endregion Constants

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

    // Application initialising indicator
    private boolean mInitialised;

    // Network Change Listener
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private boolean mReceiverRegistered;
    private boolean mServerAvailable;

    // Request Maker
    private RequestsModule mRequester;
    private int mRequestsActive;

    // Configuration File
    private Configuration mConfig;

    // File Writer
    private FileWriter mFileWriter;

    // Version Information
    private DateTime mVersion;

    // Visitor Data
    private long mVisitorId;
    private DateTime mVisitStart;
    private DateTime mVisitEnd;
    private ArrayList<Event> mItinerary;
    private boolean mItineraryUpdated;
    private ArrayList<Species> mStarredSpecies;
    private boolean mStarredSpeciesUpdated;

    // Park Data
    private ArrayList<Area> mAreas;
    private ArrayList<Feeding> mFeedings;
    private ArrayList<Species> mSpecies;
    private ArrayList<Event> mEvents;

    //endregion Data Retrieval

    //endregion Properties

    //region Methods

    //region Initialisation

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Set initialised to false
        mInitialised = false;

        // If creating from a bundle
        if (savedInstanceState != null) {

            // Set server available from savedInstanceState
            mServerAvailable = savedInstanceState.getBoolean(ARG_SERVER_AVAILABLE);
        }

        // Change theme from splash screen to application theme
        setTheme(R.style.Bjorneparken);

        super.onCreate(savedInstanceState);

        // Build and display UI components
        buildUIComponents();

        // Initialise requester to perform calls to the server
        mRequester = new RequestsModule(getString(R.string.app_name), this);
        mRequestsActive = 0;

        // If server was not previously available
        if (!mServerAvailable) {

            // Initialise and register a new receiver to notify the main thread of network changes
            setupNetworkChangeListener();
        }
    }

    private void buildUIComponents(){

        setContentView(R.layout.activity_home);

        // Build toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Build navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationOptions = NavigationDrawerItem.build(this);

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

    private void setupNetworkChangeListener(){

        // Unregister receiver
        if (mReceiverRegistered) {

            unregisterReceiver(mNetworkChangeReceiver);
            mReceiverRegistered = false;
        }

        // Initialise new network change receiver to flag network changes
        mNetworkChangeReceiver = new NetworkChangeReceiver(mRequester);

        // Create intent filter and add CONNECTIVITY_ACTION to limit alerts to network connection changes
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        // Register receiver with filter as broadcast receiver
        registerReceiver(mNetworkChangeReceiver, filter);

        // Update registered state
        mReceiverRegistered = true;
    }

    private void getData(){

        // Create new FileWriter to interact with local data
        mFileWriter = new FileWriter(getApplicationContext());

        // Get config from file
        mConfig = mFileWriter.getConfigFromFile();

        // Attempt to retrieve visitor ID from file
        mVisitorId = mFileWriter.getIdFromFile();

        // Attempt to retrieve park data from file
        getParkDataFromFile();

        // Attempt to retrieve visitor data from file
        getVisitorDataFromFile();

        // If user exists
        if (mVisitorId != 0){

            // If server is available
            if (mServerAvailable) {

                // Attempt to retrieve version from file
                mVersion = mFileWriter.getVersionFromFile();

                // Check version
                checkVersion();

            } else {

                // Initialise view with locally available data
                onAllDataInitialised();
            }

        } else {

            // Create new date objects at the current day
            mVisitStart = new DateTime(new Date());
            mVisitEnd = new DateTime(new Date());

            // Create activity to allow the user to select their visit dates
            Intent getDateIntent = new Intent(this, WelcomeActivity.class);
            getDateIntent.putExtra(ARG_VISIT_START, mVisitStart);
            getDateIntent.putExtra(ARG_VISIT_END, mVisitEnd);
            startActivityForResult(getDateIntent, INITIALISE_DATES);
        }
    }

    private void onAllDataInitialised(){

        // Update initialised flag
        mInitialised = true;

        // Create new home fragment, populating from visitor itinerary
        HomeFragment homeFragment = HomeFragment.newInstance(mItinerary, mVisitStart, mVisitEnd);

        // Change to new fragment
        setFragment(homeFragment);
    }

    //endregion Initialisation

    //region Data From File

    public void getParkDataFromFile(){

        // Attempt to retrieve areas from file
        mAreas = mFileWriter.getAreasFromFile();

        // Retrieve images for any area which has them
        for (Area area : mAreas){

            getImageFromFile((IModel) area);
        }

        // Attempt to retrieve events from file
        mEvents = mFileWriter.getEventsFromFile();

        // Retrieve images for any event which has them
        for (Event event : mEvents){

            getImageFromFile(event);
        }

        // Attempt to retrieve feedings from file
        mFeedings = mFileWriter.getFeedingsFromFile();

        // Retrieve images for any species which have them
        for (Feeding feeding : mFeedings){

            getImageFromFile(feeding);
        }

        // Attempt to retrieve species from file
        mSpecies = mFileWriter.getSpeciesFromFile();

        // Retrieve images for any species which has them
        for (Species speciesInst : mSpecies){

            getImageFromFile(speciesInst);
        }
    }

    private void getVisitorDataFromFile(){

        // Retrieve visitor dates from file
        mVisitStart = mFileWriter.getVisitStartDateFromFile();
        mVisitEnd = mFileWriter.getVisitEndDateFromFile();

        // Attempt to retrieve visitor itinerary from file
        mItinerary = mFileWriter.getItineraryFromFile();

        // Retrieve images for any species which has them
        for (Event event : mItinerary){

            getImageFromFile(event);
        }

        // Attempt to retrieve visitor starred species from file
        mStarredSpecies = mFileWriter.getStarredSpeciesFromFile();
    }

    private ArrayList<Feeding> getFeedingsForSpecies(Species species){

        ArrayList<Feeding> returnList = new ArrayList<>();

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

    private ArrayList<Enclosure> getEnclosuresFromAreas(){

        ArrayList<Enclosure> returnList = new ArrayList<>();

        for (Area area : mAreas){

            if (area instanceof Enclosure){

                returnList.add((Enclosure) area);
            }
        }

        return returnList;
    }

    private ArrayList<Enclosure> getEnclosuresForSpecies(Species species){

        ArrayList<Enclosure> returnList = new ArrayList<>();

        // Fetch all enclosures
        ArrayList<Enclosure> allEnclosures = getEnclosuresFromAreas();

        // For each enclosure, check whether it contains the requested species
        for (Enclosure enclosure : allEnclosures){

            for (Species speciesInst : enclosure.getSpecies()){

                if (species.getId() == speciesInst.getId()){

                    returnList.add(enclosure);
                    break;
                }
            }
        }

        return returnList;
    }

    private ArrayList<Amenity> getAmenitiesFromAreas(){

        ArrayList<Amenity> returnList = new ArrayList<>();

        for (Area area : mAreas){

            if (area instanceof Amenity){

                Amenity amenity = (Amenity) area;

                // If amenity is not an attraction
                if (!(amenity.getAmenityType().equals("ATTRACTION"))) {

                    returnList.add((Amenity) area);
                }
            }
        }

        return returnList;
    }

    private ArrayList<Amenity> getAttractionsFromAreas(){

        ArrayList<Amenity> returnList = new ArrayList<>();

        for (Area area : mAreas){

            if (area instanceof Amenity){

                Amenity amenity = (Amenity) area;

                // If amenity is an attraction
                if (amenity.getAmenityType().equals("ATTRACTION")) {

                    returnList.add((Amenity) area);
                }
            }
        }

        return returnList;
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

    //endregion Data From File

    //region Data From Server

    public void setServerAvailability(boolean available) {

        mServerAvailable = available;

        // If application is still initialising
        if (!mInitialised) {

            // Populate app with data
            getData();

        } else {

            // If server is not available, return
            if (!mServerAvailable) {

                return;
            }

            // Check version has not changed since last connection
            checkVersion();

            // If itinerary has been updated whilst offline, synchronise it
            if (mItineraryUpdated) {

                mRequester.syncItinerary(mVisitorId, mItinerary);
            }

            // If starred species list has been updated whilst offline, synchronise it
            if (mStarredSpeciesUpdated) {

                mRequester.syncStarredSpecies(mVisitorId, mStarredSpecies);
            }
        }
    }

    private void checkVersion(){

        // Obtain version from server to compare with local version
        mRequester.getVersion();
    }

    public void compareVersions(DateTime serverVersion){

        // If no version stored or version has changed
        if ((mVersion == null) || (mVersion.getValue() != serverVersion.getValue())) {

            // Retrieve park data from server
            getParkDataFromServer();

            // If initialising
            if (!mInitialised) {

                // Fetch user's data, as some previously starred items may not currently be available
                getVisitorDataFromServer();
            }

            // Update stored version number
            mVersion = serverVersion;
            mFileWriter.writeVersionToFile(mVersion);
        }

        // If initialising
        if (!mInitialised) {

            // Initialise view with most up to date data
            onAllDataInitialised();
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

        // Fetch latest park data
        getParkDataFromServer();

        // Fetch user data for ID
        getVisitorDataFromServer();

        // Initialise view with locally available data
        onAllDataInitialised();
    }

    private void getVisitorDataFromServer(){

        // Update itinerary
        mRequester.getItinerary(mVisitorId);

        // Update starred species
        mRequester.getStarredSpecies(mVisitorId);
    }

    public void getItinerary(){

        // Update itinerary
        mRequester.getItinerary(mVisitorId);
    }

    public void getParkDataFromServer(){

        // Fetch areas
        mRequester.getAllAreas();

        // Fetch events
        mRequester.getAllEvents();

        // Fetch feedings
        mRequester.getAllFeedings();

        // Fetch species
        mRequester.getAllSpecies();
    }

    //region Callbacks

    //region Park Data Callbacks

    public void saveAreas(MainAreaListResponse areas){

        // Cache response
        mAreas = ResponseConverter.convertAreaListResponse(areas);

        // Write response to file
        mFileWriter.writeAreasToFile(areas);

        // Retrieve images for any areas which have them
        for (Area area : mAreas){

            getImageFromServer((IModel) area);
        }
    }

    public void saveEvents(MainEventListResponse events){

        // Cache response
        mEvents = ResponseConverter.convertEventListResponse(events);

        // Write response to file
        mFileWriter.writeEventsToFile(events);

        // Retrieve images for any events which have them
        for (Event event : mEvents){

            getImageFromServer(event);
        }
    }

    public void saveFeedings(MainEventListResponse feedings){

        // Cache response
        mFeedings = ResponseConverter.convertFeedingListResponse(feedings);

        // Write response to file
        mFileWriter.writeFeedingsToFile(feedings);

        // Retrieve images for any feedings which have them
        for (Feeding feeding : mFeedings){

            getImageFromServer(feeding);
        }
    }

    public void saveSpecies(MainSpeciesListResponse species){

        // Cache response
        mSpecies = ResponseConverter.convertSpeciesListResponse(species);

        // Retrieve images for any species which have them
        for (Species speciesInst : mSpecies){

            getImageFromServer(speciesInst);
        }

        // Write response to file
        mFileWriter.writeSpeciesToFile(species);
    }

    private void getImageFromServer(IModel item){

        // Check if server is available
        if (!mServerAvailable) {

            return;
        }

        // Retrieve image from server
        mRequester.getImage(item);
    }

    private void getImageFromFile(IModel item){

        // Check if item has an image
        if (item.getImageUrl() == null) {

            return;
        }

        // Attempt to read image from file
        mFileWriter.getImageFromFile(item);
    }

    //endregion Park Data Callbacks

    //region User Data Callbacks

    public void saveItinerary(MainEventListResponse itineraryResponse){

        // Cache response
        mItinerary = ResponseConverter.convertEventListResponse(itineraryResponse);

        // Retrieve images for items
        for (Event event : mItinerary){

            getImageFromServer(event);
        }

        // Clear synchronisation flag
        mItineraryUpdated = false;

        // Write response to file
        mFileWriter.writeItineraryToFile(itineraryResponse);

        // If notifications enabled
        if (Boolean.valueOf(mConfig.getProperty(Configuration.NOTIFICATIONS_ENABLED))) {

            // Create notification service
            NotificationEventReceiver.setupAlarm(getApplicationContext(), mItinerary, mVisitStart, mVisitEnd);
        }

        // If currently on the home page, refresh it
        if (currentNavigationDrawerIndex == 0){

            navigateToPosition(0);
        }
    }

    public void saveStarredSpecies(MainSpeciesListResponse starredSpeciesResponse){

        // Cache response
        mStarredSpecies = ResponseConverter.convertSpeciesListResponse(starredSpeciesResponse);

        // Retrieve images for items
        for (Species species : mStarredSpecies){

            getImageFromServer(species);
        }

        // Clear synchronisation flag
        mStarredSpeciesUpdated = false;

        // Write response to file
        mFileWriter.writeStarredSpeciesToFile(starredSpeciesResponse);
    }

    //endregion User Data Callbacks

    //endregion Callbacks

    //endregion Data From Server

    //region UI

    private void setFragment(Fragment fragment){

        // Do not perform any fragment transactions if the activity is finishing or destroyed
        if (isFinishing() || getSupportFragmentManager().isDestroyed()){

            return;
        }

        // Get currently displayed fragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);

        if (currentFragment != null) {

            // Replace existing fragment with newly passed fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commitAllowingStateLoss();

        } else {

            // Create fragment
            getSupportFragmentManager().beginTransaction().add(R.id.content_frame, fragment).addToBackStack(null).commitAllowingStateLoss();
        }

        getSupportFragmentManager().executePendingTransactions();
    }

    public void updateProgress(boolean complete) {

        // If task has completed
        if (complete) {

            // Decrease number of active requests
            mRequestsActive -= 1;

            // If no requests active
            if (mRequestsActive == 0) {

                // Hide progress wheel
                mProgressBar.setVisibility(View.GONE);
            }

        } else {

            // Increase number of active requests
            mRequestsActive += 1;

            // Show progress bar
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void navigateToPosition(int position){

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

                // Sort events by time
                Collections.sort(mItinerary, new Event.EventTimeComparator());

                // Retrieve new list fragment, populating from visitor itinerary
                fragment = ListFragment.newInstance(mItinerary, 1, Event.class.getSimpleName(), true);
                break;

            case 2: // If selection is 'Animals'
                // Retrieve title for page
                title = mNavigationOptions[2].name;

                // Sort species by starred
                Collections.sort(mSpecies, new Comparator<Species>(){

                    @Override
                    public int compare(Species species1, Species species2) {

                        boolean species1Starred = isStarred(species1);
                        boolean species2Starred = isStarred(species2);

                        if (species1Starred && !species2Starred){

                            return -1;

                        } else if (!species1Starred && species2Starred){

                            return 1;

                        } else {

                            return species1.getCommonName().compareTo(species2.getCommonName());
                        }
                    }
                });

                // Retrieve new list fragment, populating from list of species
                fragment = ListFragment.newInstance(mSpecies, 1, Species.class.getSimpleName(), true);
                break;

            case 3: // If selection is 'Events'
                // Retrieve title for page
                title = mNavigationOptions[3].name;

                // Sort events by starred/time
                Collections.sort(mEvents, new Comparator<Event>() {

                    @Override
                    public int compare(Event event1, Event event2) {

                        boolean event1Starred = isInItinerary(event1);
                        boolean event2Starred = isInItinerary(event2);

                        if (event1Starred && !event2Starred) {

                            return -1;

                        } else if (!event1Starred && event2Starred) {

                            return 1;

                        } else {

                            Calendar now = Calendar.getInstance();
                            return event1.getEventStartCalendar(now, 0).compareTo(event2.getEventStartCalendar(now, 0));
                        }
                    }
                });

                // Retrieve new list fragment, populating from list of events
                fragment = ListFragment.newInstance(mEvents, 1, Event.class.getSimpleName(), true);
                break;

            case 4: // If selection is 'Attractions'
                // Retrieve title for page
                title = mNavigationOptions[4].name;

                // Retrieve attractions from list of areas
                ArrayList<Amenity> attractions = getAttractionsFromAreas();

                // Sort attractions
                Collections.sort(attractions, new Amenity.AmenityComparator(this));

                // Retrieve new list fragment, populating from list of attractions
                fragment = ListFragment.newInstance(attractions, 1, Amenity.class.getSimpleName());
                break;

            case 5: // If selection is 'Amenities'
                // Retrieve title for page
                title = mNavigationOptions[5].name;

                // Retrieve amenities from list of areas
                ArrayList<Amenity> amenities = getAmenitiesFromAreas();

                // Sort amenities
                Collections.sort(amenities, new Amenity.AmenityComparator(this));

                // Retrieve new list fragment, populating from list of amenities
                fragment = ListFragment.newInstance(amenities, 1, Amenity.class.getSimpleName());
                break;

            case 6: // If selection is 'Map'
                // Retrieve title for page
                title = mNavigationOptions[6].name;

                // Retrieve new menu fragment
                fragment = ImageFragment.newMapInstance();
                break;

            case 7: // If selection is 'Restaurant Menu'
                // Retrieve title for page
                title = mNavigationOptions[7].name;

                // Retrieve new menu fragment
                fragment = ImageFragment.newMenuInstance();
                break;

            case 8: // If selection is 'Social'
                // Retrieve title for page
                title = mNavigationOptions[8].name;

                // Retrieve new social fragment
                fragment = SocialFragment.newInstance(mServerAvailable);
                break;

            case 9: // If selection is 'Settings'
                // Retrieve title for page
                title = mNavigationOptions[9].name;

                // Retrieve new settings fragment
                fragment = SettingsFragment.newInstance(mConfig);
                break;

            case 10: // If selection is 'Help'
                // Retrieve title for page
                title = mNavigationOptions[10].name;

                // Retrieve new help fragment
                fragment = HelpFragment.newInstance();
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
    public void onItemStarred(Parcelable parcelable){

        onItemStarred(parcelable, null, null, -1);
    }

    private void onItemStarred(Parcelable parcelable, DialogConfirmFragment.OnUnstarDetailListener unstarDetailListener, DialogConfirmFragment.OnUnstarListItemListener unstarListItemListener, int position) {

        // If star action was against a species
        if (parcelable.getClass() == Species.class){

            // Perform species starred action
            onSpeciesStarred((Species) parcelable, unstarDetailListener, unstarListItemListener, position);

        } else if (parcelable.getClass() == Event.class || parcelable.getClass().getSuperclass() == Event.class) {

            // Perform event starred action
            onEventStarred((Event) parcelable, unstarDetailListener, unstarListItemListener, position);
        }
    }

    @Override
    public void onItemStarred(Parcelable parcelable, DialogConfirmFragment.OnUnstarListItemListener listener, int position){

        onItemStarred(parcelable, null, listener, position);
    }

    @Override
    public void onItemStarred(Parcelable parcelable, DialogConfirmFragment.OnUnstarDetailListener listener){

        onItemStarred(parcelable, listener, null, -1);
    }

    private void onSpeciesStarred(Species speciesToStar, DialogConfirmFragment.OnUnstarDetailListener unstarDetailListener, DialogConfirmFragment.OnUnstarListItemListener unstarListItemListener, int position){

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

            Species species = mStarredSpecies.get(speciesIndex);

            // Remove the species from the local starred species list
            mStarredSpecies.remove(species);

            // If the server is available
            if (mServerAvailable) {

                // Remove the species from the datastore starred species list
                mRequester.unstarSpecies(mVisitorId, speciesToStar);

            } else {

                // Else, flag the starred species list as updated to be synchronised when the server next becomes available
                mStarredSpeciesUpdated = true;

                // Write to file
                mFileWriter.writeStarredSpeciesToFile(mStarredSpecies);

                // Remove all feedings for the species
                removeFeedingsForSpecies(species);
            }

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

                // Sort feedings by time
                Collections.sort(feedings, new Event.EventTimeComparator());

                // Display a dialog box to select them
                DialogListFragment options = DialogListFragment.newInstance(feedings);
                options.show(getSupportFragmentManager(), "DialogListFragment");

            } else {

                String dialogTitle;
                String dialogMessage;

                // Retrieve list of enclosures with species
                ArrayList<Enclosure> enclosuresWithSpecies = getEnclosuresForSpecies(speciesToStar);

                if (enclosuresWithSpecies.size() > 0){

                    // Build up string representation of enclosures containing the species
                    StringBuilder strBuilder = new StringBuilder();

                    for (int i = 0; i < enclosuresWithSpecies.size(); i++){

                        // Add enclosure label to string representation
                        strBuilder.append(enclosuresWithSpecies.get(i).getLabel());
                    }

                    dialogTitle = getString(R.string.no_feedings_title);
                    dialogMessage = String.format(getString(R.string.no_feedings_message), speciesToStar.getCommonName(), strBuilder.toString());

                } else {


                    dialogTitle = String.format(getString(R.string.species_unavailable_title), speciesToStar.getCommonName());
                    dialogMessage = getString(R.string.species_unavailable_message);
                }

                DialogConfirmFragment noFeedings = DialogConfirmFragment.newInstance(dialogTitle, dialogMessage);

                if (unstarDetailListener != null){

                    // Show confirmation dialog
                    noFeedings.setUnstarDetailListener(unstarDetailListener);
                }
                if (unstarListItemListener != null){

                    // Show confirmation dialog
                    noFeedings = DialogConfirmFragment.newInstance(dialogTitle, dialogMessage, position);
                    noFeedings.setUnstarListItemListener(unstarListItemListener);
                }

                noFeedings.show(getSupportFragmentManager(), "DialogConfirmFragment");
            }

            // If the server is available
            if (mServerAvailable) {

                // Add the species to the datastore starred species list
                mRequester.starSpecies(mVisitorId, speciesToStar);

            } else {

                // Else, flag the starred species list as updated to be synchronised when the server next becomes available
                mStarredSpeciesUpdated = true;

                // Write to file
                mFileWriter.writeStarredSpeciesToFile(mStarredSpecies);
            }
        }
    }

    private void removeFeedingsForSpecies(Species species){

        // Retrieve all feedings for the species
        ArrayList<Feeding> feedings = getFeedingsForSpecies(species);

        // For each feedings
        for (Feeding feeding : feedings){

            // For each event in the itinerary
            for (Event event : mItinerary){

                // If the event is found in the itinerary
                if (feeding.getId() == event.getId() && feeding.getLocation().getId() == event.getLocation().getId()){

                    // Remove it
                    mItinerary.remove(event);

                    break;
                }
            }
        }

        // Update flag
        mItineraryUpdated = true;

        // Update stored itinerary
        mFileWriter.writeItineraryToFile(mItinerary);
    }

    private void onEventStarred(Event eventToStar, DialogConfirmFragment.OnUnstarDetailListener unstarDetailListener, DialogConfirmFragment.OnUnstarListItemListener unstarListItemListener, int position){

        int eventIndex = -1;
        boolean conflict = false;

        // Attempt to retrieve the index of the passed event
        for (Event event : mItinerary) {

            if (event.getId() == eventToStar.getId()) {

                if (event.getLocation().getId() == eventToStar.getLocation().getId()) {

                    eventIndex = mItinerary.indexOf(event);
                    break;
                }
            }

            // Check if event conflicts
            if (event.conflictsWith(eventToStar)){
                conflict = true;
            }
        }

        // If index was found
        if (eventIndex != -1){

            // Remove the event from the local itinerary
            mItinerary.remove(eventIndex);

            // If server is available
            if (mServerAvailable) {

                // Remove the event from the datastore itinerary
                mRequester.removeFromItinerary(mVisitorId, eventToStar);

            } else {

                // Else, flag the itinerary as updated to be synchronised when the server next becomes available
                mItineraryUpdated = true;

                // Write to file
                mFileWriter.writeItineraryToFile(mItinerary);
            }

        } else {

            if (!conflict) {

                // Add the event to the local itinerary
                mItinerary.add(eventToStar);

                // If server is available
                if (mServerAvailable) {

                    // Add the species to the datastore itinerary
                    mRequester.addToItinerary(mVisitorId, eventToStar);

                } else {

                    // Else, flag the itinerary as updated to be synchronised when the server next becomes available
                    mItineraryUpdated = true;

                    // Write to file
                    mFileWriter.writeItineraryToFile(mItinerary);
                }

            } else {

                DialogConfirmFragment confirmDialog = DialogConfirmFragment.newInstance(R.string.event_conflict_title, R.string.event_conflict_message, true);

                if (unstarDetailListener != null){

                    // Show confirmation dialog
                    confirmDialog.setUnstarDetailListener(unstarDetailListener);
                }
                if (unstarListItemListener != null){

                    // Show confirmation dialog
                    confirmDialog = DialogConfirmFragment.newInstance(R.string.event_conflict_title, R.string.event_conflict_message, position, true);
                    confirmDialog.setUnstarListItemListener(unstarListItemListener);
                }

                confirmDialog.show(getSupportFragmentManager(), "DialogConfirmFragment");
            }

        }

        // If notifications enabled
        if (Boolean.valueOf(mConfig.getProperty(Configuration.NOTIFICATIONS_ENABLED))) {

            // Create notification service
            NotificationEventReceiver.setupAlarm(getApplicationContext(), mItinerary, mVisitStart, mVisitEnd);
        }
    }

    @Override
    public void onNotificationsEnabledChanged(boolean enabled){

        // Update config and write to file
        mConfig.setNotificationsEnabled(enabled);
        mFileWriter.writeConfigToFile(mConfig);
    }

    //endregion Fragment Listener Methods

    //endregion UI

    //region Activity Overridden Methods

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INITIALISE_DATES && resultCode == Activity.RESULT_OK){

            // Update dates
            mVisitStart = (DateTime) data.getSerializableExtra(ARG_VISIT_START);
            mVisitEnd = (DateTime) data.getSerializableExtra(ARG_VISIT_END);

            // Write dates to file
            mFileWriter.writeVisitStartDateToFile(mVisitStart);
            mFileWriter.writeVisitEndDateToFile(mVisitEnd);

            // Initialise version file
            mVersion = new DateTime(new Date(0));
            mFileWriter.writeVersionToFile(mVersion);

            // If server is not available
            if (!mServerAvailable) {

                return;
            }

            // Obtain new ID
            getNewId();

        }
    }

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
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
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

        } else if (item.getItemId() == R.id.action_share){

            navigateToPosition(8);
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);

            if (fragment.getClass() == SocialFragment.class) {

                ((SocialFragment) fragment).takePhoto(SocialFragment.OTHER);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop(){

        // Unregister receiver
        if (mReceiverRegistered) {

            unregisterReceiver(mNetworkChangeReceiver);
            mReceiverRegistered = false;
        }

        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(ARG_SERVER_AVAILABLE, mServerAvailable);
    }

    @Override
    public void onPause(){

        // Unregister receiver
        if (mReceiverRegistered){

            unregisterReceiver(mNetworkChangeReceiver);
            mReceiverRegistered = false;
        }

        super.onPause();
    }

    @Override
    public void onResume(){

        setupNetworkChangeListener();
        super.onResume();
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
            if (currentFragment.getClass() == DetailFragment.class) {

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