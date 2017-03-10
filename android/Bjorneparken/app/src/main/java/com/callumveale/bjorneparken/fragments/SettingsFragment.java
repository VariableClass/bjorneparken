package com.callumveale.bjorneparken.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.config.Configuration;
import com.callumveale.bjorneparken.models.Event;
import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    //region Constants

    public static final String ARG_NOTIFICATIONS = "notifications-enabled";

    //endregion Constants

    //region Properties

    private boolean mNotificationsEnabled;
    private OnNotificationsChangedListener mNotificationsListener;

    //endregion Properties

    //region Constructors

    public SettingsFragment() {
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
    public static SettingsFragment newInstance(Configuration config) {

        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_NOTIFICATIONS, Boolean.valueOf(config.getProperty(Configuration.NOTIFICATIONS_ENABLED)));
        fragment.setArguments(args);
        return fragment;
    }

    //region Fragment Overridden Methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNotificationsEnabled = getArguments().getBoolean(ARG_NOTIFICATIONS);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Retrieve notifications toggle button
        Switch notificationsToggle = (Switch) view.findViewById(R.id.notifications_toggle);
        notificationsToggle.setChecked(mNotificationsEnabled);

        // Add selected listener
        notificationsToggle.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mNotificationsListener.onNotificationsEnabledChanged(b);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNotificationsChangedListener) {
            mNotificationsListener = (OnNotificationsChangedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onNotificationsChangedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNotificationsListener = null;
    }

    //endregion Fragment Overridden Methods

    //endregion Methods

    //region Interfaces

    public interface OnNotificationsChangedListener{

        void onNotificationsEnabledChanged(boolean enabled);
    }

    //endregion Interfaces
}
