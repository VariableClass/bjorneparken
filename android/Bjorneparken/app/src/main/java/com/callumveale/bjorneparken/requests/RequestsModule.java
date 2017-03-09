package com.callumveale.bjorneparken.requests;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Event;
import com.callumveale.bjorneparken.models.Species;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Locale;

import none.bjorneparkappen_api.BjorneparkappenApi;

/**
 * Created by callum on 27/02/2017.
 */
public class RequestsModule {

    //region Constants

    private static final String HOST = "api-dot-bjorneparkappen.appspot.com";
    private static final String ROOT_URL = "https://" + HOST + "/_ah/api/";

    //region Request Constants

    static final String API_KEY = "AIzaSyCuD2dk_XFcn512V5JxAZbFlAK9dgNlQ9c";

    static final String EVENT_ID = "event_id";
    static final String LANGUAGE_CODE = "language_code";
    static final String LOCATION_ID = "location_id";
    static final String SPECIES_ID = "species_id";
    static final String VISITOR_ID = "visitor_id";

    //endregion Request Constants

    //endregion Constants

    //region Properties

    private BjorneparkappenApi.Builder mBuilder;
    private HomeActivity mActivity;
    private String mLanguage;

    //endregion Properties

    //region Constructors

    public RequestsModule(String applicationName, HomeActivity activity){

        mBuilder = new BjorneparkappenApi.Builder(
                AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);
        mBuilder.setRootUrl(RequestsModule.ROOT_URL);
        mBuilder.setApplicationName(applicationName);

        mActivity = activity;
        mLanguage = Locale.getDefault().getLanguage();
    }

    //endregion Constructors

    //region Methods

    public void checkServerAvailability(){

        CheckConnectionTask checkConnection = new CheckConnectionTask(HOST, mActivity);
        checkConnection.execute();
    }

    //region GET Requests

    public void getAllAmenities(){

        GetAllAmenitiesTask getAllAmenities = new GetAllAmenitiesTask(mBuilder, mActivity, mLanguage);
        getAllAmenities.execute();
    }

    public void getAllAttractions(){

        GetAllAttractionsTask getAllAttractions = new GetAllAttractionsTask(mBuilder, mActivity, mLanguage);
        getAllAttractions.execute();
    }

    public void getAllSpecies(){

        GetAllSpeciesTask getAllSpecies = new GetAllSpeciesTask(mBuilder, mActivity, mLanguage);
        getAllSpecies.execute();
    }

    public void getAllFeedings(){

        GetAllFeedingsTask getAllFeedings = new GetAllFeedingsTask(mBuilder, mActivity, mLanguage);
        getAllFeedings.execute();
    }

    public void getVersion(){

        GetVersionTask getVersion = new GetVersionTask(mBuilder, mActivity);
        getVersion.execute();
    }

    public void getVisitorId(DateTime visitStart, DateTime visitEnd){

        GetVisitorIdTask getVisitorId = new GetVisitorIdTask(mBuilder, mActivity, visitStart, visitEnd);
        getVisitorId.execute();
    }

    public void getItinerary(long visitorId){

        GetVisitorItineraryTask getVisitorItinerary = new GetVisitorItineraryTask(mBuilder, mActivity, mLanguage, visitorId);
        getVisitorItinerary.execute();
    }

    public void getStarredSpecies(long visitorId){

        GetVisitorStarredSpeciesTask getVisitorStarredSpecies = new GetVisitorStarredSpeciesTask(mBuilder, mActivity, mLanguage, visitorId);
        getVisitorStarredSpecies.execute();
    }

    //endregion GET Requests

    //region POST Requests

    public void addToItinerary(long visitorId, Event event){

        AddToItineraryTask addToItinerary = new AddToItineraryTask(mBuilder, mActivity, mLanguage, visitorId, event);
        addToItinerary.execute();
    }

    public void removeFromItinerary(long visitorId, Event event){

        RemoveFromItineraryTask removeFromItinerary = new RemoveFromItineraryTask(mBuilder, mActivity, mLanguage, visitorId, event);
        removeFromItinerary.execute();
    }

    public void starSpecies(long visitorId, Species species){

        StarSpeciesTask starSpecies = new StarSpeciesTask(mBuilder, mActivity, mLanguage, visitorId, species);
        starSpecies.execute();
    }

    public void unstarSpecies(long visitorId, Species species){

        UnstarSpeciesTask unstarSpecies = new UnstarSpeciesTask(mBuilder, mActivity, mLanguage, visitorId, species);
        unstarSpecies.execute();
    }

    //endregion POST Requests

    //endregion Methods
}
