package com.callumveale.bjorneparken.adapters;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.fragments.DetailFragment;
import com.callumveale.bjorneparken.fragments.ListFragment.OnListItemSelectionListener;
import com.callumveale.bjorneparken.models.Event;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class EventRecyclerViewAdapter extends RecyclerViewAdapter {

    //region Constants

    private static final int EMPTY_TEXT_RESOURCE = R.string.empty_itinerary;

    //endregion Constants

    //region Constructors

    public EventRecyclerViewAdapter(HomeActivity activity, ArrayList<? extends Parcelable> events, OnListItemSelectionListener selectedListener, DetailFragment.OnItemStarredListener starredListener) {
        super(activity, events, selectedListener, starredListener);
    }

    //endregion Constructors

    //region Methods

    //region RecyclerViewAdapter Overridden Methods

    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, final int position) {

        final Event event = (Event) mItems.get(position);

        // Set list view item
        holder.mItem = event;

        // Set header
        holder.mHeaderView.setText(event.getLabel());

        // Set subheading
        holder.mSubheadingView.setText(event.getLocation().getLabel());
        holder.mSubheadingView.setVisibility(View.VISIBLE);

        // Set important subheading
        String timeDescription = event.getStartTime() + " - " + event.getEndTime();
        holder.mImportantSubheadingView.setText(timeDescription);
        holder.mImportantSubheadingView.setVisibility(View.VISIBLE);

        // Set star image
        if (mActivity.isInItinerary(event)){

            // Set image to starred
            holder.mStarView.setImageResource(R.drawable.star_selected);
            holder.mStarView.setContentDescription(mActivity.getString(R.string.starred));

        } else {

            // Set image to unstarred
            holder.mStarView.setImageResource(R.drawable.star_unselected);
            holder.mStarView.setContentDescription(mActivity.getString(R.string.unstarred));
        }
        holder.mStarView.setVisibility(View.VISIBLE);

        // Set body
        String description = event.getDescription();
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
                    mItemSelectedListener.onStarredItemSelection(holder.mItem, mActivity.isInItinerary(event));
                }
            }
        });

        // Add starred listener
        holder.mStarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mItemStarredListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    if (mActivity.isInItinerary(event)){

                        // Set image to unstarred
                        holder.mStarView.setImageResource(R.drawable.star_unselected);
                        holder.mStarView.setContentDescription(mActivity.getString(R.string.unstarred));

                    } else {

                        // Set image to starred
                        holder.mStarView.setImageResource(R.drawable.star_selected);
                        holder.mStarView.setContentDescription(mActivity.getString(R.string.starred));
                    }
                    mItemStarredListener.onItemStarred(holder.mItem);
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
