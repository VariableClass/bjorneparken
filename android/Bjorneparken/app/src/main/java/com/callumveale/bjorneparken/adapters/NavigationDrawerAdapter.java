package com.callumveale.bjorneparken.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.models.NavigationDrawerItem;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerItemViewHolder> {


    //region Properties

    private NavigationDrawerItem mItems[];
    private final INavigationDrawerListener mListener;

    //endregion Properties

    //region Constructors

    public NavigationDrawerAdapter(NavigationDrawerItem[] items, INavigationDrawerListener listener) {
        mItems = items;
        mListener = listener;
    }

    //endregion Constructors

    //region Methods

    //region RecyclerView.Adapter<NavigationDrawerItemViewHolder> Overridden Methods

    @Override
    public NavigationDrawerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_list_item, parent, false);

        return new NavigationDrawerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NavigationDrawerItemViewHolder holder, int position) {

        // Set item
        holder.mItem = mItems[position];

        // Set label
        holder.mNameView.setText(mItems[position].name);

        // Add selected listener
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {

                    mListener.navigateToPosition(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.length;
    }

    //endregion RecyclerView.Adapter<NavigationDrawerItemViewHolder> Overridden Methods

    //endregion Methods

    //region Interfaces

    public interface INavigationDrawerListener {

        void navigateToPosition(int position);
    }

    //endregion Interfaces
}