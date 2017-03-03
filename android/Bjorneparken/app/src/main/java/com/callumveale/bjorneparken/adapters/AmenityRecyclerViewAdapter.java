package com.callumveale.bjorneparken.adapters;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.fragments.ListFragment.OnListItemSelectionListener;
import com.callumveale.bjorneparken.models.Amenity;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class AmenityRecyclerViewAdapter extends RecyclerViewAdapter {

    //region Constants

    private static final int EMPTY_TEXT_RESOURCE = R.string.no_amenities;

    //endregion Constants

    //region Constructors

    public AmenityRecyclerViewAdapter(ArrayList<? extends Parcelable> amenities, OnListItemSelectionListener listener) {
        super(amenities, listener);
    }

    //endregion Constructors

    //region Methods

    //region RecyclerViewAdapter Overridden Methods

    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, int position) {

        Amenity amenity = (Amenity) mItems.get(position);

        holder.mItem = amenity;
        holder.mHeaderView.setText(amenity.getLabel());
        holder.mBodyView.setText(amenity.getDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mItemSelectedListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    holder.mView.setPressed(true);
                    mItemSelectedListener.onItemSelection(holder.mItem);
                }
            }
        });
    }

    //endregion RecyclerViewAdapter Overridden Methods

    //region IListAdapter Overridden Methods

    @Override
    public String getEmptyText(Activity activity){

        return activity.getString(EMPTY_TEXT_RESOURCE);
    }

    //endregion IListAdapter Overridden Methods

    //endregion Methods
}
