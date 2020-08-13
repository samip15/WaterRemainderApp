package com.example.waterremainderapp.sync;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class WaterRemainderIntentService extends IntentService {
    // default constructor
    public WaterRemainderIntentService() {
        super("WaterRemainderIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // get Action From Intent
        String action = intent.getAction();
        // pass action
        RemainderTask.executeTask(this,action);

    }
}
