package com.callumveale.bjorneparken.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Event;

import java.util.ArrayList;

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

    //endregion Constants

    //region Properties

    private ArrayList<? extends Parcelable> mItinerary;
    private ListFragment.OnListItemSelectionListener mSelectedListener;
    private DetailFragment.OnItemStarredListener mStarredListener;

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
    public static HomeFragment newInstance(ArrayList<? extends Parcelable> itinerary) {

        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_LIST, itinerary);
        fragment.setArguments(args);
        return fragment;
    }

    //region Fragment Overridden Methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItinerary = getArguments().getParcelableArrayList(ARG_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // If itinerary has items
        if (mItinerary.size() > 0) {

            final Event event = (Event) mItinerary.get(0);

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

            // Retrieve and hide the no events text view
            TextView noEvents = (TextView) view.findViewById(R.id.home_empty);
            noEvents.setVisibility(View.GONE);

            // Show the up next view
            LinearLayout upNextView = (LinearLayout) view.findViewById(R.id.up_next_view);
            upNextView.setVisibility(View.VISIBLE);

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

        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListFragment.OnListItemSelectionListener && context instanceof DetailFragment.OnItemStarredListener) {
            mSelectedListener = (ListFragment.OnListItemSelectionListener) context;
            mStarredListener = (DetailFragment.OnItemStarredListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener and OnItemStarredListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSelectedListener = null;
        mStarredListener = null;
    }

    //endregion Fragment Overridden Methods

    //endregion Methods
}
