package com.callumveale.bjorneparken.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.callumveale.bjorneparken.R;
import com.callumveale.bjorneparken.models.NavigationDrawerItem;

/**
 * Created by callum on 03/03/2017.
 */
class NavigationDrawerItemViewHolder extends RecyclerView.ViewHolder {

    //region Properties

    final View mView;
    final TextView mNameView;
    NavigationDrawerItem mItem;

    //endregion Properties

    //region Constructors

    NavigationDrawerItemViewHolder(View view) {
        super(view);
        mView = view;
        mNameView = (TextView) view.findViewById(R.id.drawer_itemName);
    }

    //endregion Constructors

    //region Methods

    //region RecyclerView.ViewHolder Overridden Methods

    @Override
    public String toString() {
        return super.toString() + " '" + mNameView.getText() + "'";
    }

    //endregion RecyclerView.ViewHolder Overridden Methods

    //endregion Methods
}
