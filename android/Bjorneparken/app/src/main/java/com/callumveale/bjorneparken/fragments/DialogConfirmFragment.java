package com.callumveale.bjorneparken.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.callumveale.bjorneparken.R;

/**
 * Created by callum on 04/03/2017.
 */
public class DialogConfirmFragment extends DialogFragment {

    //region Constants

    public static final String ARG_POSITION = "position";

    //endregion Constants

    //region Properties

    private int mPosition;
    private OnUnstarListItemListener mListItemListener;
    private OnUnstarDetailListener mDetailListener;

    //endregion Properties

    //region Constructors

    public DialogConfirmFragment(){}

    //endregion Constructors

    //region Methods

    public static DialogConfirmFragment newInstance() {

        return new DialogConfirmFragment();
    }

    public static DialogConfirmFragment newInstance(int position) {

        DialogConfirmFragment fragment = new DialogConfirmFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    //region Fragment Overridden Methods

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.confirm_unstar_species_title)
                .setMessage(R.string.confirm_unstar_species_message)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        if (mListItemListener != null) {

                            mListItemListener.unstarItem(mPosition);
                        }

                        if (mDetailListener != null) {

                            mDetailListener.unstar();
                        }
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create();

        return builder.show();
    }

    public void setUnstarListItemListener(OnUnstarListItemListener listItemListener){

        mListItemListener = listItemListener;
        mDetailListener = null;
    }

    public void setUnstarDetailListener(OnUnstarDetailListener detailListener){

        mDetailListener = detailListener;
        mListItemListener = null;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mListItemListener = null;
        mDetailListener = null;
    }

    //endregion Fragment Overridden Methods

    //endregion Methods

    //region Interfaces

    public interface OnUnstarListItemListener {

        void unstarItem(int position);
    }

    public interface OnUnstarDetailListener {

        void unstar();
    }

    //endregion Interfaces
}