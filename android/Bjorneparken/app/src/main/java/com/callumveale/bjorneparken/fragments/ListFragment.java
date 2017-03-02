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
import com.callumveale.bjorneparken.adapters.IListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ListFragment extends Fragment {

    public static final String ARG_LIST = "list";
    public static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ARG_DATA_TYPE = "data-type";

    private ArrayList<Parcelable> mList = new ArrayList<>();
    private int mColumnCount = 1;
    private String mDataType = "";
    private OnListItemSelectionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListFragment() {
    }

    public static ListFragment newInstance(Parcelable[] items, int columnCount, String dataType) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(ARG_LIST, items);
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_DATA_TYPE, dataType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mList = getArguments().getParcelableArrayList(ARG_LIST);
            mDataType = getArguments().getString(ARG_DATA_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        Object viewAdapter = null;

        try {
            Class className = Class.forName("com.callumveale.bjorneparken.adapters." + mDataType + "RecyclerViewAdapter");
            viewAdapter = className.getConstructor(List.class, OnListItemSelectionListener.class).newInstance(mList, mListener);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Retrieve empty text
        TextView emptyTextView = (TextView) view.findViewById(R.id.list_empty);

        // If list has items
        if (mList.size() != 0) {

            // Hide empty text
            emptyTextView.setVisibility(View.GONE);

            RecyclerView list = (RecyclerView) view.findViewById(R.id.list);

            // Set the adapter
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                list.setLayoutManager(new LinearLayoutManager(context));
            } else {
                list.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            list.setAdapter((RecyclerView.Adapter) viewAdapter);

            list.setVisibility(View.VISIBLE);

        } else {

            String emptyText = ((IListAdapter)viewAdapter).getEmptyText(getActivity());
            emptyTextView.setText(emptyText);
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListItemSelectionListener) {
            mListener = (OnListItemSelectionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListItemSelectionListener {

        void onListItemSelection(Parcelable item, View selectedListItem);
    }

}
