package com.example.waterremainderapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.prefs.Preferences;

public class PreferencesUtilities {
    public static final String KEY_WATER_COUNT = "water-count";
    public static final String KEY_CHARGING_REMAINDER_COUNT = "charging-reminder-count";
    private static final int DEFAULT_COUNT = 0;

    /**
     * Get the total water taken by user
     *
     * @param context
     * @return
     */
    public static int getWaterCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int glassOfWater = prefs.getInt(KEY_WATER_COUNT, DEFAULT_COUNT);
        return glassOfWater;
    }

    /**
     * Set the  water count
     *
     * @param context
     * @return
     */
    synchronized private static void setWaterCount(Context context, int glassOfWater) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(KEY_WATER_COUNT, glassOfWater);
        editor.apply();
    }

    /**
     * Increments water count by one
     */
    synchronized public static void incrementWaterCount(Context context) {
        int waterCount = PreferencesUtilities.getWaterCount(context);
        PreferencesUtilities.setWaterCount(context, ++waterCount);
    }

    /**
     * Get the total charging remainder count
     *
     * @param context
     * @return
     */
    public static int getChargingRemainder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingRemainder = prefs.getInt(KEY_CHARGING_REMAINDER_COUNT, DEFAULT_COUNT);
        return chargingRemainder;
    }

    /**
     * increment the total charge count by one
     *
     * @param context
     * @return
     */
    synchronized public static void incrementChargingRemainderCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingRemainder = prefs.getInt(KEY_WATER_COUNT, DEFAULT_COUNT);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_CHARGING_REMAINDER_COUNT, ++chargingRemainder);
        editor.apply();
    }
}
