package com.callumveale.bjorneparken.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.fragments.DetailFragment;
import com.callumveale.bjorneparken.fragments.ListFragment.OnListItemSelectionListener;
import com.callumveale.bjorneparken.models.Event;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> implements IListAdapter{

    private static final int EMPTY_TEXT_RESOURCE = R.string.empty_itinerary;

    private final HomeActivity mActivity;
    private final List<Event> mEvents;
    private final OnListItemSelectionListener mItemSelectedListener;
    private final DetailFragment.OnItemStarredListener mItemStarredListener;

    public EventRecyclerViewAdapter(HomeActivity activity, List items, OnListItemSelectionListener selectedListener, DetailFragment.OnItemStarredListener starredListener) {
        mActivity = activity;
        mEvents = (List<Event>)items;
        mItemSelectedListener = selectedListener;
        mItemStarredListener = starredListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mEvents.get(position);
        holder.mLabelView.setText(mEvents.get(position).getLabel());
        holder.mLocationView.setText(mEvents.get(position).getLocation().getLabel());

        if (isStarred(position)){

            holder.mStarredButton.setText(R.string.starred);

        } else {

            holder.mStarredButton.setText(R.string.unstarred);
        }

        String timeDescription = mEvents.get(position).getStartTime() + " - " + mEvents.get(position).getEndTime();

        holder.mTimesView.setText(timeDescription);

        String description = mEvents.get(position).getDescription();
        if (description.length() > 100){
            description = description.substring(0, 95) + "...";
        }
        holder.mDescriptionView.setText(description);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mItemSelectedListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    holder.mView.setPressed(true);
                    mItemSelectedListener.onStarredListItemSelection(holder.mItem, isStarred(position));
                }
            }
        });

        holder.mStarredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mItemSelectedListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mItemStarredListener.onItemStarred(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public String getEmptyText(Activity activity){

        return activity.getString(EMPTY_TEXT_RESOURCE);
    }

    private boolean isStarred(int position){

        boolean isStarred = false;

        for (Event starredEvent : mActivity.getVisitor().getItinerary()){

            // If the event is starred
            if ((starredEvent.getId() == mEvents.get(position).getId()) &&
                    (starredEvent.getLocation().getId() == mEvents.get(position).getLocation().getId())){

                isStarred = true;
                break;
            }
        }

        return isStarred;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mLabelView;
        public final TextView mDescriptionView;
        public final TextView mLocationView;
        public final TextView mTimesView;
        public final Button mStarredButton;

        public Event mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLabelView = (TextView) view.findViewById(R.id.header_heading);
            mLocationView = (TextView) view.findViewById(R.id.header_subheading);
            mLocationView.setVisibility(View.VISIBLE);
            mTimesView = (TextView) view.findViewById(R.id.header_important_subheading);
            mTimesView.setVisibility(View.VISIBLE);
            mDescriptionView = (TextView) view.findViewById(R.id.body);
            mStarredButton = (Button) view.findViewById(R.id.list_star);
            mStarredButton.setVisibility(View.VISIBLE);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLabelView.getText() + "'";
        }
    }
}
