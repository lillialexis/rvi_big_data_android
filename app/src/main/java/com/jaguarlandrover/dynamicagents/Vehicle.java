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

    @SerializedName("pin")
    private String mPin = null;

    @SerializedName("remember_pin")
    private Boolean mRememberPin = false;

    private transient ArrayList<VehicleUpdateListener> mListeners = new ArrayList<>();

    public Vehicle() {
    }

    public Vehicle(String id, String pin, boolean rememberPin) {
        setId(id);
        setRememberPin(rememberPin);
        setPin(pin);
    }

    public String getPin() {
        return mPin;
    }

    public void setPin(String pin) {
        if (!mRememberPin) return;

        if (pin != null && pin.isEmpty()) pin = null;
        mPin = pin;

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

    public Boolean getRememberPin() {
        return mRememberPin;
    }

    public void setRememberPin(Boolean rememberPin) {
        if (!rememberPin) setPin(null);

        mRememberPin = rememberPin;
        fireUpdate();
    }

    public boolean isEqual(Vehicle other) {
        return (this.getId().equals(other.getId()) && this.getPin().equals(other.getPin()) && this.getRememberPin().equals(other.getRememberPin()));
    }

    public boolean isConfigured() {
        return !(mId == null || (mRememberPin && mPin == null));
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
