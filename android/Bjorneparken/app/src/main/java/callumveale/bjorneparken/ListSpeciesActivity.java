package callumveale.bjorneparken;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;

import none.bjorneparkappen.Bjorneparkappen;
import none.bjorneparkappen.model.MainSpeciesListResponse;
import none.bjorneparkappen.model.MainSpeciesResponse;

import static callumveale.bjorneparken.HomeActivity.API_KEY;
import static callumveale.bjorneparken.HomeActivity.ROOT_URL;

public class ListSpeciesActivity extends AppCompatActivity {

    ViewGroup layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Build toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Retrieve view to which items will be added
        layout = (ViewGroup) findViewById(R.id.view);

        GetSpeciesTask task = new GetSpeciesTask(this);
        task.execute();
    }

    private void display(MainSpeciesListResponse response){

        if (response.getSpecies() != null) {

            for (MainSpeciesResponse speciesInstance : response.getSpecies()) {

                TextView textView = new TextView(this);
                textView.setTextSize(18);
                textView.setText(speciesInstance.getCommonName() + "\n" + speciesInstance.getLatin() + "\n" + speciesInstance.getDescription() + "\n\n");

                layout.addView(textView);
            }
        }
    }

    class GetSpeciesTask extends AsyncTask<Void, Void, MainSpeciesListResponse> {

        ListSpeciesActivity activity;

        GetSpeciesTask(ListSpeciesActivity activity){

            this.activity = activity;
        }

        @Override
        protected MainSpeciesListResponse doInBackground(Void... params) {

            Bjorneparkappen.Builder builder = new Bjorneparkappen.Builder(
                    AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);

            builder.setRootUrl(ROOT_URL);

            MainSpeciesListResponse response = new MainSpeciesListResponse();

            try {

                response = builder.build().species().list("en").setKey(API_KEY).execute();

            } catch (IOException e) {

                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(MainSpeciesListResponse response){

            activity.display(response);
        }
    }
}
