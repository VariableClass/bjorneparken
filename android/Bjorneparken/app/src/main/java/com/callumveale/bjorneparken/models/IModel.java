package com.callumveale.bjorneparken.models;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by callum on 01/03/2017.
 */

public interface IModel {

    long getId();

    String getHeader();

    String getSubheader();

    String getDescription();

    String getCaption();

    String getSubcaption(Context context);

    String getListTitle(Context context);

    String[] getList();

    String getImageUrl();

    Bitmap getImage();

    void setImage(Bitmap bitmap);
}
