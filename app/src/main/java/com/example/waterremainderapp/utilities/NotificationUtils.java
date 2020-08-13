package com.example.waterremainderapp.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.waterremainderapp.MainActivity;
import com.example.waterremainderapp.R;
import com.example.waterremainderapp.sync.RemainderTask;
import com.example.waterremainderapp.sync.WaterRemainderIntentService;

public class NotificationUtils {
    // notification id
    private static final int WATER_REMAINDER_NOTIFICATION_ID = 347;
    // pending intent
    private static final int WATER_REMAINDER_PENDING_INTENT_ID = 348;
    // channel
    private static final String WATER_REMAINDER_CHANNEL_ID = "remainder_notification";
    // action
    private static final int ACTION_DRINK_PENDING_INTENT_ID = 349;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 350;

    /**
     * it helps us to go to main activity from notification manager using pending intent
     */
    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context,
                WATER_REMAINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
    //=============================NOTIFICATION =================================

    public static void remindUserWhileCharging(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    WATER_REMAINDER_CHANNEL_ID,
                    "primary",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, WATER_REMAINDER_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .setSmallIcon(R.drawable.ic_baseline_local_drink_24)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.noti_text))
                .setContentText(context.getString(R.string.noti_content_text))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.noti_content_text)))
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent(context))
                //actions
                .addAction(drinkWaterAction(context))
                .addAction(ignoreRemainderAction(context))
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(WATER_REMAINDER_NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * creating bit map for large icon
     *
     * @param context
     * @return
     */

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_baseline_local_drink_24);
        return largeIcon;
    }

    // =========================Notification Actions==============

    /**
     * increase water count using action
     */
    private static NotificationCompat.Action drinkWaterAction(Context context) {
        // intent for increasing water count
        Intent incrementWaterCount = new Intent(context, WaterRemainderIntentService.class);
        incrementWaterCount.setAction(RemainderTask.ACTION_INCREMENT_WATER_COUNT);
        PendingIntent increamentPendingIntent = PendingIntent.getService(context,
                ACTION_DRINK_PENDING_INTENT_ID,
                incrementWaterCount,
                PendingIntent.FLAG_CANCEL_CURRENT);
        // create action
        NotificationCompat.Action drinkWaterAction = new NotificationCompat.Action(R.drawable.ic_baseline_local_drink_24,
                "I did it!!",
                increamentPendingIntent);
        return drinkWaterAction;
    }

    // =========================Notification Actions=====================================

    /**
     * ignore notification action and clear notification
     */
    private static NotificationCompat.Action ignoreRemainderAction(Context context) {
        // intent for increasing water count
        Intent ignoreRemainderIntent = new Intent(context, WaterRemainderIntentService.class);
        ignoreRemainderIntent.setAction(RemainderTask.ACTION_DISMISS_NOTIFICATION);
        PendingIntent ignorePendingIntent = PendingIntent.getService(context,
                ACTION_DRINK_PENDING_INTENT_ID,
                ignoreRemainderIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        // create action
        NotificationCompat.Action ignoreRemainderAction = new NotificationCompat.Action(R.drawable.ic_baseline_cancel_24,
                "No Thank You!!",
                ignorePendingIntent);
        return ignoreRemainderAction;
    }

    /**
     * helps to clear all notification
     * @param context
     */
    public static void clearAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
