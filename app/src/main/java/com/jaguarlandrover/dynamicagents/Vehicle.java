package com.jaguarlandrover.dynamicagents;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2016 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    Vehicle.java
 * Project: DynamicAgents
 *
 * Created by Lilli Szafranski on 1/21/16.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Vehicle
{
    private final static String TAG = "DynamicAgents:Vehicle";

    @SerializedName("id")
    private String mId = null;

    @SerializedName("stored_pin")
    private String mStoredPin = null;

    @SerializedName("should_store_pin")
    private Boolean mShouldStorePin = false;

    private transient String mPin = null;

    private transient ArrayList<VehicleUpdateListener> mListeners = new ArrayList<>();

    public Vehicle() {
    }

    public Vehicle(String id, String storedPin, boolean shouldStorePin) {
        setId(id);
        setShouldStorePin(shouldStorePin);
        setStoredPin(storedPin);
    }

    public String getStoredPin() {
        return mStoredPin;
    }

    public void setStoredPin(String storedPin) {
        if (!mShouldStorePin) return;

        if (storedPin != null && storedPin.isEmpty()) storedPin = null;
        mStoredPin = storedPin;

        fireUpdate();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        if (id != null && id.isEmpty()) id = null;
        mId = id;

        fireUpdate();
    }

    public Boolean getShouldStorePin() {
        return mShouldStorePin;
    }

    public void setShouldStorePin(Boolean shouldStorePin) {
        if (!shouldStorePin) setStoredPin(null);

        mShouldStorePin = shouldStorePin;
        fireUpdate();
    }

    public String getPin() {
        if (mStoredPin != null) return mStoredPin;

        return mPin;
    }

    public void setPin(String pin) {
        if (pin != null && pin.isEmpty()) pin = null;

        mPin = pin;
    }

    public boolean equals(Vehicle other) {

        if (this.getId() == null && other.getId() != null) return false;
        if (this.getId() != null && !this.getId().equals(other.getId())) return false;

        if (this.getStoredPin() == null && other.getStoredPin() != null) return false;
        if (this.getStoredPin() != null && !this.getStoredPin().equals(other.getStoredPin())) return false;

        if (this.getShouldStorePin() != other.getShouldStorePin()) return false;

        return true;
    }

    public boolean isConfigured() {
        return !(mId == null || (mShouldStorePin && mStoredPin == null));
    }

    public void addVehicleUpdateListener(VehicleUpdateListener listener) {
        mListeners.add(listener);
    }

    public void removeVehicleUpdateListener(VehicleUpdateListener listener) {
        mListeners.remove(listener);
    }

    private void fireUpdate() {
        for (VehicleUpdateListener listener : mListeners)
            listener.onVehicleUpdated();
    }

    public interface VehicleUpdateListener
    {
        void onVehicleUpdated();
    }
}
