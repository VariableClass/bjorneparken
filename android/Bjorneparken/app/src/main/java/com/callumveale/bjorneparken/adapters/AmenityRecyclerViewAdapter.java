package com.callumveale.bjorneparken.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.fragments.ListFragment.OnListItemSelectionListener;
import com.callumveale.bjorneparken.models.Amenity;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class AmenityRecyclerViewAdapter extends RecyclerView.Adapter<AmenityRecyclerViewAdapter.ViewHolder> implements IListAdapter {

    private static final int EMPTY_TEXT_RESOURCE = R.string.no_amenities;

    private final List<Amenity> mAmenities;
    private final OnListItemSelectionListener mListener;

    public AmenityRecyclerViewAdapter(List items, OnListItemSelectionListener listener) {
        mAmenities = (List<Amenity>)items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mAmenities.get(position);
        holder.mLabelView.setText(mAmenities.get(position).getLabel());
        holder.mDescriptionView.setText(mAmenities.get(position).getDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    holder.mView.setPressed(true);
                    mListener.onListItemSelection(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAmenities.size();
    }

    public String getEmptyText(Activity activity){

        return activity.getString(EMPTY_TEXT_RESOURCE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mLabelView;
        public final TextView mDescriptionView;
        public Amenity mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLabelView = (TextView) view.findViewById(R.id.header_heading);
            mDescriptionView = (TextView) view.findViewById(R.id.body);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLabelView.getText() + "'";
        }
    }
}
