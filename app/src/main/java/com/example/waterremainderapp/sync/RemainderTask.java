package com.example.waterremainderapp.sync;

import android.content.Context;

import com.example.waterremainderapp.utilities.NotificationUtils;
import com.example.waterremainderapp.utilities.PreferencesUtilities;

public class RemainderTask {
    public static final String ACTION_INCREMENT_WATER_COUNT  = "increment-water-count";
    public static final String ACTION_DISMISS_NOTIFICATION  = "dismiss-notification";
    public static final String ACTION_CHARGING_REMAINDER  = "charging-remainder";
    public static void executeTask(Context context,String action){
        if (ACTION_INCREMENT_WATER_COUNT.equals(action)){
            // water count increase
            incrementWaterCount(context);
        }else if (ACTION_DISMISS_NOTIFICATION.equals(action)){
            // clear notification
            NotificationUtils.clearAllNotification(context);
        }else if (ACTION_CHARGING_REMAINDER.equals(action)){
            // charging state
            issueChargingRemainder(context);
        }
    }
    private static void incrementWaterCount(Context context){
        PreferencesUtilities.incrementWaterCount(context);
    }
    private static void issueChargingRemainder(Context context){
        PreferencesUtilities.incrementChargingRemainderCount(context);
    }
}
