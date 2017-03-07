package com.callumveale.bjorneparken.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.models.IModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    //region Constants

    public static final String ARG_ITEM = "item";
    public static final String ARG_STARRED = "starred";

    //endregion Constants

    //region Properties

    private Parcelable mItem;
    private Boolean isStarred;
    private OnItemStarredListener mListener;

    //endregion Properties

    //region Constructors

    public DetailFragment() {
        // Required empty public constructor
    }

    //endregion Constructors

    //region Methods

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(Parcelable item, boolean starred) {

        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, item);
        args.putBooleanArray(ARG_STARRED, new boolean[]{starred});
        fragment.setArguments(args);
        return fragment;
    }

    public static DetailFragment newInstance(Parcelable item) {

        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    //region Fragment Overridden Methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = getArguments().getParcelable(ARG_ITEM);
            boolean[] starredArray = getArguments().getBooleanArray(ARG_STARRED);
            if (starredArray != null){
                isStarred = starredArray[0];
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Retrieve item from which to populate the view
        IModel item = (IModel) mItem;

        if (item.getImageBytes() != null){


        }

        // Set image
        byte[] imageBytes = item.getImageBytes();
        if (imageBytes != null){

            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            ImageView imageView = (ImageView) view.findViewById(R.id.detail_image);
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
        }

        final ImageView star = (ImageView) view.findViewById(R.id.detail_star);

        // If no value for is starred
        if (isStarred == null){

            // Hide the star
            star.setVisibility(View.GONE);

        } else {

            // Else, if item is starred
            if (isStarred) {

                // Set image to starred
                star.setImageResource(R.drawable.star_selected);
                star.setContentDescription(getString(R.string.starred));

            } else {

                // Set image to unstarred
                star.setImageResource(R.drawable.star_unselected);
                star.setContentDescription(getString(R.string.unstarred));
            }
        }

        // Retrieve and set header text view
        TextView mHeaderView = (TextView) view.findViewById(R.id.detail_header);
        mHeaderView.setText(item.getHeader());

        // Retrieve and set description text view
        TextView mDescriptionView = (TextView) view.findViewById(R.id.detail_description);
        mDescriptionView.setText(item.getDescription());

        // If item has a subheader
        if (item.getSubheader() != null){

            // Retrieve, set and show subheader text view
            TextView mSubheaderView = (TextView) view.findViewById(R.id.detail_subheader);
            mSubheaderView.setText(item.getSubheader());
            mSubheaderView.setVisibility(View.VISIBLE);
        }

        // If item has a caption
        if (item.getCaption() != null){

            // Retrieve, set and show caption text view
            TextView mCaptionView = (TextView) view.findViewById(R.id.detail_caption);
            mCaptionView.setText(item.getCaption());
            mCaptionView.setVisibility(View.VISIBLE);
        }

        // If item has a subcaption
        if (item.getSubcaption() != null){

            // Retrieve, set and show subcaption text view
            TextView mSubcaptionView = (TextView) view.findViewById(R.id.detail_subcaption);
            mSubcaptionView.setText(item.getSubcaption());
            mSubcaptionView.setVisibility(View.VISIBLE);
        }

        // Add starred listener
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mListener) {

                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    isStarred = !isStarred;
                    if (isStarred) {

                        // Set image to starred
                        star.setImageResource(R.drawable.star_selected);
                        star.setContentDescription(getString(R.string.starred));

                    } else {

                        // Set image to unstarred
                        star.setImageResource(R.drawable.star_unselected);
                        star.setContentDescription(getString(R.string.unstarred));
                    }
                    mListener.onItemStarred(mItem);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemStarredListener) {
            mListener = (OnItemStarredListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnItemStarredListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //endregion Fragment Overridden Methods

    //endregion Methods

    //region Interfaces

    public interface OnItemStarredListener {

        void onItemStarred(Parcelable parcelable);
    }

    //endregion Interfaces
}
