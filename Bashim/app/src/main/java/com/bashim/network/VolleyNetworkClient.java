package com.bashim.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

/**
 * Created by Gusya on 28/01/2017.
 */

public class VolleyNetworkClient implements INetworkClient{

    //TAG to associate with every request
    public static final String TAG = "MY_REQ_TAG";

    private final RequestQueue requestQueue;

    public VolleyNetworkClient(final Context context) {
        //create request queue with default parameters
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void execute(final Response netClientResponse) {
        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(

                //define request URL
                NetworkConstants.SERVER + NetworkConstants.GET_METHOD + NetworkConstants.GET_FROM_BASH_IM,

                //define response listener
                new Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        netClientResponse.onSuccess(response);
                    }
                },

                //define error listener
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        netClientResponse.onFailure();
                    }
                });

        jsonArrayRequest.setTag(TAG);
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void stopRequests(Context context) {
        requestQueue.cancelAll(TAG);
    }
}
