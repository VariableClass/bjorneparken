package com.callumveale.bjorneparken.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.Feeding;

import java.util.ArrayList;

/**
 * Created by callum on 04/03/2017.
 */
public class DialogListFragment extends DialogFragment {

    //region Constants

    public static final String ARG_OPTIONS = "options";

    //endregion Constants

    //region Properties

    private ArrayList<Feeding> mOptions;
    private DetailFragment.OnItemStarredListener mListener;

    //endregion Properties

    //region Constructors

    public DialogListFragment(){}

    //endregion Constructors

    //region Methods

    public static DialogListFragment newInstance(ArrayList<Feeding> options) {

        DialogListFragment fragment = new DialogListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_OPTIONS, options);
        fragment.setArguments(args);
        return fragment;
    }

    //region Fragment Overridden Methods

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOptions = getArguments().getParcelableArrayList(ARG_OPTIONS);
        }
    }

    private String[] getStringArray(ArrayList<Feeding> feedings){

        String[] stringArray = new String[feedings.size()];

        for (int i = 0; i < feedings.size(); i++){

            Feeding feeding = feedings.get(i);

            stringArray[i] = feeding.getStartTime() + ": " + feeding.getHeader();
        }

        return stringArray;
    }

    private boolean[] getSelectedItems(ArrayList<Feeding> feedings){

        boolean[] selectedItems = new boolean[feedings.size()];

        for (int i = 0; i < feedings.size(); i++){

            selectedItems[i] = ((HomeActivity) getActivity()).isInItinerary(feedings.get(i));
        }

        return selectedItems;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String[] options = getStringArray(mOptions);
        boolean[] selected = getSelectedItems(mOptions);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_feeding)
                .setMultiChoiceItems(options, selected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                        mListener.onItemStarred(mOptions.get(i));
                    }
                })
                .setNeutralButton(R.string.close_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof DetailFragment.OnItemStarredListener) {
            mListener = (DetailFragment.OnItemStarredListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnItemStarredListener");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mListener = null;
    }

    //endregion Fragment Overridden Methods

    //endregion Methods
}