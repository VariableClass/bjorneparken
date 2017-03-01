package com.callumveale.bjorneparken.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.fragments.ListFragment.OnListItemSelectionListener;
import com.callumveale.bjorneparken.models.Event;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {

    private final List<Event> mValues;
    private final OnListItemSelectionListener mListener;

    public EventRecyclerViewAdapter(List items, OnListItemSelectionListener listener) {
        mValues = (List<Event>)items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mLabelView.setText(mValues.get(position).getLabel());
        holder.mLocationView.setText(mValues.get(position).getLocation().getLabel());

        String timeDescription = mValues.get(position).getStartTime() + " - " + mValues.get(position).getEndTime();

        holder.mTimesView.setText(timeDescription);

        String description = mValues.get(position).getDescription();
        if (description.length() > 100){
            description = description.substring(0, 95) + "...";
        }
        holder.mDescriptionView.setText(description);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListItemSelection(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mLabelView;
        public final TextView mDescriptionView;
        public final TextView mLocationView;
        public final TextView mTimesView;

        public Event mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLabelView = (TextView) view.findViewById(R.id.event_label);
            mDescriptionView = (TextView) view.findViewById(R.id.event_description);
            mLocationView = (TextView) view.findViewById(R.id.event_location);
            mTimesView = (TextView) view.findViewById(R.id.event_times);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLabelView.getText() + "'";
        }
    }
}
