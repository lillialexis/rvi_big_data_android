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
 * Created by Lilli Szafranski on 1/21/16.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.annotation.SuppressLint;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

@SuppressLint("LongLogTag")
public class VehicleManager
{
    private final static String TAG = "DynamicAgents:VehicleManager";

    private final static String KEY_VEHICLE_ARRAY = "key_vehicle_array";

    private static Gson gson = new Gson();

    private static Vehicle.VehicleUpdateListener vehicleUpdateListener = new Vehicle.VehicleUpdateListener()
    {
        @Override
        public void onVehicleUpdated() {
            VehicleManager.saveVehicles();
        }
    };

    private static ArrayList<Vehicle> vehicles = loadVehicles();

    private VehicleManager() {
    }

    private static void saveVehicles() {
        String vehiclesPrefsString = gson.toJson(vehicles);

        Log.d(TAG, "Saving vehicles: " + vehiclesPrefsString);

        SharedPrefsManager.putStringInPrefs(KEY_VEHICLE_ARRAY, vehiclesPrefsString);
    }

    private static ArrayList<Vehicle> loadVehicles() {
        assert gson != null;
        assert vehicleUpdateListener != null;

        ArrayList<Vehicle> tempList = new ArrayList<>(Collections.singletonList(new Vehicle()));
        String vehiclesPrefsString = SharedPrefsManager.getStringFromPrefs(KEY_VEHICLE_ARRAY, null);

        Log.d(TAG, "Loading vehicles: " + vehiclesPrefsString);

        if (vehiclesPrefsString != null) {
            Type collectionType = new TypeToken<Collection<Vehicle>>()
            {
            }.getType();

            try {
                Collection<Vehicle> collection = gson.fromJson(vehiclesPrefsString, collectionType);
                tempList = new ArrayList<>(collection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Vehicle vehicle : tempList)
            vehicle.addVehicleUpdateListener(vehicleUpdateListener);

        return tempList;
    }

    public static void addVehicle(Vehicle vehicle) {
        vehicle.addVehicleUpdateListener(vehicleUpdateListener);
        vehicles.add(vehicle);
        saveVehicles();
    }

    public static void removeVehicle(Vehicle vehicle) {
        vehicle.removeVehicleUpdateListener(vehicleUpdateListener);
        vehicles.remove(vehicle);
        saveVehicles();
    }

    public static ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public static boolean isConfigured() {
        for (Vehicle vehicle : vehicles)
            if (!vehicle.isConfigured())
                return false;

        return true;
    }
}
