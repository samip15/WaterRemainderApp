package com.example.waterremainderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterremainderapp.sync.RemainderTask;
import com.example.waterremainderapp.sync.RemainderUtils;
import com.example.waterremainderapp.sync.WaterRemainderIntentService;
import com.example.waterremainderapp.utilities.NotificationUtils;
import com.example.waterremainderapp.utilities.PreferencesUtilities;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView mWaterCount, mChargingCount;
    private ImageView mChargingIv;
    private Toast mToast;

    ChargingBroadCastRecerver mChargingBroadCastReceiver;
    IntentFilter mChrgingIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // views
        mWaterCount = findViewById(R.id.tv_water_count);
        mChargingCount = findViewById(R.id.tv_charging_remainder_count);
        mChargingIv = findViewById(R.id.iv_power_increment);
        // update views
        updateWaterCount();
        updateChargingRemainderCount();
        // run the charging remainder
        RemainderUtils.scheduleChargingRemainder(this);
        // set up shared preference
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.registerOnSharedPreferenceChangeListener(this);
        // broadcast receiver
        mChargingBroadCastReceiver = new ChargingBroadCastRecerver();
        mChrgingIntentFilter = new IntentFilter();
        //  add filters to intent filter
        mChrgingIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        mChrgingIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
    }


    /**
     * update water count into text view
     */
    private void updateWaterCount() {
        int waterCount = PreferencesUtilities.getWaterCount(this);
        mWaterCount.setText(waterCount+"");
    }

    /**
     * update charging remainder into text view
     */
    private void updateChargingRemainderCount() {
        int chargingRemainder = PreferencesUtilities.getChargingRemainder(this);
        String formattedString = getResources().getQuantityString(R.plurals.charge_notification_count, chargingRemainder, chargingRemainder);
        mChargingCount.setText(formattedString);
    }

    /**
     * Add water count by 1
     *
     * @param view
     */
    public void incrementWater(View view) {
        // toast if increment
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, getString(R.string.drinkingwater), Toast.LENGTH_SHORT);
        mToast.show();
        // increment and start service
        Intent increamentWaterCountIntent = new Intent(this, WaterRemainderIntentService.class);
        increamentWaterCountIntent.setAction(RemainderTask.ACTION_INCREMENT_WATER_COUNT);
        startService(increamentWaterCountIntent);
    }
    // while pref get changed
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
       if (PreferencesUtilities.KEY_WATER_COUNT.equals(key)){
           updateWaterCount();
       }else if (PreferencesUtilities.KEY_CHARGING_REMAINDER_COUNT.equals(key)){
           updateChargingRemainderCount();
       }
    }

    /**
     * un resister the preference
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // clean
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }
    // ========================BROADCAST RECEIVER ====================
    private class ChargingBroadCastRecerver extends BroadcastReceiver{
        // we receive certain intent if the phone is charging or an any other state.
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean isCharging = action.equals(Intent.ACTION_POWER_CONNECTED);
            showCharging(isCharging);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mChargingBroadCastReceiver,mChrgingIntentFilter);
        // sticky broadcast
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            // use battery manager to know if there is change in charging
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            showCharging(batteryManager.isCharging());
        }else{
            // using intent filter and battery manager to get the change in battrey status
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent currentBatteryStatusIntent = registerReceiver(null,intentFilter);
            int batteryStatus = currentBatteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
            boolean isCharging = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING || batteryStatus == BatteryManager.BATTERY_STATUS_DISCHARGING;
            showCharging(isCharging);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mChargingBroadCastReceiver);
    }

    /**
     * change the charging color
     */
    private void showCharging(boolean isCharging){
        if (isCharging){
            mChargingIv.setImageResource(R.drawable.ic_baseline_power_24_green);
        }else{
            mChargingIv.setImageResource(R.drawable.ic_baseline_power_24);
        }
    }
}