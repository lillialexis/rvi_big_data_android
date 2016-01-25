package com.jaguarlandrover.dynamicagents;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2016 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    BackendServer.java
 * Project: DynamicAgents
 *
 * Created by Lilli Szafranski on 1/21/16.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.annotation.SuppressLint;

@SuppressLint("LongLogTag")
public class BackendServer
{
    private final static String TAG = "DynamicAgents:BackendServer";

    private final static String KEY_SERVER_URL = "key_server_url";
    private final static String KEY_PORT       = "key_port";

    private static String  serverUrl = loadServerUrl();
    private static Integer port      = loadPort();

    private BackendServer() {
    }

    private static String loadServerUrl() {
        return SharedPrefsManager.getStringFromPrefs(KEY_SERVER_URL, null);
    }

    private static Integer loadPort() {
        return SharedPrefsManager.getIntFromPrefs(KEY_PORT, 0);
    }

    private static void loadServer() {
        serverUrl = loadServerUrl();
        port      = loadPort();
    }

    private static void saveServer() {
        SharedPrefsManager.putStringInPrefs(KEY_SERVER_URL, serverUrl);
        SharedPrefsManager.putIntInPrefs(KEY_PORT, port);
    }

    public static boolean isConfigured() {
        return (serverUrl != null) && (port != 0);
    }

    public static String getServerUrl() {
        return serverUrl;
    }

    public static void setServerUrl(String serverUrl) {
        if (serverUrl != null && serverUrl.isEmpty()) serverUrl = null;
        BackendServer.serverUrl = serverUrl;

        saveServer();
    }

    public static Integer getPort() {
        return port;
    }

    public static void setPort(Integer port) {
        BackendServer.port = port;

        saveServer();
    }
}
