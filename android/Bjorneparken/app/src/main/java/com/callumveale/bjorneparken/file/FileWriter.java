package com.callumveale.bjorneparken.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.callumveale.bjorneparken.config.Configuration;
import com.callumveale.bjorneparken.models.Amenity;
import com.callumveale.bjorneparken.models.Animal;
import com.callumveale.bjorneparken.models.Area;
import com.callumveale.bjorneparken.models.Event;
import com.callumveale.bjorneparken.models.Feeding;
import com.callumveale.bjorneparken.models.IModel;
import com.callumveale.bjorneparken.models.Species;
import com.callumveale.bjorneparken.notifications.Notification;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import none.bjorneparkappen_api.model.MainAnimalListResponse;
import none.bjorneparkappen_api.model.MainAreaListResponse;
import none.bjorneparkappen_api.model.MainEventListResponse;
import none.bjorneparkappen_api.model.MainSpeciesListResponse;

/**
 * Created by callum on 01/03/2017.
 */

public class FileWriter {

    //region Constants

    private static final String AREAS_FILE = "areas";
    private static final String CONFIG_FILE = "config";
    private static final String EVENTS_FILE = "events";
    private static final String FEEDINGS_FILE = "feedings";
    private static final String ITINERARY_FILE = "itinerary";
    private static final String NOTIFICATIONS_FILE = "notifications";
    private static final String SPECIES_FILE = "species";
    private static final String STARRED_SPECIES_FILE = "starred_species";
    private static final String VERSION_FILE = "version";
    private static final String VISITOR_ID_FILE = "visitor_id";
    private static final String VISIT_START_FILE = "visit_start";
    private static final String VISIT_END_FILE = "visit_start";

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

    //region Config

    public void writeConfigToFile(Configuration config){

        FileOutputStream outputStream;

        try {
            outputStream = mContext.openFileOutput(CONFIG_FILE, Context.MODE_PRIVATE);

            config.store(outputStream, CONFIG_FILE);

            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfigFromFile(){

        Configuration config = new Configuration();

        try {
            InputStream inputStream = mContext.openFileInput(VISITOR_ID_FILE);

            if (inputStream != null) {

                config.load(inputStream);

                inputStream.close();

            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return config;
    }

    //endregion Config

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

    //region Visit Dates

    //region Visit Start

    public void writeVisitStartDateToFile(DateTime visitStartDate){

        FileOutputStream outputStream;

        try {
            outputStream = mContext.openFileOutput(VISIT_START_FILE, Context.MODE_PRIVATE);
            outputStream.write(String.valueOf(visitStartDate.getValue()).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DateTime getVisitStartDateFromFile(){

        long visitStartDate = 0;

        try {
            InputStream inputStream = mContext.openFileInput(VISIT_START_FILE);

            if (inputStream != null) {

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                visitStartDate = Long.parseLong(bufferedReader.readLine());

                inputStream.close();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return new DateTime(visitStartDate);
    }

    //endregion Visit Start

    //region Visit End

    public void writeVisitEndDateToFile(DateTime visitEndDate){

        FileOutputStream outputStream;

        try {
            outputStream = mContext.openFileOutput(VISIT_END_FILE, Context.MODE_PRIVATE);
            outputStream.write(String.valueOf(visitEndDate.getValue()).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DateTime getVisitEndDateFromFile(){

        long visitEndDate = 0;

        try {
            InputStream inputStream = mContext.openFileInput(VISIT_END_FILE);

            if (inputStream != null) {

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                visitEndDate = Long.parseLong(bufferedReader.readLine());

                inputStream.close();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return new DateTime(visitEndDate);
    }

    //endregion Visit End

    //endregion Visit Dates

    //region Itinerary

    public void writeItineraryToFile(MainEventListResponse itineraryJson){

        writeJsonToFile(itineraryJson, ITINERARY_FILE);
    }

    public void writeItineraryToFile(ArrayList<Event> itinerary){

        MainEventListResponse itineraryJson = ResponseConverter.convertLocalEventList(itinerary);
        itineraryJson.setFactory(new JacksonFactory());

        writeItineraryToFile(itineraryJson);
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

    public void writeStarredSpeciesToFile(ArrayList<Species> species){

        MainSpeciesListResponse starredSpeciesJson = ResponseConverter.convertLocalSpeciesList(species);
        starredSpeciesJson.setFactory(new JacksonFactory());

        writeStarredSpeciesToFile(starredSpeciesJson);
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

    //region Events

    public void writeEventsToFile(MainEventListResponse eventsJson){

        writeJsonToFile(eventsJson, EVENTS_FILE);
    }

    public ArrayList<Event> getEventsFromFile(){

        MainEventListResponse events = new MainEventListResponse();

        try {
            InputStream inputStream = mContext.openFileInput(EVENTS_FILE);

            if (inputStream != null) {

                events = new JacksonFactory().fromInputStream(inputStream, MainEventListResponse.class);
                inputStream.close();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return ResponseConverter.convertEventListResponse(events);
    }

    //endregion Events

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

    public void writeAreasToFile(MainAreaListResponse areasJson){

        writeJsonToFile(areasJson, AREAS_FILE);
    }

    public ArrayList<Area> getAreasFromFile(){

        MainAreaListResponse areas = new MainAreaListResponse();

        try {
            InputStream inputStream = mContext.openFileInput(AREAS_FILE);

            if (inputStream != null) {

                areas = new JacksonFactory().fromInputStream(inputStream, MainAreaListResponse.class);
                inputStream.close();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return ResponseConverter.convertAreaListResponse(areas);
    }

    //endregion Areas

    //region Images

    public void getImageFromFile(IModel item){

        try {

            FileInputStream inputStream = new FileInputStream(new File(mContext.getFilesDir() + "/" + item.getClass().getSimpleName() + "-" + item.getId()));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            // Get image
            byte[] buffer = new byte[2048];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            inputStream.close();
            byteArrayOutputStream.close();

            // Get image byte array
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Load image into item
            Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            item.setImage(image);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //endregion Images

    //region Notifications

    public void writeNotificationsToFile(ArrayList<Notification> notifications){

        try {

            OutputStream outputStream = mContext.openFileOutput(NOTIFICATIONS_FILE, Context.MODE_PRIVATE);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);

            // Write out each notification to notifications file
            for (Notification notification : notifications){

                String line = notification.toString() + "\n";
                bos.write(line.getBytes());
            }

            bos.flush();
            bos.close();

            outputStream.close();

        } catch (IOException e){

            e.printStackTrace();
        }

    }

    public ArrayList<Notification> getNotificationsFromFile(){

        ArrayList<Notification> notifications = new ArrayList<>();

        try {

            // Read in all notifications from the notifications file
            InputStream inputStream = mContext.openFileInput(NOTIFICATIONS_FILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;

            while((line = reader.readLine()) != null){

                String[] tokens = line.split(",");

                if (tokens.length > 0) {

                    Notification notification = new Notification(Integer.parseInt(tokens[0]), Long.parseLong(tokens[1]), Long.parseLong(tokens[2]), Long.parseLong(tokens[3]));
                    notifications.add(notification);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return notifications;
    }


    //endregion Notifications

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
