package com.callumveale.bjorneparken.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.adapters.IListAdapter;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ListFragment extends Fragment {

    //region Constants

    public static final String ARG_LIST = "list";
    public static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ARG_DATA_TYPE = "data-type";
    public static final String ARG_STARRABLE = "starrable";

    //endregion Constants

    //region Properties

    private ArrayList<? extends Parcelable> mList;
    private boolean isStarrable;
    private int mColumnCount;
    private String mDataType;
    private OnListItemSelectionListener mSelectedListener;
    private DetailFragment.OnItemStarredListener mStarredListener;

    //endregion Properties

    //region Constructors

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListFragment() {
    }

    //endregion Constructors

    //region Methods

    public static ListFragment newInstance(ArrayList<? extends Parcelable> items, int columnCount, String dataType, boolean isStarrable) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_LIST, items);
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_DATA_TYPE, dataType);
        args.putBoolean(ARG_STARRABLE, isStarrable);
        fragment.setArguments(args);
        return fragment;
    }

    public static ListFragment newInstance(ArrayList<? extends Parcelable> items, int columnCount, String dataType) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_LIST, items);
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_DATA_TYPE, dataType);
        args.putBoolean(ARG_STARRABLE, false);
        fragment.setArguments(args);
        return fragment;
    }

    //region Fragment Overridden Methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mList = getArguments().getParcelableArrayList(ARG_LIST);
            mDataType = getArguments().getString(ARG_DATA_TYPE);
            isStarrable = getArguments().getBoolean(ARG_STARRABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Retrieve the list adapter
        Object viewAdapter = null;

        try {

            // Retrieve adapter class name
            Class className = Class.forName("com.callumveale.bjorneparken.adapters." + mDataType + "RecyclerViewAdapter");

            Context activity = getActivity();

            // If class is starrable
            if (isStarrable) {

                // Create a new starrable view adapter
                viewAdapter = className.getConstructor(HomeActivity.class, ArrayList.class, OnListItemSelectionListener.class, DetailFragment.OnItemStarredListener.class).newInstance(activity, mList, mSelectedListener, mStarredListener);

            } else {

                // Create a new non-starrable view adapter
                viewAdapter = className.getConstructor(HomeActivity.class, ArrayList.class, OnListItemSelectionListener.class).newInstance(activity, mList, mSelectedListener);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Retrieve empty text
        TextView emptyTextView = (TextView) view.findViewById(R.id.list_empty);

        // If list has items
        if (mList.size() != 0) {

            // Hide empty text
            emptyTextView.setVisibility(View.GONE);

            // Retrieve list
            RecyclerView list = (RecyclerView) view.findViewById(R.id.list);

            // Set layout manager
            Context context = view.getContext();
            if (mColumnCount <= 1) {

                // If column count less than or equal to 1, set linear layout manager
                list.setLayoutManager(new LinearLayoutManager(context));

            } else {

                // Else, set a grid layout manager
                list.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            // Set view adapter
            list.setAdapter((RecyclerView.Adapter) viewAdapter);

            // Show list
            list.setVisibility(View.VISIBLE);

        } else {

            // Retrieve and show empty text for list
            String emptyText = ((IListAdapter)viewAdapter).getEmptyText(getActivity());
            emptyTextView.setText(emptyText);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListItemSelectionListener) {
            mSelectedListener = (OnListItemSelectionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

        if (context instanceof DetailFragment.OnItemStarredListener) {
            mStarredListener = (DetailFragment.OnItemStarredListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnItemStarredListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSelectedListener = null;
        mStarredListener = null;
    }

    //endregion Fragment Overridden Methods

    //endregion Methods

    //region Interfaces

    public interface OnListItemSelectionListener {

        void onItemSelection(Parcelable item);

        void onStarredItemSelection(Parcelable item, Boolean isStarred);
    }

    //endregion Interfaces
}
