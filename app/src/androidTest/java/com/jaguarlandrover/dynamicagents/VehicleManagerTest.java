package com.jaguarlandrover.dynamicagents;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2016 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    VehicleManager.java
 * Project: DynamicAgents
 *
 * Created by Lilli Szafranski on 1/22/16.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.annotation.SuppressLint;
import android.test.AndroidTestCase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

@SuppressLint("LongLogTag")
public class VehicleManagerTest extends AndroidTestCase
{
    private final static String TAG = "DynamicAgents:VehicleManager";

    private final static String KEY_VEHICLE_ARRAY = "key_vehicle_array";

    private final static String TEST_ID_1 = "ONE";
    private final static String TEST_ID_2 = "TWO";
    private final static String TEST_ID_3 = "THREE";

    private final static String TEST_PIN_1 = "123";
    private final static String TEST_PIN_2 = "456";
    private final static String TEST_PIN_3 = "789";

    private final static String TEST_STR_1 = "[{\"id\":\"ONE\",\"remember_pin\":false}]";
    private final static String TEST_STR_2 = "[{\"id\":\"ONE\",\"remember_pin\":true}]";
    private final static String TEST_STR_3 = "[{\"id\":\"ONE\",\"pin\":\"123\",\"remember_pin\":true}]";
    private final static String TEST_STR_4 = "[{\"id\":\"ONE\",\"pin\":\"456\",\"remember_pin\":true}]";
    private final static String TEST_STR_5 = "[{\"id\":\"ONE\",\"pin\":\"456\",\"remember_pin\":true},{\"id\":\"TWO\",\"remember_pin\":true},{\"id\":\"THREE\",\"remember_pin\":false}]";
    private final static String TEST_STR_6 = "[{\"id\":\"ONE\",\"pin\":\"456\",\"remember_pin\":true},{\"id\":\"TWO\",\"pin\":\"789\",\"remember_pin\":true},{\"id\":\"THREE\",\"pin\":\"123\",\"remember_pin\":true}]";
    private final static String TEST_STR_7 = "[{\"id\":\"ONE\",\"pin\":\"456\",\"remember_pin\":true},{\"id\":\"TWO\",\"pin\":\"789\",\"remember_pin\":true}]";

    private String mSavedPrefsString;

    public VehicleManagerTest() {
        super();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        mSavedPrefsString = getVehiclePrefsString();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();

        setVehiclePrefsString(mSavedPrefsString);
    }

    private void setVehiclePrefsString(String newPrefsString) {
        SharedPrefsManager.putStringInPrefs(KEY_VEHICLE_ARRAY, newPrefsString);
        loadVehicleManager();
    }

    private String getVehiclePrefsString() {
        return SharedPrefsManager.getStringFromPrefs(KEY_VEHICLE_ARRAY, null);
    }

    private void loadVehicleManager() {
        try {
            Field field = VehicleManager.class.getDeclaredField("vehicles");
            field.setAccessible(true);

            Method method = VehicleManager.class.getDeclaredMethod("loadVehicles");
            method.setAccessible(true);

            field.set(null, method.invoke(null));

        } catch (Exception e) {
            assertTrue("Exception thrown when trying to load VehicleManager", false);
            e.printStackTrace();
        }
    }

    private void saveVehicleManager() {
        try {
            Method method = VehicleManager.class.getDeclaredMethod("saveVehicles");
            method.setAccessible(true);
            method.invoke(null);
        } catch (Exception e) {
            assertTrue("Exception thrown when trying to save VehicleManager", false);
            e.printStackTrace();
        }
    }

    /**
     * Test something
     */
    public final void testNullInPrefs() {
        setVehiclePrefsString(null);

        assertTrue(VehicleManager.getVehicles().size() == 1);
        assertFalse(VehicleManager.isConfigured());

        Vehicle vehicle = VehicleManager.getVehicles().get(0);

        assertFalse(vehicle.isConfigured());
        assertEquals(null, vehicle.getId());
        assertEquals(null, vehicle.getPin());
        assertFalse(vehicle.getRememberPin());
    }

