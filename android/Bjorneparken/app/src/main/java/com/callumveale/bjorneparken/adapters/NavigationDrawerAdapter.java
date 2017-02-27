package com.callumveale.bjorneparken.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.models.NavigationDrawerItem;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {
    private final INavigationDrawerListener mListener;
    private final int layoutResourceId;
    private NavigationDrawerItem items[] = null;

    public NavigationDrawerAdapter(INavigationDrawerListener listener, int layoutResourceId, NavigationDrawerItem[] items) {
        this.mListener = listener;
        this.layoutResourceId = layoutResourceId;
        this.items = items;
    }

    @Override
    public NavigationDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NavigationDrawerAdapter.ViewHolder holder, int position) {
        holder.mItem = items[position];
        holder.mNameView.setText(items[position].name);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {

                    mListener.selectNavigationItem(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public NavigationDrawerItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.drawer_itemName);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }

    public interface INavigationDrawerListener {

        void selectNavigationItem(int position);
    }
}