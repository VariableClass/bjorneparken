package com.callumveale.bjorneparken.config;

import java.util.Properties;

/**
 * Created by callum on 10/03/2017.
 */

public class Configuration extends Properties {

    //region Constants

    public static final String NOTIFICATIONS_ENABLED = "notifications_enabled";

    //endregion Constants

    //region Constructors

    public Configuration(){

        setNotificationsEnabled(true);
    }

    //endregion Constructors

    //region Methods

    public void setNotificationsEnabled(Boolean enabled){

        setProperty(NOTIFICATIONS_ENABLED, enabled.toString());
    }

    //endregion Methods
}
