package com.callumveale.bjorneparken.adapters;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.fragments.DetailFragment;
import com.callumveale.bjorneparken.fragments.ListFragment;

import java.util.ArrayList;

/**
 * Created by callum on 03/03/2017.
 */
class RecyclerViewAdapter extends RecyclerView.Adapter<ListItemViewHolder> implements IListAdapter{

    //region Constants

    private static final int EMPTY_TEXT_RESOURCE = R.string.list_empty;

    //endregion Constants

    //region Properties

    final HomeActivity mActivity;
    final ArrayList<? extends Parcelable> mItems;
    final ListFragment.OnListItemSelectionListener mItemSelectedListener;
    final DetailFragment.OnItemStarredListener mItemStarredListener;

    RecyclerView mRecyclerView;

    //endregion Properties

    //region Constructors

    RecyclerViewAdapter(HomeActivity activity, ArrayList<? extends Parcelable> items, ListFragment.OnListItemSelectionListener selectedListener, DetailFragment.OnItemStarredListener starredListener) {
        mActivity = activity;
        mItems = items;
        mItemSelectedListener = selectedListener;
        mItemStarredListener = starredListener;
    }

    RecyclerViewAdapter(HomeActivity activity, ArrayList<? extends Parcelable> items, ListFragment.OnListItemSelectionListener selectedListener){
        mActivity = activity;
        mItems = items;
        mItemSelectedListener = selectedListener;
        mItemStarredListener = null;
    }

    //endregion Constructors

    //region Methods

    //region RecyclerView.Adapter<ListItemViewHolder> Overridden Methods

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_item, parent, false);

        mRecyclerView = (RecyclerView) parent;

        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    //endregion RecyclerView.Adapter<ListItemViewHolder> Overridden Methods

    //region IListAdapter Overridden Methods

    public String getEmptyText(Activity activity){

        return activity.getString(EMPTY_TEXT_RESOURCE);
    }

    //endregion IListAdapter Overridden Methods

    //endregion Methods
}
