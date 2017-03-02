package com.callumveale.bjorneparken.adapters;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.fragments.ListFragment.OnListItemSelectionListener;
import com.callumveale.bjorneparken.models.Species;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class SpeciesRecyclerViewAdapter extends RecyclerView.Adapter<SpeciesRecyclerViewAdapter.ViewHolder> implements IListAdapter{

    private static final int EMPTY_TEXT_RESOURCE = R.string.no_species;

    private final List<Species> mValues;
    private final OnListItemSelectionListener mListener;

    public SpeciesRecyclerViewAdapter(List items, OnListItemSelectionListener listener) {
        mValues = (List<Species>)items;
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

    public String getEmptyText(Activity activity){

        return activity.getString(EMPTY_TEXT_RESOURCE);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCommonNameView;
        public final TextView mLatinView;
        public final TextView mDescriptionView;
        public Species mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCommonNameView = (TextView) view.findViewById(R.id.species_common_name);
            mLatinView = (TextView) view.findViewById(R.id.species_latin);
            mDescriptionView = (TextView) view.findViewById(R.id.species_description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCommonNameView.getText() + "'";
        }
    }
}
