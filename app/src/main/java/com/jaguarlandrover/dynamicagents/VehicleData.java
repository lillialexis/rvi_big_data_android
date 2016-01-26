package com.jaguarlandrover.dynamicagents;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2016 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    VehicleData.java
 * Project: DynamicAgents
 *
 * Created by Lilli Szafranski on 1/26/16.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VehicleData
{
    private final static String TAG = "DynamicAgents:VehicleData";

    @SerializedName("vin")
    private String mVin = "";

    @SerializedName("is_running")
    private Boolean mIsRunning = false;

    @SerializedName("last_ping_time")
    private String mLastPingTime = "";

    @SerializedName("agents")
    private ArrayList<Agent> mAgents;

    public VehicleData() {
    }
}
