package com.callumveale.bjorneparken.notifications;

public class Notification {

    private int mNotificationId;
    private long mAreaId;
    private long mEventId;
    private long mStartTime;

    public Notification(int notificationId, long areaId, long eventId, long startTime){

        mNotificationId = notificationId;
        mAreaId = areaId;
        mEventId = eventId;
        mStartTime = startTime;
    }

    public int getNotificationId(){

        return mNotificationId;
    }

    public long getAreaId(){

        return mAreaId;
    }

    public long getEventId(){

        return mEventId;
    }

    public long getStartTime(){

        return mStartTime;
    }

    @Override
    public String toString(){

        return mNotificationId + "," + mAreaId + "," + mEventId + "," + mStartTime;
    }
}
