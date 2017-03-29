package com.callumveale.bjorneparken.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.callumveale.bjorneparken.R;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment {

    private static final String ARG_IMAGE_RESOURCE_ID = "image";

    private int mImageResourceId;

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newMenuInstance() {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_RESOURCE_ID, R.drawable.menu);
        fragment.setArguments(args);
        return fragment;
    }

    public static ImageFragment newMapInstance() {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_RESOURCE_ID, R.drawable.map);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageResourceId = getArguments().getInt(ARG_IMAGE_RESOURCE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        ImageViewTouch image = (ImageViewTouch) view.findViewById(R.id.zoomable_image);
        image.setImageResource(mImageResourceId);

        return view;
    }
}
