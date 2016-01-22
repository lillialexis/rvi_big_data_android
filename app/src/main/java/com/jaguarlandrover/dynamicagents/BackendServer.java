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

public class BackendServer
{
    private final static String TAG = "DynamicAgents:BackendServer";

    private final static String SERVER_URL_KEY = "server_url_key";
    private final static String PORT_KEY       = "port_key";

    private static BackendServer ourInstance = new BackendServer();

    private static String serverUrl;

    private static Integer port;

    private static boolean isLoaded;

    private BackendServer() {
        //BackendServer.loadServer();
    }

    private static void loadServer() {
        serverUrl = SharedPrefsManager.getStringFromPrefs(SERVER_URL_KEY, null);
        port = SharedPrefsManager.getIntFromPrefs(PORT_KEY, 0);

        isLoaded = true;
    }

    private static void saveServer() {
        SharedPrefsManager.putStringInPrefs(SERVER_URL_KEY, serverUrl);
        SharedPrefsManager.putIntInPrefs(PORT_KEY, port);
    }

    public static boolean isConfigured() {
        if (!isLoaded) loadServer();
        return (serverUrl != null) && (port != null);
    }

    public static String getServerUrl() {
        if (!isLoaded) loadServer();
        return serverUrl;
    }

    public static void setServerUrl(String serverUrl) {
        if (serverUrl.isEmpty()) serverUrl = null;
        BackendServer.serverUrl = serverUrl;

        saveServer();
    }

    public static Integer getPort() {
        if (!isLoaded) loadServer();
        return port;
    }

    public static void setPort(Integer port) {
        BackendServer.port = port;

        saveServer();
    }
}
