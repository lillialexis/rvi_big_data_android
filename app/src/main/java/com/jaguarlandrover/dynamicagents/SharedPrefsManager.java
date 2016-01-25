package com.jaguarlandrover.dynamicagents;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2016 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    SharedPrefsManager.java
 * Project: DynamicAgents
 *
 * Created by Lilli Szafranski on 1/21/16.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefsManager
{
    private final static String TAG = "DynamicAgents:SharedPrefsManager";

    private final static String PREFS_STRING = "vehicle_manager_prefs";

    private static Context                  applicationContext = DynamicAgentsApplication.getContext();
    private static SharedPreferences        prefs              = applicationContext.getSharedPreferences(PREFS_STRING, MODE_PRIVATE);
    private static SharedPreferences.Editor editor             = prefs.edit();

    public static String getStringFromPrefs(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    public static Integer getIntFromPrefs(String key, Integer defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    public static Boolean getBoolFromPrefs(String key, Boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    public static void putStringInPrefs(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public static void putIntInPrefs(String key, Integer value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public static void putBoolInPrefs(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void deleteValueFromPrefs(String key) {
        editor.remove(key);
        editor.apply();
    }

    private SharedPrefsManager() {
    }
}
