package com.callumveale.bjorneparken.requests;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by callum on 09/03/2017.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    //region Properties

    private RequestsModule mRequester;

    //endregion Properties

    //region Constructors

    public NetworkChangeReceiver(RequestsModule requester) {

        mRequester = requester;
    }

    //endregion Constructors

    @Override
    public void onReceive(Context context, Intent intent) {

        mRequester.checkServerAvailability();
    }
}