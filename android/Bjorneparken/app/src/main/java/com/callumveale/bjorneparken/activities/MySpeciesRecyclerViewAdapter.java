package com.callumveale.bjorneparken.activities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.models.Species;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySpeciesRecyclerViewAdapter extends RecyclerView.Adapter<MySpeciesRecyclerViewAdapter.ViewHolder> {

    private final List<Species> mValues;
    private final SpeciesFragment.OnListFragmentInteractionListener mListener;

    public MySpeciesRecyclerViewAdapter(List<Species> items, SpeciesFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_species, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mCommonNameView.setText(mValues.get(position).getCommonName());
        holder.mLatinView.setText(mValues.get(position).getLatin());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public final TextView mCommonNameView;
        public final TextView mLatinView;
        public Species mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCommonNameView = (TextView) view.findViewById(R.id.common_name);
            mLatinView = (TextView) view.findViewById(R.id.latin);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCommonNameView.getText() + "'";
        }
    }
}
