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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class VehicleManager implements Vehicle.VehicleUpdateListener
{
    private final static String TAG = "DynamicAgents:VehicleManager";

    private final static String VEHICLE_ARRAY_KEY = "vehicle_array_key";

    private static VehicleManager ourInstance = new VehicleManager();

    private static Gson gson = new Gson();

    private static ArrayList<Vehicle> vehicles = null;
    private static boolean isLoaded = false;

    private VehicleManager() {
        VehicleManager.loadVehicles();
    }

    private static void saveVehicles() {
        String vehiclesPrefsString = gson.toJson(vehicles);

        SharedPrefsManager.putStringInPrefs(VEHICLE_ARRAY_KEY, vehiclesPrefsString);
    }

    private static void loadVehicles() {
        String vehiclesPrefsString = SharedPrefsManager.getStringFromPrefs(VEHICLE_ARRAY_KEY, null);

        if (vehiclesPrefsString != null) {
            Type collectionType = new TypeToken<Collection<Vehicle>>()
            {
            }.getType();

            try {
                Collection<Vehicle> collection = gson.fromJson(vehiclesPrefsString, collectionType);
                vehicles = new ArrayList<>(collection);
            } catch (Exception e) {
                e.printStackTrace();
                vehicles = new ArrayList<>();
            }
        } else {
            vehicles = new ArrayList<>();
        }

        isLoaded = true;
    }

    public static void addVehicle(Vehicle vehicle) {
        if (!isLoaded) loadVehicles();

        vehicle.addVehicleUpdateListener(ourInstance);
        vehicles.add(vehicle);
        saveVehicles();
    }

    public static void removeVehicle(Vehicle vehicle) {
        if (!isLoaded) loadVehicles();

        vehicle.removeVehicleUpdateListener(ourInstance);
        vehicles.remove(vehicle);
        saveVehicles();
    }

    public static void removeAllVehicles() {
        vehicles = null;
        saveVehicles();
    }

    public static ArrayList<Vehicle> getVehicles() {
        if (!isLoaded) loadVehicles();
        return vehicles;
    }

    public static boolean isConfigured() {
        if (!isLoaded) loadVehicles();
        return (vehicles == null);
    }

    @Override
    public void onVehicleUpdated() {
        VehicleManager.saveVehicles();
    }
}
