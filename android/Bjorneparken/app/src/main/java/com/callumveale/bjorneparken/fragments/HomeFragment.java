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
import com.callumveale.bjorneparken.models.Event;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    public static final String ARG_LIST = "list";

    private List<Event> mItinerary;

    private OnItemSelectionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param itinerary List of events
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(ArrayList<Parcelable> itinerary) {

        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_LIST, itinerary);
        fragment.setArguments(args);
        return fragment;
    }

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

        TextView emptyView = (TextView) view.findViewById(R.id.home_empty);

        if (mItinerary.size() > 0) {

            // Hide the empty text label
            emptyView.setVisibility(View.GONE);

            // Create up next card
            TextView mLabelView = (TextView) view.findViewById(R.id.event_label);
            mLabelView.setText(mItinerary.get(0).getLabel());

            TextView mLocationView = (TextView) view.findViewById(R.id.event_location);
            mLocationView.setText(mItinerary.get(0).getLocation().getLabel());

            TextView mTimesView = (TextView) view.findViewById(R.id.event_start_time);
            mTimesView.setText(mItinerary.get(0).getStartTime());

            String description = mItinerary.get(0).getDescription();
            if (description.length() > 100) {
                description = description.substring(0, 95) + "...";
            }

            TextView mDescriptionView = (TextView) view.findViewById(R.id.event_description);
            mDescriptionView.setText(description);

            // Show the up next view
            LinearLayout upNextView = (LinearLayout) view.findViewById(R.id.up_next_view);
            upNextView.setVisibility(View.VISIBLE);

            // Set listener for interaction with card
            LinearLayout eventCard = (LinearLayout) view.findViewById(R.id.event_card);

            eventCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onItemSelection(mItinerary.get(0));
                    }
                }
            });

        } else {


        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectionListener) {
            mListener = (OnItemSelectionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnItemSelectionListener {

        void onItemSelection(Event event);
    }
}
