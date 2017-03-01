package com.callumveale.bjorneparken.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public static final String ARG_ITEM = "item";

    private Parcelable mItem;

    private OnItemStarredListener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(Parcelable item) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = getArguments().getParcelable(ARG_ITEM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        IModel item = (IModel) mItem;

        TextView mHeaderView = (TextView) view.findViewById(R.id.detail_header);
        mHeaderView.setText(item.getHeader());

        TextView mDescriptionView = (TextView) view.findViewById(R.id.detail_description);
        mDescriptionView.setText(item.getDescription());

        if (item.getSubheader() != null){

            TextView mSubheaderView = (TextView) view.findViewById(R.id.detail_subheader);
            mSubheaderView.setText(item.getSubheader());
            mSubheaderView.setVisibility(View.VISIBLE);
        }

        if (item.getCaption() != null){

            TextView mCaptionView = (TextView) view.findViewById(R.id.detail_caption);
            mCaptionView.setText(item.getCaption());
            mCaptionView.setVisibility(View.VISIBLE);
        }

        if (item.getSubcaption() != null){

            TextView mSubcaptionView = (TextView) view.findViewById(R.id.detail_subcaption);
            mSubcaptionView.setText(item.getSubcaption());
            mSubcaptionView.setVisibility(View.VISIBLE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onItemStarred(mItem);
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Parcelable parcelable) {
        if (mListener != null) {
            mListener.onItemStarred(parcelable);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemStarredListener) {
            mListener = (OnItemStarredListener) context;
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
    public interface OnItemStarredListener {
        void onItemStarred(Parcelable parcelable);
    }
}
