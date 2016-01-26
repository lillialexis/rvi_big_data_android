package com.jaguarlandrover.dynamicagents;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2016 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    Signal.java
 * Project: DynamicAgents
 *
 * Created by Lilli Szafranski on 1/26/16.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import com.google.gson.annotations.SerializedName;

public class Signal
{
    private final static String TAG = "DynamicAgents:Signal";

    @SerializedName("id")
    private String mId             = "";

    @SerializedName("interface")
    private String mInterface      = "";
    @SerializedName("name")
    private String mName           = "";

    @SerializedName("description")
    private String mDescription    = "";

    @SerializedName("type")
    private String mType           = "";

    @SerializedName("interval")
    private String mInterval       = "";

    public Signal() {
    }
}
