package com.callumveale.bjorneparken.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by callum on 27/02/2017.
 */

public class Event implements IModel, Parcelable{

    //region Constants

    public static final Creator CREATOR = new Creator() {

        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    //endregion Constants

    //region Properties

    private long id;
    private String label;
    private String description;
    private Area location;
    private String startTime;
    private String endTime;
    private boolean isActive;

    //endregion Properties

    //region Constructors

    public Event(long id, String label, String description, Area location, String startTime, String endTime, boolean isActive){
        this.id = id;
        this.label = label;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
    }

    // Parcelling part
    public Event(Parcel in){

        this.id = in.readLong();

        String[] data = new String[4];
        in.readStringArray(data);
        this.label = data[0];
        this.description = data[1];
        this.startTime = data[2];
        this.endTime = data[3];

        this.location = in.readParcelable(Amenity.class.getClassLoader());

        boolean[] boolArray = new boolean[1];
        in.readBooleanArray(boolArray);
        this.isActive = boolArray[0];
    }

    public Event(){}

    //endregion Constructors

    //region Methods

    public boolean conflictsWith(Event compareEvent){

        Calendar now = Calendar.getInstance();

        Calendar thisStart = getEventStartCalendar(now, 0);
        Calendar thisEnd = getEventEndCalendar(now, 0);

        Calendar compareStart = compareEvent.getEventStartCalendar(now, 0);
        Calendar compareEnd = compareEvent.getEventEndCalendar(now, 0);

        // If this event's end time after the passed event's start time
        if (thisEnd.compareTo(compareStart) > 0){

            // If this event's start time before the passed event's end time
            if (thisStart.compareTo(compareEnd) < 0){

                return true;
            }
        }

        // If this event's start time before the passed event's end time
        if (thisStart.compareTo(compareEnd) < 0){

            // If this event's end time after the passed event's start time
            if (thisEnd.compareTo(compareStart) > 0) {

                return true;
            }
        }

        return false;
    }

    private Calendar getEventTime(Calendar visitStart, int daysDelta, int hour, int minute){

        // Create a new Calendar set to the visit start date
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(visitStart.getTime());

        // Add 1 day until we reach the currently processing date
        timeCalendar.add(Calendar.HOUR, (24 * daysDelta));

        // Set the hour and minute to the event time
        timeCalendar.set(Calendar.HOUR_OF_DAY, hour);
        timeCalendar.set(Calendar.MINUTE, minute);

        return timeCalendar;
    }

    public Calendar getEventStartCalendar(Calendar visitStart, int daysDelta){

        // Retrieve the event start time
        int startHour = Integer.parseInt(startTime.substring(0, 2));
        int startMinute = Integer.parseInt(startTime.substring(3, 5));

        // Return calendar instance
        return getEventTime(visitStart, daysDelta, startHour, startMinute);
    }

    public Calendar getEventEndCalendar(Calendar visitStart, int daysDelta){

        // Retrieve the event end time
        int endHour = Integer.parseInt(endTime.substring(0, 2));
        int endMinute = Integer.parseInt(endTime.substring(3, 5));

        // Return calendar instance
        return getEventTime(visitStart, daysDelta, endHour, endMinute);
    }

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public Area getLocation() {
        return location;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(Area location) {
        this.location = location;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    //region IModel Overridden Methods

    @Override
    public String getHeader() {
        return this.label;
    }

    @Override
    public String getSubheader() {
        return this.startTime + " - " + this.endTime;
    }

    @Override
    public String getCaption() {
        return this.location.getLabel();
    }

    @Override
    public String getSubcaption() {
        return null;
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    @Override
    public Bitmap getImage() {
        return null;
    }

    @Override
    public void setImage(Bitmap bitmap) {
        return;
    }

    //endregion IModel Overridden Methods

    //region Parcelable Overridden Methods

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeStringArray(new String[]{this.label, this.description, this.startTime, this.endTime});

        if (this.location.getClass() == Amenity.class) {
            dest.writeParcelable((Amenity) this.location, 0);

        } else if (this.location.getClass() == Enclosure.class){

            dest.writeParcelable((Enclosure) this.location, 0);
        }


        dest.writeBooleanArray(new boolean[]{this.isActive});
    }

    //endregion Parcelable Overridden Methods

    //endregion Methods

    //region Interfaces

    public static class EventTimeComparator implements Comparator<Event> {
        @Override
        public int compare(Event event1, Event event2) {

            Calendar now = Calendar.getInstance();

            return event1.getEventStartCalendar(now, 0).compareTo(event2.getEventStartCalendar(now, 0));
        }
    }

    //endregion Interfaces
}