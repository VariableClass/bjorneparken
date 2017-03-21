package com.callumveale.bjorneparken.models;

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

    String getSubcaption();

    String getImageUrl();

    Bitmap getImage();

    void setImage(Bitmap bitmap);
}
