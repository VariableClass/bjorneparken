package com.callumveale.bjorneparken.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.fragments.DetailFragment;
import com.callumveale.bjorneparken.fragments.ListFragment.OnListItemSelectionListener;
import com.callumveale.bjorneparken.models.Species;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class SpeciesRecyclerViewAdapter extends RecyclerViewAdapter {

    //region Constants

    private static final int EMPTY_TEXT_RESOURCE = R.string.no_species;

    //endregion Constants

    //region Constructors

    public SpeciesRecyclerViewAdapter(HomeActivity activity, ArrayList<? extends Parcelable> species, OnListItemSelectionListener selectedListener, DetailFragment.OnItemStarredListener starredListener) {
        super(activity, species, selectedListener, starredListener);

    }

    //endregion Constructors

    //region Methods

    //region RecyclerViewAdapter Overridden Methods

    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, final int position) {

        final Species species = (Species) mItems.get(position);

        // Set list view item
        holder.mItem = species;

        // Set header
        holder.mHeaderView.setText(species.getCommonName());

        // Set subheading
        holder.mSubheadingView.setText(species.getLatin());
        holder.mSubheadingView.setTypeface(holder.mSubheadingView.getTypeface(), Typeface.ITALIC);
        holder.mSubheadingView.setVisibility(View.VISIBLE);

        // Set star button
        if (mActivity.isStarred(species)){

            holder.mStarButton.setText(R.string.starred);

        } else {

            holder.mStarButton.setText(R.string.unstarred);
        }
        holder.mStarButton.setVisibility(View.VISIBLE);

        // Set body
        String description = species.getDescription();
        if (description.length() > 100){
            description = description.substring(0, 95) + "...";
        }
        holder.mBodyView.setText(description);

        // Add selected listener
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mItemSelectedListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    holder.mView.setPressed(true);
                    mItemSelectedListener.onStarredItemSelection(holder.mItem, mActivity.isStarred(species));
                }
            }
        });

        // Add starred listener
        holder.mStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mItemSelectedListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    if (mActivity.isStarred(species)){

                        holder.mStarButton.setText(R.string.unstarred);

                    } else {

                        holder.mStarButton.setText(R.string.starred);
                    }
                    mItemStarredListener.onItemStarred(holder.mItem);
                }
            }
        });
    }

    @Override
    public String getEmptyText(Activity activity){

        return activity.getString(EMPTY_TEXT_RESOURCE);
    }

    //endregion RecyclerViewAdapter Overridden Methods

    //endregion Methods
}
