package callumveale.bjorneparken;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;

import none.bjorneparkappen.Bjorneparkappen;
import none.bjorneparkappen.model.MainAnimalResponse;

public class HomeActivity extends AppCompatActivity {

    public final static String SEND_TEXT = "callumveale.bjorneparken.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Build toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {

        GetSuccessTask task = new GetSuccessTask();

        task.execute(this);
    }

    public void intend(String result){

        // Create intent using DisplayMessageActivity
        Intent intent = new Intent(this, DisplayMessageActivity.class);

        // Retrieve edit text box
        EditText editText = (EditText) findViewById(R.id.edit_message);

        // Retrieve text from text box
        String message = editText.getText().toString();

        // Attach text to intent
        intent.putExtra(SEND_TEXT, result);

        // Start intent
        startActivity(intent);
    }
}

class GetSuccessTask extends AsyncTask<HomeActivity, Void, String> {

    private HomeActivity[] activities = new HomeActivity[1];

    private static final String API_KEY = "<api_key>";

    @Override
    protected String doInBackground(HomeActivity... homes) {

        for (HomeActivity home : homes) {
            activities[0] = home;
        }

        Bjorneparkappen.Builder builder = new Bjorneparkappen.Builder(
                AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);

        MainAnimalResponse animals = null;

        try {
            animals = builder.build().animals().list().setKey(API_KEY).execute();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return animals.getContent();
    }

    @Override
    protected void onPostExecute(String result) {

       activities[0].intend(result);
    }
}
