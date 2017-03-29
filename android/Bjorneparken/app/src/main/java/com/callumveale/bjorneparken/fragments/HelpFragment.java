package com.callumveale.bjorneparken.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.callumveale.bjorneparken.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HelpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HelpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpFragment extends Fragment {

    //region Constructors

    public HelpFragment() {
        // Required empty public constructor
    }

    //endregion Constructors

    //region Methods

    public static HelpFragment newInstance() {

        return new HelpFragment();
    }

    //region Fragment Overridden Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    //endregion Fragment Overridden Methods

    //endregion Methods
}
