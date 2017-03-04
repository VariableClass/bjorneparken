package com.callumveale.bjorneparken.file;

import android.content.Context;

import com.callumveale.bjorneparken.models.Amenity;
import com.callumveale.bjorneparken.models.Event;
import com.callumveale.bjorneparken.models.Feeding;
import com.callumveale.bjorneparken.models.Species;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import none.bjorneparkappen_api.model.MainAreaListResponse;
import none.bjorneparkappen_api.model.MainEventListResponse;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;

/**
 * Created by callum on 01/03/2017.
 */

public class FileWriter {

    //region Constants

    private static final String AMENITIES_FILE = "amenities";
    private static final String ATTRACTIONS_FILE = "attractions";
    private static final String FEEDINGS_FILE = "feedings";
    private static final String ITINERARY_FILE = "itinerary";
    private static final String SPECIES_FILE = "species";
    private static final String STARRED_SPECIES_FILE = "starred_species";
    private static final String VERSION_FILE = "version";
    private static final String VISITOR_ID_FILE = "visitor_id";

    //endregion Constants

    //region Properties

    private Context mContext;

    //endregion Properties

    //region Constructors

    public FileWriter(Context context){

        this.mContext = context;
    }

    //endregion Constructors

    //region Methods

    //region Visitor

    //region Visitor ID

    public void writeIdToFile(long visitorId){

        FileOutputStream outputStream;

        try {
            outputStream = mContext.openFileOutput(VISITOR_ID_FILE, Context.MODE_PRIVATE);
            outputStream.write(String.valueOf(visitorId).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getIdFromFile(){

        long visitorId = 0;

        try {
            InputStream inputStream = mContext.openFileInput(VISITOR_ID_FILE);

            if (inputStream != null) {

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                visitorId = Long.parseLong(bufferedReader.readLine());

                inputStream.close();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return visitorId;
    }

    //endregion Visitor ID

    //region Itinerary

    public void writeItineraryToFile(MainEventListResponse itineraryJson){

        writeJsonToFile(itineraryJson, ITINERARY_FILE);
    }

    public ArrayList<Event> getItineraryFromFile(){

        MainEventListResponse itinerary = new MainEventListResponse();

        try {
            InputStream inputStream = mContext.openFileInput(ITINERARY_FILE);

            if (inputStream != null) {

                itinerary = new JacksonFactory().fromInputStream(inputStream, MainEventListResponse.class);
                inputStream.close();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return ResponseConverter.convertEventListResponse(itinerary);
    }

    //endregion Itinerary

    //region Starred Species

    public void writeStarredSpeciesToFile(MainSpeciesListResponse starredSpeciesJson){

        writeJsonToFile(starredSpeciesJson, STARRED_SPECIES_FILE);
    }

    public ArrayList<Species> getStarredSpeciesFromFile(){

        MainSpeciesListResponse starredSpecies = new MainSpeciesListResponse();

        try {
            InputStream inputStream = mContext.openFileInput(STARRED_SPECIES_FILE);

            if (inputStream != null) {

                starredSpecies = new JacksonFactory().fromInputStream(inputStream, MainSpeciesListResponse.class);
                inputStream.close();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return ResponseConverter.convertSpeciesListResponse(starredSpecies);
    }

    //endregion StarredSpecies

    //endregion Visitor

    //region Version

    public void writeVersionToFile(DateTime version){

        FileOutputStream outputStream;

        try {
            outputStream = mContext.openFileOutput(VERSION_FILE, Context.MODE_PRIVATE);
            outputStream.write(version.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DateTime getVersionFromFile(){

        DateTime version = null;

        try {
            InputStream inputStream = mContext.openFileInput(VERSION_FILE);

            if (inputStream != null) {

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                version = new DateTime(bufferedReader.readLine());

                inputStream.close();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return version;
    }

    //endregion Version

    //region Species

    public void writeSpeciesToFile(MainSpeciesListResponse speciesJson){

       writeJsonToFile(speciesJson, SPECIES_FILE);
    }

    public ArrayList<Species> getSpeciesFromFile(){

        MainSpeciesListResponse species = new MainSpeciesListResponse();

        try {
            InputStream inputStream = mContext.openFileInput(SPECIES_FILE);

            if (inputStream != null) {

                species = new JacksonFactory().fromInputStream(inputStream, MainSpeciesListResponse.class);
                inputStream.close();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return ResponseConverter.convertSpeciesListResponse(species);
    }

    //endregion Species

    //region Events

    //region Feedings

    public void writeFeedingsToFile(MainEventListResponse feedingsJson){

        writeJsonToFile(feedingsJson, FEEDINGS_FILE);
    }

    public ArrayList<Feeding> getFeedingsFromFile(){

        MainEventListResponse feedings = new MainEventListResponse();

        try {
            InputStream inputStream = mContext.openFileInput(FEEDINGS_FILE);

            if (inputStream != null) {

                feedings = new JacksonFactory().fromInputStream(inputStream, MainEventListResponse.class);
                inputStream.close();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return ResponseConverter.convertFeedingListResponse(feedings);
    }

    //endregion Feedings

    //endregion Events

    //region Areas

    //region Amenities

    public void writeAmenitiesToFile(MainAreaListResponse amenitiesJson){

        writeJsonToFile(amenitiesJson, AMENITIES_FILE);
    }

    public ArrayList<Amenity> getAmenitiesFromFile(){

        MainAreaListResponse amenities = new MainAreaListResponse();

        try {
            InputStream inputStream = mContext.openFileInput(AMENITIES_FILE);

            if (inputStream != null) {

                amenities = new JacksonFactory().fromInputStream(inputStream, MainAreaListResponse.class);
                inputStream.close();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return ResponseConverter.convertAmenityListResponse(amenities);
    }

    public void writeAttractionsToFile(MainAreaListResponse attractionsJson){

        writeJsonToFile(attractionsJson, ATTRACTIONS_FILE);
    }

    public ArrayList<Amenity> getAttractionsFromFile(){

        MainAreaListResponse attractions = new MainAreaListResponse();

        try {
            InputStream inputStream = mContext.openFileInput(ATTRACTIONS_FILE);

            if (inputStream != null) {

                attractions = new JacksonFactory().fromInputStream(inputStream, MainAreaListResponse.class);
                inputStream.close();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return ResponseConverter.convertAmenityListResponse(attractions);
    }

    //endregion Amenities

    //endregion Areas

    private void writeJsonToFile(GenericJson jsonToWrite, String filename){

        FileOutputStream outputStream;

        try {
            outputStream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(jsonToWrite.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion Methods
}
