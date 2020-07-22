package com.example.waterremainderapp.sync;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class RemainderUtils {

    // charging state or not
    // 15 mins update or notify
    private static final int REMAINDER_INTERVAL_MINUTES = 1;
    private static final int REMAINDER_INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toSeconds(REMAINDER_INTERVAL_MINUTES);
    private static final int SYNC_TIME_SECONDS = REMAINDER_INTERVAL_SECONDS;
    // tag
    private static final String REMAINDER_JOB_TAG = "hydration-remainder-tag";
    // bool to check if our job is initialized or not
    private static boolean sInitialized;

    synchronized public static void scheduleChargingRemainder(@NonNull final Context context) {
        if (sInitialized) {
            return;
        }
        // initialize
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        // job create
        Job remaindejob = dispatcher.newJobBuilder()
                // first set our job service
                .setService(WaterRemainderFirebaseJobService.class)
                // set unit tag to the job
                .setTag(REMAINDER_JOB_TAG)
                // set constraints where the job should run
                .setConstraints(Constraint.DEVICE_CHARGING)
                // job run until the device is rebooted
                .setLifetime(Lifetime.FOREVER)
                // frequently job should rul
                .setRecurring(true)
                // our job should run at 1 minute interval for now
                .setTrigger(
                        Trigger.executionWindow(
                                REMAINDER_INTERVAL_MINUTES,
                                REMAINDER_INTERVAL_MINUTES + SYNC_TIME_SECONDS))
                // replace old job with new one
                .setReplaceCurrent(true)
                .build();
        // schedule the job
        dispatcher.schedule(remaindejob);
        sInitialized = true;
    }

}
