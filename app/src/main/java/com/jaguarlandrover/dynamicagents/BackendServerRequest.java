package com.jaguarlandrover.dynamicagents;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2016 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    BackendServerRequest.java
 * Project: DynamicAgents
 *
 * Created by Lilli Szafranski on 1/26/16.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.annotation.SuppressLint;
import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.Map;

@SuppressLint("LongLogTag")
public class BackendServerRequest
{
    private final static String TAG = "DynamicAgents:BackendServerRequest";

    public enum RequestMethod
    {
        VEHICLE_DATA("vehicle_data"),
        AGENT_SUBSCRIBE("agent_subscribe"),
        DA_DATA("da_data"),
        NONE("none");

        private final String mIdentifier;

        RequestMethod(String identifier) {
            mIdentifier = identifier;
        }

        public final String value() {
            return mIdentifier;
        }

        public static RequestMethod get(String identifier) {
            switch (identifier) {
                case "vehicle_data": return VEHICLE_DATA;
                case "agent_subscribe": return AGENT_SUBSCRIBE;
                case "da_data": return DA_DATA;

            }

            return NONE;
        }
    }

    public interface BackendServerRequestListener {
        void onDidSucceed(String data);
        void onDidFail(Throwable error);
    }

    private static String baseUrl = "";
    private static String pathComponent = "";

    private static RequestQueue requestQueue = Volley.newRequestQueue(DynamicAgentsApplication.getContext());

    private RequestMethod mRequestMethod = RequestMethod.NONE;

    private static Response.Listener<VehicleData> listener = new Response.Listener<VehicleData>()
    {
        @Override
        public void onResponse(VehicleData response) {
            Gson gson = new Gson();
            Log.d(TAG, gson.toJson(response));
        }
    };

    private static Response.ErrorListener errorListener = new Response.ErrorListener()
    {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, error.getLocalizedMessage());
        }
    };

    private Vehicle mVehicle = null;

    private BackendServerRequestListener mListener;

    public BackendServerRequest(RequestMethod requestMethod, Vehicle vehicle) {
        mRequestMethod = requestMethod;
        mVehicle = vehicle;
    }

    public static void setBaseUrl(String baseUrl) {
        BackendServerRequest.baseUrl = baseUrl;
    }

    public static void setPathComponent(String pathComponent) {
        BackendServerRequest.pathComponent = pathComponent;
    }

    public void setListener(BackendServerRequestListener listener) {
        mListener = listener;
    }

    private boolean isStringValid(String string) {
        return string != null && !string.isEmpty();
    }

    private void validateParams() throws InvalidParameterException {
        if (!isStringValid(BackendServerRequest.baseUrl)) throw new InvalidParameterException("Need to set baseUrl");
        if (!isStringValid(BackendServerRequest.pathComponent)) throw new InvalidParameterException("Need to set path component");
        if (!isStringValid(mVehicle.getId())) throw new InvalidParameterException("Need to set vehicle id");
        //if (isStringValid(mVehicle.getPin())) throw new InvalidParameterException("Need to set vehicle pin");
    }

    private String fqurl() {
        return baseUrl + pathComponent + mRequestMethod.value();
    }

    public void send() throws InvalidParameterException {
        validateParams();

        GsonRequest<VehicleData> gsonRequest = new GsonRequest<>(fqurl(), VehicleData.class, null, listener, errorListener);

        requestQueue.add(gsonRequest);
    }

    private void error(Throwable error) {
        if (mListener != null) {
            mListener.onDidFail(error);
        }
    }

    private void success(String data) {
        if (mListener != null) {
            mListener.onDidSucceed(data);
        }
    }

    private class GsonRequest<T> extends Request<T>
    {
        private final Gson gson = new Gson();
        private final Class<T>             clazz;
        private final Map<String, String>  headers;
        private final Response.Listener<T> listener;

        /**
         * Make a GET request and return a parsed object from JSON.
         *
         * @param url URL of the request to make
         * @param clazz Relevant class object, for Gson's reflection
         * @param headers Map of request headers
         */
        public GsonRequest(String url, Class<T> clazz, Map<String, String> headers,
                           Response.Listener<T> listener, Response.ErrorListener errorListener) {
            super(Method.GET, url, errorListener);
            this.clazz = clazz;
            this.headers = headers;
            this.listener = listener;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return headers != null ? headers : super.getHeaders();
        }

        @Override
        protected void deliverResponse(T response) {
            listener.onResponse(response);
        }

        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            try {
                String json = new String(
                        response.data,
                        HttpHeaderParser.parseCharset(response.headers));
                return Response.success(
                        gson.fromJson(json, clazz),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
        }
    }
}