    public final void testVehicleManagerUpdateListener() {
        /* Test the update listener of the empty vehicle VM was initialized with when prefs str was null */
        setVehiclePrefsString(null);

        assertNull(getVehiclePrefsString());

        Vehicle vehicle1 = VehicleManager.getVehicles().get(0);

        assertFalse(VehicleManager.isConfigured());
        assertFalse(vehicle1.isConfigured());

        vehicle1.setId(TEST_ID_1);
        assertEquals(TEST_STR_1, getVehiclePrefsString());
        assertTrue(VehicleManager.isConfigured());
        assertTrue(vehicle1.isConfigured());

        vehicle1.setPin(TEST_PIN_1); /* Shouldn't fire update event because 'mRememberPin' is still false */
        assertEquals(TEST_STR_1, getVehiclePrefsString());
        assertTrue(VehicleManager.isConfigured());
        assertTrue(vehicle1.isConfigured());

        vehicle1.setRememberPin(true);
        assertEquals(TEST_STR_2, getVehiclePrefsString());
        assertFalse(VehicleManager.isConfigured());
        assertFalse(vehicle1.isConfigured());

        /* Test the update listener of the vehicle VM was initialized with from non-null prefs string */
        setVehiclePrefsString(TEST_STR_3);

        Vehicle vehicle2 = VehicleManager.getVehicles().get(0);

        vehicle2.setPin(TEST_PIN_2);
        assertEquals(TEST_STR_4, getVehiclePrefsString());
        assertTrue(VehicleManager.isConfigured());
        assertTrue(vehicle2.isConfigured());

        /* Test the update listener of newly added vehicles */
        Vehicle vehicle3 = new Vehicle();
        Vehicle vehicle4 = new Vehicle();

        assertEquals(TEST_STR_4, getVehiclePrefsString());
        assertTrue(VehicleManager.isConfigured());

        vehicle3.setId(TEST_ID_2); /* Not added to VM, so nothing is fired yet */
        vehicle4.setId(TEST_ID_3); /* Not added to VM, so nothing is fired yet */

        assertEquals(TEST_STR_4, getVehiclePrefsString());
        assertTrue(VehicleManager.isConfigured());

        VehicleManager.addVehicle(vehicle3);
        VehicleManager.addVehicle(vehicle4);

        vehicle3.setRememberPin(true);

        assertEquals(TEST_STR_5, getVehiclePrefsString());
        assertFalse(VehicleManager.isConfigured());

        vehicle4.setRememberPin(true);

        vehicle3.setPin(TEST_PIN_3);
        vehicle4.setPin(TEST_PIN_1);

        assertEquals(TEST_STR_6, getVehiclePrefsString());

        /* Test the update listener of removed vehicles */
        VehicleManager.removeVehicle(vehicle4);

        assertEquals(TEST_STR_7, getVehiclePrefsString());

        vehicle4.setId(TEST_ID_1);

        assertEquals(TEST_STR_7, getVehiclePrefsString());
    }


    public final void testIncompleteConfiguration() {
        setVehiclePrefsString(null);

        Vehicle vehicle1 = VehicleManager.getVehicles().get(0);

        assertFalse(VehicleManager.isConfigured());
        assertFalse(vehicle1.isConfigured());

        Vehicle vehicle2 = new Vehicle(TEST_ID_2, TEST_PIN_2, true);
        Vehicle vehicle3 = new Vehicle(TEST_ID_3, null, true);

        assertTrue(vehicle2.isConfigured());
        assertFalse(vehicle3.isConfigured());

        VehicleManager.addVehicle(vehicle2);
        VehicleManager.addVehicle(vehicle3);

        assertFalse(VehicleManager.isConfigured());

        vehicle1.setPin(TEST_PIN_1);

        assertFalse(VehicleManager.isConfigured());

        vehicle1.setId(TEST_ID_1);

        assertTrue(vehicle1.isConfigured());
        assertFalse(VehicleManager.isConfigured());

        vehicle1.setRememberPin(true);

        assertFalse(vehicle1.isConfigured());
        assertFalse(VehicleManager.isConfigured());

        vehicle1.setPin(TEST_PIN_1);

        assertTrue(vehicle1.isConfigured());
        assertFalse(VehicleManager.isConfigured());

        vehicle3.setPin(TEST_PIN_3);

        assertTrue(vehicle3.isConfigured());
        assertTrue(VehicleManager.isConfigured());

        vehicle2.setPin("");

        assertFalse(vehicle2.isConfigured());
        assertFalse(VehicleManager.isConfigured());

        vehicle2.setPin(null);

        assertFalse(vehicle2.isConfigured());
        assertFalse(VehicleManager.isConfigured());

        vehicle2.setRememberPin(false);

        assertTrue(vehicle2.isConfigured());
        assertTrue(VehicleManager.isConfigured());
    }

    public final void testIncompleteConfigurationMultipleVehiclesLoadedFromPrefs() {
        setVehiclePrefsString(TEST_STR_6);

        Vehicle vehicle1 = VehicleManager.getVehicles().get(0);
        Vehicle vehicle2 = VehicleManager.getVehicles().get(1);
        Vehicle vehicle3 = VehicleManager.getVehicles().get(2);

        assertTrue(vehicle1.isConfigured());
        assertTrue(vehicle2.isConfigured());
        assertTrue(vehicle3.isConfigured());
        assertTrue(VehicleManager.isConfigured());

        vehicle1.setId("");

        assertFalse(vehicle1.isConfigured());
        assertFalse(VehicleManager.isConfigured());

        vehicle1.setId(null);

        assertFalse(vehicle1.isConfigured());
        assertFalse(VehicleManager.isConfigured());

        vehicle1.setId(TEST_ID_1);
        vehicle2.setPin("");

        assertFalse(vehicle2.isConfigured());
        assertFalse(VehicleManager.isConfigured());

        vehicle2.setRememberPin(false);

        assertTrue(vehicle2.isConfigured());
        assertTrue(VehicleManager.isConfigured());
    }

    public final void testAddingVehicles() {
        /* Tested elsewhere for the most part */

        /* What happens if we modify this directly? */
        setVehiclePrefsString(TEST_STR_6);

        ArrayList<Vehicle> vehicles = VehicleManager.getVehicles();

        assertTrue(vehicles.size() == 3);

        vehicles.add(new Vehicle());

        assertTrue(vehicles.size() == 4);
        assertTrue(VehicleManager.getVehicles().size() == 4);
    }

    public final void testRemovingVehicles() {
        /* Tested elsewhere */
    }

    public final void testReorderingVehicles() {

    }

    public final void testInvalidVehicles() {
        setVehiclePrefsString("not a valid vehicle string");

        assertTrue(VehicleManager.getVehicles().size() == 1);

        Vehicle vehicle = VehicleManager.getVehicles().get(0);

        assertEquals(vehicle.getId(), null);
        assertEquals(vehicle.getPin(), null);
        assertFalse(vehicle.getRememberPin());
    }
}
