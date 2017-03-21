package com.callumveale.bjorneparken.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Event;
import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    //region Constants

    public static final String ARG_LIST = "list";
    public static final String ARG_VISIT_START = "visit-start";
    public static final String ARG_VISIT_END = "visit-end";

    //endregion Constants

    //region Properties

    private ArrayList<? extends Parcelable> mItinerary;
    private ListFragment.OnListItemSelectionListener mSelectedListener;
    private DateTime mVisitStart;
    private DateTime mVisitEnd;

    //endregion Properties

    //region Constructors

    public HomeFragment() {
        // Required empty public constructor
    }

    //endregion Constructors

    //region Methods

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param itinerary List of events
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(ArrayList<? extends Parcelable> itinerary, DateTime visitStart, DateTime visitEnd) {

        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_LIST, itinerary);
        args.putLong(ARG_VISIT_START, visitStart.getValue());
        args.putLong(ARG_VISIT_END, visitEnd.getValue());
        fragment.setArguments(args);
        return fragment;
    }

    //region Fragment Overridden Methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItinerary = getArguments().getParcelableArrayList(ARG_LIST);
            mVisitStart = new DateTime(getArguments().getLong(ARG_VISIT_START));
            mVisitEnd = new DateTime(getArguments().getLong(ARG_VISIT_END));
        }
    }


    private Event getNextEvent(ArrayList<Event> events){

        Event returnEvent = null;

        // Convert passed dates to calendars
        Calendar visitStart = Calendar.getInstance();
        visitStart.setTimeInMillis(mVisitStart.getValue());

        Calendar visitEnd = Calendar.getInstance();
        visitEnd.setTimeInMillis(mVisitEnd.getValue());
        visitEnd.add(Calendar.HOUR_OF_DAY, 23);
        visitEnd.add(Calendar.MINUTE, 59);


        Calendar now = Calendar.getInstance();

        // If visit is yet to start or in the past
        if (now.compareTo(visitStart) < 0 || now.compareTo(visitEnd) > 0){

            return returnEvent;
        }

        // Set shortest time until to 24 hours in the future
        long shortestTimeUntil = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);

        // For each event
        for (Event event : events){

            // Get exact start date
            Calendar eventStartTime = event.getEventStartCalendar(now, 0);

            // Calculate time until the event
            long timeUntil = eventStartTime.getTimeInMillis() - System.currentTimeMillis();

            // If the event is less than ten minutes in and the start time is nearer than the following event on the itinerary
            if ((timeUntil >= -600000) && (timeUntil < shortestTimeUntil)){

                // Update the shortest time until next event and event to return
                shortestTimeUntil = timeUntil;
                returnEvent = event;
            }
        }

        return returnEvent;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // If itinerary has no items
        if (mItinerary.size() < 0) {

            // Return unchanged default view (No items in itinerary message);
            return view;
        }

        // Attempt to retrieve the next event in the itinerary
        final Event event = getNextEvent((ArrayList<Event>) mItinerary);

        // If there is an event coming up
        if (event != null){

            // Retrieve and hide the no events text view
            TextView noEvents = (TextView) view.findViewById(R.id.home_empty);
            noEvents.setVisibility(View.GONE);

            Bitmap bitmap = event.getImage();

            // Set image
            if (bitmap != null){

                ImageView mImageView = (ImageView) view.findViewById(R.id.event_image);
                mImageView.setImageBitmap(bitmap);
                mImageView.setVisibility(View.VISIBLE);
            }

            // Retrieve and set the label text view
            TextView mLabelView = (TextView) view.findViewById(R.id.event_label);
            mLabelView.setText(event.getLabel());

            // Retrieve and set the location text view
            TextView mLocationView = (TextView) view.findViewById(R.id.event_location);
            mLocationView.setText(event.getLocation().getLabel());

            // Retrieve and set the time text view
            TextView mTimesView = (TextView) view.findViewById(R.id.event_start_time);
            mTimesView.setText(event.getStartTime());

            // Retrieve and set the description text view
            String description = event.getDescription();
            if (description.length() > 100) {
                description = description.substring(0, 95) + "...";
            }
            TextView mDescriptionView = (TextView) view.findViewById(R.id.event_description);
            mDescriptionView.setText(description);

            // Set listener for interaction with card
            LinearLayout eventCard = (LinearLayout) view.findViewById(R.id.event_card);

            // Add selected listener
            eventCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mSelectedListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mSelectedListener.onStarredItemSelection(event, ((HomeActivity) getActivity()).isInItinerary(event));
                    }
                }
            });


            // Get exact start time
            Calendar now = Calendar.getInstance();
            Calendar eventStartTime = event.getEventStartCalendar(now, 0);

            // If event underway
            if (eventStartTime.getTimeInMillis() - System.currentTimeMillis() <= 0){

                // Display now message, hiding event start time
                TextView upNextText = (TextView) view.findViewById(R.id.up_next_text);
                String nowText = getString(R.string.event_now) + ":";
                upNextText.setText(nowText);

                mTimesView.setVisibility(View.INVISIBLE);
            }

            // Show the up next view
            LinearLayout upNextView = (LinearLayout) view.findViewById(R.id.up_next_view);
            upNextView.setVisibility(View.VISIBLE);

        } else {

            // Display no more events for the day message
            TextView noEvents = (TextView) view.findViewById(R.id.home_empty);
            noEvents.setText(getString(R.string.no_more_events));
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListFragment.OnListItemSelectionListener && context instanceof DetailFragment.OnItemStarredListener) {
            mSelectedListener = (ListFragment.OnListItemSelectionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSelectedListener = null;
    }

    //endregion Fragment Overridden Methods

    //endregion Methods
}
