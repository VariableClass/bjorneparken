package callumveale.bjorneparken;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    public static final String API_KEY = "AIzaSyCuD2dk_XFcn512V5JxAZbFlAK9dgNlQ9c";
    public static final String ROOT_URL = "https://bjorneparkappen.appspot.com/_ah/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Build toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void getSpecies(View view){

        // Create intent
        Intent intent = new Intent(this, ListSpeciesActivity.class);

        // Start intent
        startActivity(intent);
    }

    public void getAmenities(View view){

        // Create intent
        Intent intent = new Intent(this, ListAmenitiesActivity.class);

        // Start intent
        startActivity(intent);
    }

    public void getEvents(View view){

        // Create intent
        Intent intent = new Intent(this, ListEventsActivity.class);

        // Start intent
        startActivity(intent);
    }

    public void getFeedings(View view){

        // Create intent
        Intent intent = new Intent(this, ListFeedingsActivity.class);

        // Start intent
        startActivity(intent);
    }

}