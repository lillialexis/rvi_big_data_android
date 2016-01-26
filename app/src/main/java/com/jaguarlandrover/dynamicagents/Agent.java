package com.jaguarlandrover.dynamicagents;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2016 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    Agent.java
 * Project: DynamicAgents
 *
 * Created by Lilli Szafranski on 1/26/16.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Agent
{
    private final static String TAG = "DynamicAgents:Agent";

    @SerializedName("id")
    private String mId          = "";

    @SerializedName("name")
    private String mName        = "";

    @SerializedName("description")
    private String mDescription = "";

    @SerializedName("signals")
    private ArrayList<Signal> mSignals = null;

    public Agent() {
    }
}
