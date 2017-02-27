package com.callumveale.bjorneparken.activities;

import android.content.res.Configuration;
import android.os.AsyncTask;

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
import android.widget.ProgressBar;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.models.Species;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import none.bjorneparkappen_api.BjorneparkappenApi;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;
import none.bjorneparkappen_api.model.MainSpeciesResponse;

public class HomeActivity extends AppCompatActivity implements SpeciesFragment.OnListFragmentInteractionListener {

    public static final String API_KEY = "AIzaSyCuD2dk_XFcn512V5JxAZbFlAK9dgNlQ9c";
    public static final String ROOT_URL = "https://api-dot-bjorneparkappen.appspot.com/_ah/api/";

    public static String sDefSystemLanguage;

    // Toolbar
    private Toolbar mToolbar;

    // Home Activity Layout
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    // Navigation Drawer and options
    private ListView mDrawerList;
    private String[] mNavigationOptions;

    // Progress Wheel
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // If returning from a previous state, return
        if (savedInstanceState != null) {
            return;
        }

        // Set language
        sDefSystemLanguage = Locale.getDefault().getLanguage();

        // Build toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Build navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationOptions = new String[]{getString(R.string.my_visit), getString(R.string.animals),
                getString(R.string.attractions), getString(R.string.amenities), getString(R.string.park_map),
                getString(R.string.restaurant_menu), getString(R.string.settings), getString(R.string.contact)};

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
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mNavigationOptions));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Build progress wheel
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    public void onListFragmentInteraction(Species item) {

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

        GetAllTask task = new GetAllTask(this);
        task.execute();

        switch (position){

            case 0: // If selection is 'My Visit'
                setTitle(mNavigationOptions[0]);
                break;

            case 1: // If selection is 'Animals'
                setTitle(mNavigationOptions[1]);
                break;

            case 2: // If selection is 'Attractions'
                setTitle(mNavigationOptions[2]);
                break;

            case 3: // If selection is 'Amenities'
                setTitle(mNavigationOptions[3]);
                break;

            case 4: // If selection is 'Park Map'
                setTitle(mNavigationOptions[4]);
                break;

            case 5: // If selection is 'Restaurant Menu'
                setTitle(mNavigationOptions[5]);
                break;

            case 6: // If selection is 'Settings'
                setTitle(mNavigationOptions[6]);
                break;

            case 7: // If selection is 'Contact'
                setTitle(mNavigationOptions[7]);
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

    class GetAllTask extends AsyncTask<Void, Void, MainSpeciesListResponse> {

        HomeActivity activity;

        GetAllTask(HomeActivity activity){

            this.activity = activity;
        }

        @Override
        protected MainSpeciesListResponse doInBackground(Void... params) {

            BjorneparkappenApi.Builder builder = new BjorneparkappenApi.Builder(
                    AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

            builder.setRootUrl(ROOT_URL);

            MainSpeciesListResponse speciesListResponse = new MainSpeciesListResponse();

            try {

                speciesListResponse = builder.build().species().all(sDefSystemLanguage).setKey(API_KEY).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }


            return speciesListResponse;
        }

        @Override
        protected void onPreExecute(){
            updateProgress(false);
        }

        @Override
        protected void onPostExecute(MainSpeciesListResponse response){

            updateProgress(true);
            createSpeciesFragment(response);
        }

        private void updateProgress(boolean complete){

            if (complete){
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }

        private void createSpeciesFragment(MainSpeciesListResponse response){

            SpeciesFragment fragment = new SpeciesFragment();
            Bundle args = new Bundle();

            ArrayList<Species> species = new ArrayList<>();

            if (response.getSpecies() != null){

                for (MainSpeciesResponse speciesResponse : response.getSpecies()){

                    species.add(new Species(speciesResponse.getCommonName(), speciesResponse.getLatin(), speciesResponse.getDescription()));
                }
            }
            args.putParcelableArrayList(SpeciesFragment.ARG_SPECIES_LIST, species);
            args.putInt(SpeciesFragment.ARG_COLUMN_COUNT, 1);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
        }
    }
}