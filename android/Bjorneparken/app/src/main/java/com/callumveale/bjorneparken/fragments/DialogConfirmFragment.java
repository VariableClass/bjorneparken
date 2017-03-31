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

    public static final String ARG_TITLE = "title";
    public static final String ARG_MESSAGE = "message";
    public static final String ARG_POSITION = "position";
    public static final String ARG_VISUAL_UNSTAR = "visual-unstar";

    //endregion Constants

    //region Properties

    private int mPosition;
    private int mTitleResourceId;
    private int mMessageResourceId;
    private String mTitle;
    private String mMessage;
    private boolean mVisualUnstarOnly;
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

    public static DialogConfirmFragment newInstance(int titleResourceId, int messageResourceId, int position, boolean visualUnstarOnly) {

        DialogConfirmFragment fragment = new DialogConfirmFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE, titleResourceId);
        args.putInt(ARG_MESSAGE, messageResourceId);
        args.putInt(ARG_POSITION, position);
        args.putBoolean(ARG_VISUAL_UNSTAR, visualUnstarOnly);
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogConfirmFragment newInstance(int titleResourceId, int messageResourceId, int position) {

        DialogConfirmFragment fragment = new DialogConfirmFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE, titleResourceId);
        args.putInt(ARG_MESSAGE, messageResourceId);
        args.putInt(ARG_POSITION, position);
        args.putBoolean(ARG_VISUAL_UNSTAR, false);
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogConfirmFragment newInstance(int titleResourceId, int messageResourceId, boolean visualUnstarOnly) {

        DialogConfirmFragment fragment = new DialogConfirmFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE, titleResourceId);
        args.putInt(ARG_MESSAGE, messageResourceId);
        args.putBoolean(ARG_VISUAL_UNSTAR, visualUnstarOnly);
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogConfirmFragment newInstance(int titleResourceId, int messageResourceId) {

        DialogConfirmFragment fragment = new DialogConfirmFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE, titleResourceId);
        args.putInt(ARG_MESSAGE, messageResourceId);
        args.putBoolean(ARG_VISUAL_UNSTAR, false);
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogConfirmFragment newInstance(String title, String message, int position) {

        DialogConfirmFragment fragment = new DialogConfirmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putInt(ARG_POSITION, position);
        args.putBoolean(ARG_VISUAL_UNSTAR, false);
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogConfirmFragment newInstance(String title, String message) {

        DialogConfirmFragment fragment = new DialogConfirmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putBoolean(ARG_VISUAL_UNSTAR, false);
        fragment.setArguments(args);
        return fragment;
    }

    //region Fragment Overridden Methods

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_POSITION);

            // If string title is null, retrieve resource IDs
            if (getArguments().getString(ARG_TITLE) == null) {

                mTitleResourceId = getArguments().getInt(ARG_TITLE);
                mMessageResourceId = getArguments().getInt(ARG_MESSAGE);

            } else {

                mTitle = getArguments().getString(ARG_TITLE);
                mMessage = getArguments().getString(ARG_MESSAGE);
            }

            mVisualUnstarOnly = getArguments().getBoolean(ARG_VISUAL_UNSTAR);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (mTitle == null) {

            builder.setTitle(mTitleResourceId)
                    .setMessage(mMessageResourceId);
        } else {

            builder.setTitle(mTitle)
                    .setMessage(mMessage);
        }

        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mListItemListener != null) {

                            mListItemListener.unstarItem(mPosition, mVisualUnstarOnly);
                        }

                        if (mDetailListener != null) {

                            mDetailListener.unstar(mVisualUnstarOnly);
                        }
                        dialogInterface.dismiss();
                    }
                });

        // If performing a superficial unstar, do not allow cancel
        if (!mVisualUnstarOnly) {

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

        }

        // Build message
        builder.create();

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

        void unstarItem(int position, boolean visualUnstarOnly);
    }

    public interface OnUnstarDetailListener {

        void unstar(boolean visualUnstarOnly);
    }

    //endregion Interfaces
}