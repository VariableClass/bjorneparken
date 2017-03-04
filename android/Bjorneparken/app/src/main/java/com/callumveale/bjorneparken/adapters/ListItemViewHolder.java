package com.callumveale.bjorneparken.adapters;

import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.callumveale.bjorneparken.R;

/**
 * Created by callum on 03/03/2017.
 */

class ListItemViewHolder extends RecyclerView.ViewHolder {

    //region Properties

    final View mView;
    final TextView mHeaderView;
    final TextView mSubheadingView;
    final TextView mImportantSubheadingView;
    final TextView mBodyView;
    final ImageView mStarView;

    Parcelable mItem;

    //endregion Properties

    //region Constructors

    ListItemViewHolder(View view) {
        super(view);
        mView = view;
        mHeaderView = (TextView) view.findViewById(R.id.header_heading);
        mSubheadingView = (TextView) view.findViewById(R.id.header_subheading);
        mImportantSubheadingView = (TextView) view.findViewById(R.id.header_important_subheading);
        mBodyView = (TextView) view.findViewById(R.id.body);
        mStarView = (ImageView) view.findViewById(R.id.list_star);

    }

    //endregion Constructors

    //region Methods

    //region RecyclerView.ViewHolder Overridden Methods

    @Override
    public String toString() {
        return super.toString() + " '" + mHeaderView.getText() + "'";
    }

    //endregion RecyclerView.ViewHolder Overridden Methods

    //endregion Methods
}