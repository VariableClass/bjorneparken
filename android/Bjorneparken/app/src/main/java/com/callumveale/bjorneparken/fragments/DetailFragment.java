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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.models.Amenity;
import com.callumveale.bjorneparken.models.IModel;
import com.callumveale.bjorneparken.models.Species;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements DialogConfirmFragment.OnUnstarDetailListener {

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

        Bitmap bitmap = item.getImage();

        LinearLayout mDescriptionPanel = (LinearLayout) view.findViewById(R.id.detail_description_panel);
        TextView mCaptionView;
        TextView mSubcaptionView;

        // Set image
        if (bitmap != null){

            // Show the image panel
            LinearLayout imagePanel = (LinearLayout) view.findViewById(R.id.detail_image_panel);
            imagePanel.setVisibility(View.VISIBLE);

            // Align description view to right of image panel
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mDescriptionPanel.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.RIGHT_OF, R.id.detail_image_panel);
            mDescriptionPanel.setLayoutParams(params);

            ImageView imageView = (ImageView) view.findViewById(R.id.detail_image);
            imageView.setImageBitmap(bitmap);

            mCaptionView = (TextView) view.findViewById(R.id.detail_caption_image);
            mSubcaptionView = (TextView) view.findViewById(R.id.detail_subcaption_image);

        } else {

            mCaptionView = (TextView) view.findViewById(R.id.detail_caption);
            mSubcaptionView = (TextView) view.findViewById(R.id.detail_subcaption);
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

            // Retrieve and set subheader text view
            TextView mSubheaderView = (TextView) view.findViewById(R.id.detail_subheader);

            if (mItem.getClass() == Amenity.class){

                mSubheaderView.setText(((Amenity) mItem).getAmenityTypeTranslation(getActivity()));

            } else {

                mSubheaderView.setText(item.getSubheader());
            }

            // Show subheader text view
            mSubheaderView.setVisibility(View.VISIBLE);
        }

        // If item has a caption
        if (item.getCaption() != null){

            // Retrieve, set and show caption text view
            mCaptionView.setText(item.getCaption());
            mCaptionView.setVisibility(View.VISIBLE);
        }

        // If item has a subcaption
        if (item.getSubcaption(getContext()) != null){

            // Retrieve, set and show subcaption text view
            mSubcaptionView.setText(item.getSubcaption(getContext()));
            mSubcaptionView.setVisibility(View.VISIBLE);
        }

        // If item has a list
        if (item.getList() != null){

            // Retrieve, set and show list title text view
            TextView mListTitleView = (TextView) view.findViewById(R.id.detail_list_title);
            mListTitleView.setText(item.getListTitle(getContext()));
            mListTitleView.setVisibility(View.VISIBLE);

            // Retrieve, set and show list text view
            String listString = "";

            for (String listItem : item.getList()){

                listString += listItem + "\n";
            }


            TextView mListView = (TextView) view.findViewById(R.id.detail_list);
            mListView.setText(listString);
            mListView.setVisibility(View.VISIBLE);
        }

        final DetailFragment fragment = this;

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

                        // Perform star action
                        mListener.onItemStarred(mItem, fragment);

                    } else {

                        // If item is a Species, show confirm dialog
                        if (mItem.getClass() == Species.class) {

                            // Show confirmation dialog
                            DialogConfirmFragment confirmDialog = DialogConfirmFragment.newInstance(R.string.confirm_unstar_species_title, R.string.confirm_unstar_species_message);
                            confirmDialog.setUnstarDetailListener(fragment);
                            confirmDialog.show(getActivity().getSupportFragmentManager(), "DialogConfirmFragment");

                        } else {

                            // Set image to starred
                            star.setImageResource(R.drawable.star_unselected);
                            star.setContentDescription(getString(R.string.unstarred));

                            // Perform star action
                            mListener.onItemStarred(mItem, fragment);
                        }
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void unstar(boolean visualUnstarOnly){

        visualUnstar();

        if (!visualUnstarOnly) {

            // Perform unstar action
            mListener.onItemStarred(mItem);
        }
    }

    private void visualUnstar(){

        ImageView star = (ImageView) getView().findViewById(R.id.detail_star);

        // Set image to unstarred
        star.setImageResource(R.drawable.star_unselected);
        star.setContentDescription(getString(R.string.unstarred));
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

        void onItemStarred(Parcelable parcelable, DialogConfirmFragment.OnUnstarDetailListener listener);

        void onItemStarred(Parcelable parcelable, DialogConfirmFragment.OnUnstarListItemListener listener, int position);
    }

    //endregion Interfaces
}
