package com.jaguarlandrover.dynamicagents;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2016 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    BackendServerTest.java
 * Project: DynamicAgents
 *
 * Created by Lilli Szafranski on 1/22/16.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.test.AndroidTestCase;

import java.lang.reflect.Method;

public class BackendServerTest extends AndroidTestCase
{
    private final static String TAG = "DynamicAgents:BackendServerTest";

    private final static String KEY_SERVER_URL = "key_server_url";
    private final static String KEY_PORT       = "key_port";

    private final static String TEST_URL_1 = "http://google.com";

    private String  mSavedUrlPrefsVal;
    private Integer mSavedPortPrefsVal;

    public BackendServerTest() {
        super();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        mSavedUrlPrefsVal = getUrlPrefsVal();
        mSavedPortPrefsVal = getPortPrefsVal();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();

        setUrlPrefsVal(mSavedUrlPrefsVal);
        setPortPrefsVal(mSavedPortPrefsVal);
    }

    private void setUrlPrefsVal(String newPrefsString) {
        SharedPrefsManager.putStringInPrefs(KEY_SERVER_URL, newPrefsString);
        loadBackendServer();
    }

    private String getUrlPrefsVal() {
        return SharedPrefsManager.getStringFromPrefs(KEY_SERVER_URL, null);
    }

    private void setPortPrefsVal(Integer newPrefsInt) {
        SharedPrefsManager.putIntInPrefs(KEY_PORT, newPrefsInt);
        loadBackendServer();
    }

    private Integer getPortPrefsVal() {
        return SharedPrefsManager.getIntFromPrefs(KEY_PORT, 0);
    }

    private void clearPrefs() {
        SharedPrefsManager.deleteValueFromPrefs(KEY_SERVER_URL);
        SharedPrefsManager.deleteValueFromPrefs(KEY_PORT);

        loadBackendServer();
    }

    private void loadBackendServer() {
        try {
            Method method = BackendServer.class.getDeclaredMethod("loadServer");
            method.setAccessible(true);

            method.invoke(null);
        } catch (Exception e) {
            assertTrue("Exception thrown when trying to load VehicleManager", false);
            e.printStackTrace();
        }
    }

    /**
     * Test something
     */
    public final void testSettingUrlAndPort() {
        setPortPrefsVal(0);
        setUrlPrefsVal(null);

        assertEquals(BackendServer.getServerUrl(), null);
        assertTrue(BackendServer.getPort() == 0);
        assertFalse(BackendServer.isConfigured());

        BackendServer.setServerUrl("");

        assertFalse(BackendServer.isConfigured());
        assertEquals(BackendServer.getServerUrl(), null);
        assertEquals(getUrlPrefsVal(), null);

        BackendServer.setServerUrl(null);

        assertFalse(BackendServer.isConfigured());
        assertEquals(BackendServer.getServerUrl(), null);
        assertEquals(getUrlPrefsVal(), null);

        BackendServer.setPort(1);

        assertFalse(BackendServer.isConfigured());
        assertTrue(BackendServer.getPort() == 1);
        assertTrue(getPortPrefsVal() == 1);

        BackendServer.setServerUrl(TEST_URL_1);

        assertTrue(BackendServer.isConfigured());
        assertEquals(BackendServer.getServerUrl(), TEST_URL_1);
        assertEquals(getUrlPrefsVal(), TEST_URL_1);

        BackendServer.setPort(0);

        assertFalse(BackendServer.isConfigured());
        assertTrue(BackendServer.getPort() == 0);
        assertTrue(getPortPrefsVal() == 0);

        clearPrefs();

        assertEquals(BackendServer.getServerUrl(), null);
        assertTrue(BackendServer.getPort() == 0);
        assertFalse(BackendServer.isConfigured());
    }
}
