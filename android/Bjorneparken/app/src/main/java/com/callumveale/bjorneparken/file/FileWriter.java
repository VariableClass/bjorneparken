package com.callumveale.bjorneparken.file;

import android.content.Context;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import none.bjorneparkappen_api.model.MainAreaListResponse;
import none.bjorneparkappen_api.model.MainEventListResponse;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;

/**
 * Created by callum on 01/03/2017.
 */

public class FileWriter {


    private static final String VISITOR_ID_FILE = "visitor_id";
    private static final String ITINERARY_FILE = "itinerary";
    private static final String VERSION_FILE = "version";
    private static final String SPECIES_FILE = "species";
    private static final String AMENITIES_FILE = "amenities";
    private static final String ATTRACTIONS_FILE = "attractions";


    private Context context;


    public FileWriter(Context context){

        this.context = context;
    }


    public void writeIdToFile(long visitorId){

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(VISITOR_ID_FILE, Context.MODE_PRIVATE);
            outputStream.write(String.valueOf(visitorId).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getIdFromFile(){

        long visitorId = 0;

        try {
            InputStream inputStream = context.openFileInput(VISITOR_ID_FILE);

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

    public void writeVersionToFile(DateTime version){

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(VERSION_FILE, Context.MODE_PRIVATE);
            outputStream.write(version.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DateTime getVersionFromFile(){

        DateTime version = null;

        try {
            InputStream inputStream = context.openFileInput(VERSION_FILE);

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

    public void writeSpeciesToFile(MainSpeciesListResponse speciesJson){

       writeJsonToFile(speciesJson, SPECIES_FILE);
    }

    public MainSpeciesListResponse getSpeciesFromFile(){

        MainSpeciesListResponse species = new MainSpeciesListResponse();

        try {
            InputStream inputStream = context.openFileInput(SPECIES_FILE);

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

        return species;
    }

    public void writeAmenitiesToFile(MainAreaListResponse amenitiesJson){

        writeJsonToFile(amenitiesJson, AMENITIES_FILE);
    }

    public MainAreaListResponse getAmenitiesFromFile(){

        MainAreaListResponse amenities = new MainAreaListResponse();

        try {
            InputStream inputStream = context.openFileInput(AMENITIES_FILE);

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

        return amenities;
    }

    public void writeAttractionsToFile(MainAreaListResponse attractionsJson){

        writeJsonToFile(attractionsJson, ATTRACTIONS_FILE);
    }

    public MainAreaListResponse getAttractionsFromFile(){

        MainAreaListResponse attractions = new MainAreaListResponse();

        try {
            InputStream inputStream = context.openFileInput(ATTRACTIONS_FILE);

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

        return attractions;
    }

    public void writeItineraryToFile(MainEventListResponse itineraryJson){

        writeJsonToFile(itineraryJson, ITINERARY_FILE);
    }

    public MainEventListResponse getItineraryFromFile(){

        MainEventListResponse itinerary = new MainEventListResponse();

        try {
            InputStream inputStream = context.openFileInput(ITINERARY_FILE);

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

        return itinerary;
    }

    private void writeJsonToFile(GenericJson jsonToWrite, String filename){

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(jsonToWrite.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
