package com.callumveale.bjorneparken.adapters;

import android.app.Activity;
import android.graphics.Typeface;
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
import com.callumveale.bjorneparken.models.Species;
import com.callumveale.bjorneparken.models.Visitor;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class SpeciesRecyclerViewAdapter extends RecyclerView.Adapter<SpeciesRecyclerViewAdapter.ViewHolder> implements IListAdapter{

    private static final int EMPTY_TEXT_RESOURCE = R.string.no_species;

    private final List<Species> mSpecies;
    private final HomeActivity mActivity;
    private final OnListItemSelectionListener mItemSelectedListener;
    private final DetailFragment.OnItemStarredListener mItemStarredListener;

    public SpeciesRecyclerViewAdapter(HomeActivity activity, List species, OnListItemSelectionListener selectedListener, DetailFragment.OnItemStarredListener starredListener) {
        mSpecies = (List<Species>)species;
        mActivity = activity;
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
        holder.mItem = mSpecies.get(position);
        holder.mCommonNameView.setText(mSpecies.get(position).getCommonName());
        holder.mLatinView.setText(mSpecies.get(position).getLatin());

        if (isStarred(position)){

            holder.mStarredButton.setText(R.string.starred);

        } else {

            holder.mStarredButton.setText(R.string.unstarred);
        }

        String description = mSpecies.get(position).getDescription();
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
                    if (isStarred(position)){

                        holder.mStarredButton.setText(R.string.unstarred);

                    } else {

                        holder.mStarredButton.setText(R.string.starred);
                    }
                    mItemStarredListener.onItemStarred(holder.mItem);
                }
            }
        });
    }

    public String getEmptyText(Activity activity){

        return activity.getString(EMPTY_TEXT_RESOURCE);
    }

    private boolean isStarred(int position){

        boolean isStarred = false;

        for (Species starredSpecies : mActivity.getVisitor().getStarredSpecies()){

            if (starredSpecies.getId() == mSpecies.get(position).getId()){

                isStarred = true;
                break;
            }
        }

        return isStarred;
    }

    @Override
    public int getItemCount() {
        return mSpecies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCommonNameView;
        public final TextView mLatinView;
        public final TextView mDescriptionView;
        public final Button mStarredButton;
        public Species mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCommonNameView = (TextView) view.findViewById(R.id.header_heading);
            mLatinView = (TextView) view.findViewById(R.id.header_subheading);
            mLatinView.setTypeface(mLatinView.getTypeface(), Typeface.ITALIC);
            mLatinView.setVisibility(View.VISIBLE);
            mDescriptionView = (TextView) view.findViewById(R.id.body);
            mStarredButton = (Button) view.findViewById(R.id.list_star);
            mStarredButton.setVisibility(View.VISIBLE);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCommonNameView.getText() + "'";
        }
    }
}
