package com.jaguarlandrover.dynamicagents;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    SettingsActivity.java
 * Project: DynamicAgentsAndroidm
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;


public class SettingsActivity extends ActionBarActivity
{
    TextView mUrlLabel;
    EditText mUrlEditText;
    TextView mPortLabel;
    EditText mPortEditText;

    TextView mVehicleIdLabel;
    EditText mVehicleIdEditText;
    TextView mVehiclePinLabel;
    EditText mVehiclePinEditText;

    Switch mRememberPin;

    Button mSubmitButton;

    Vehicle mVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (VehicleManager.getVehicles() == null || VehicleManager.getVehicles().size() == 0)
            VehicleManager.addVehicle(new Vehicle());

        mVehicle = VehicleManager.getVehicles().get(0);

        mUrlLabel = (TextView) findViewById(R.id.server_url_label);
        mUrlEditText = (EditText) findViewById(R.id.server_url_edit_text);
        mPortLabel = (TextView) findViewById(R.id.server_port_label);
        mPortEditText = (EditText) findViewById(R.id.server_port_edit_text);

        mVehicleIdLabel = (TextView) findViewById(R.id.vehicle_id_label);
        mVehicleIdEditText = (EditText) findViewById(R.id.vehicle_id_edit_text);
        mVehiclePinLabel = (TextView) findViewById(R.id.vehicle_pin_label);
        mVehiclePinEditText = (EditText) findViewById(R.id.vehicle_pin_edit_text);

        mRememberPin = (Switch) findViewById(R.id.remember_pin_switch);

        mSubmitButton = (Button) findViewById(R.id.settings_submit_button);

        mUrlEditText.setText(BackendServer.getServerUrl());
        mPortEditText.setText(BackendServer.getPort().toString());

        if (mVehicle != null) {
            mVehicleIdEditText.setText(mVehicle.getId());
            mVehiclePinEditText.setText(mVehicle.getPin());
        }

        EditText.OnEditorActionListener enterListener = new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        mSubmitButton.performClick();
                        return true;
                    }
                    return false;
                }
            };

        mUrlEditText.setOnEditorActionListener(enterListener);
        mPortEditText.setOnEditorActionListener(enterListener);

        mVehicleIdEditText.setOnEditorActionListener(enterListener);
        mVehiclePinEditText.setOnEditorActionListener(enterListener);

        mRememberPin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton rememberPin, boolean isChecked) {
                updateRememberPin(isChecked);
            }
        });

        updateRememberPin(mVehicle.getRememberPin());
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_settings, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateRememberPin(boolean isChecked) {
        if (isChecked) {
            mVehiclePinEditText.setVisibility(View.VISIBLE);
            mVehiclePinLabel.setVisibility(View.VISIBLE);
        } else {
            mVehiclePinEditText.setVisibility(View.GONE);
            mVehiclePinLabel.setVisibility(View.GONE);
        }

        mVehicle.setRememberPin(isChecked);
    }

    public void settingsSubmitButtonClicked(View view) {
        // TODO: Validate input (correct URLs, etc.)

        BackendServer.setServerUrl(mUrlEditText.getText().toString().trim());
        BackendServer.setPort(mPortEditText.getText().toString().isEmpty() ? 0 : Integer.parseInt((mPortEditText.getText().toString().trim())));

        mVehicle.setId(mVehicleIdEditText.getText().toString().trim());
        mVehicle.setPin(mVehiclePinEditText.getText().toString().trim());

        if (BackendServer.isConfigured() && mVehicle.isConfigured())
            finish();
    }

    public void settingsCancelButtonClicked(View view) {
        if (BackendServer.isConfigured() && mVehicle.isConfigured())
            finish();
    }


}
