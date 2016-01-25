package com.jaguarlandrover.dynamicagents;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2016 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    VehicleTest.java
 * Project: DynamicAgents
 *
 * Created by Lilli Szafranski on 1/22/16.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.test.AndroidTestCase;
import android.util.Log;

public class VehicleTest extends AndroidTestCase
{
    private final static String TAG = "DynamicAgents:VehicleTest";

    private final static String TEST_ID_1 = "ONE";
    private final static String TEST_ID_2 = "TWO";
    private final static String TEST_ID_3 = "THREE";

    private final static String TEST_PIN_1 = "123";
    private final static String TEST_PIN_2 = "456";
    private final static String TEST_PIN_3 = "789";

    public VehicleTest() {
        super();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();

    }

    /**
     * Test something
     */
    public final void testInitialization() {
        Vehicle v = new Vehicle();

        assertEquals(v.getId(), null);
        assertEquals(v.getPin(), null);
        assertFalse(v.getRememberPin());
        assertFalse(v.isConfigured());

        v = new Vehicle();

        v.setId(TEST_ID_1);
        v.setRememberPin(true);
        v.setPin(TEST_PIN_1);

        assertTrue(v.isConfigured());

        v = new Vehicle(null, null, false);

        assertEquals(v.getId(), null);
        assertEquals(v.getPin(), null);
        assertFalse(v.getRememberPin());
        assertFalse(v.isConfigured());

        v = new Vehicle(TEST_ID_2, null, false);

        assertEquals(v.getId(), TEST_ID_2);
        assertEquals(v.getPin(), null);
        assertFalse(v.getRememberPin());
        assertTrue(v.isConfigured());

        v = new Vehicle(null, TEST_PIN_2, false);

        assertEquals(v.getId(), null);
        assertEquals(v.getPin(), null);
        assertFalse(v.getRememberPin());
        assertFalse(v.isConfigured());

        v = new Vehicle(null, null, true);

        assertEquals(v.getId(), null);
        assertEquals(v.getPin(), null);
        assertTrue(v.getRememberPin());
        assertFalse(v.isConfigured());

        v = new Vehicle(TEST_ID_3, TEST_PIN_3, false);

        assertEquals(v.getId(), TEST_ID_3);
        assertEquals(v.getPin(), null);
        assertFalse(v.getRememberPin());
        assertTrue(v.isConfigured());

        v = new Vehicle(TEST_ID_3, null, true);

        assertEquals(v.getId(), TEST_ID_3);
        assertEquals(v.getPin(), null);
        assertTrue(v.getRememberPin());
        assertFalse(v.isConfigured());

        v = new Vehicle(null, TEST_PIN_3, true);

        assertEquals(v.getId(), null);
        assertEquals(v.getPin(), TEST_PIN_3);
        assertTrue(v.getRememberPin());
        assertFalse(v.isConfigured());

        v = new Vehicle(TEST_ID_3, TEST_PIN_3, true);

        assertEquals(v.getId(), TEST_ID_3);
        assertEquals(v.getPin(), TEST_PIN_3);
        assertTrue(v.getRememberPin());
        assertTrue(v.isConfigured());

    }

    public final void testSetId() {
        Vehicle v = new Vehicle(null, null, false);

        assertEquals(v.getId(), null);
        assertFalse(v.isConfigured());

        v.setId("");

        assertEquals(v.getId(), null);
        assertFalse(v.isConfigured());

        v.setId(TEST_ID_1);

        assertEquals(v.getId(), TEST_ID_1);
        assertTrue(v.isConfigured());

        v.setId(null);

        assertEquals(v.getId(), null);
        assertFalse(v.isConfigured());
    }

    public final void testSetPin() {
        Vehicle v = new Vehicle(TEST_ID_1, null, false);

        assertEquals(v.getPin(), null);
        assertTrue(v.isConfigured());

        v.setPin("");

        assertEquals(v.getPin(), null);
        assertTrue(v.isConfigured());

        v.setPin(TEST_PIN_1);

        assertEquals(v.getPin(), null);
        assertTrue(v.isConfigured());

        v.setPin(null);

        assertEquals(v.getPin(), null);
        assertTrue(v.isConfigured());

        v.setRememberPin(true);
        v.setPin("");

        assertEquals(v.getPin(), null);
        assertFalse(v.isConfigured());

        v.setPin(TEST_PIN_1);

        assertEquals(v.getPin(), TEST_PIN_1);
        assertTrue(v.isConfigured());

        v.setPin(null);

        assertEquals(v.getPin(), null);
        assertFalse(v.isConfigured());
    }

    public final void testSetRememberPin() {
        Vehicle v = new Vehicle(TEST_ID_1, null, true);

        assertEquals(v.getPin(), null);
        assertFalse(v.isConfigured());

        v.setPin(TEST_PIN_1);

        assertEquals(v.getPin(), TEST_PIN_1);
        assertTrue(v.isConfigured());

        v.setRememberPin(false);

        assertEquals(v.getPin(), null);
        assertTrue(v.isConfigured());

        v.setRememberPin(true);

        assertEquals(v.getPin(), null);
        assertFalse(v.isConfigured());
    }

    public final void testEquals() {
        Vehicle v1 = new Vehicle();
        Vehicle v2 = new Vehicle();

        assertTrue(v1.equals(v2));

        v1.setId(TEST_ID_1);

        assertFalse(v1.equals(v2));

        v2.setId(TEST_ID_2);

        assertFalse(v1.equals(v2));

        v2.setId(TEST_ID_1);

        assertTrue(v1.equals(v2));

        v1 = new Vehicle();
        v2 = new Vehicle();

        assertTrue(v1.equals(v2));

        v1.setRememberPin(true);

        assertFalse(v1.equals(v2));

        v2.setRememberPin(true);

        assertTrue(v1.equals(v2));

        v1.setPin(TEST_PIN_1);

        assertFalse(v1.equals(v2));

        v2.setPin(TEST_PIN_2);

        assertFalse(v1.equals(v2));

        v2.setPin(TEST_PIN_1);

        assertTrue(v1.equals(v2));

        v1.setId(TEST_ID_3);

        assertFalse(v1.equals(v2));

        v2.setId(TEST_ID_1);

        assertFalse(v1.equals(v2));

        v2.setId(TEST_ID_3);

        assertTrue(v1.equals(v2));

        v1.setPin(TEST_PIN_3);

        assertFalse(v1.equals(v2));

        v2.setPin(null);

        assertFalse(v1.equals(v2));

        v1.setRememberPin(false);
        v2.setRememberPin(false);

        assertTrue(v1.equals(v2));
    }

}
