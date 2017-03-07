package com.callumveale.bjorneparken.models;

/**
 * Created by callum on 01/03/2017.
 */

public interface IModel {

    String getHeader();

    String getSubheader();

    String getDescription();

    String getCaption();

    String getSubcaption();

    byte[] getImageBytes();
}
