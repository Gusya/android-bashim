package com.bashim.network;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Gusya on 28/01/2017.
 */

public class AsyncHttpNetworkClient implements INetworkClient {

    private final AsyncHttpClient client;
    private final Context context;

    public AsyncHttpNetworkClient(Context context) {
        this.client = new AsyncHttpClient();
        this.context = context;
    }

    @Override
    public void execute(final Response topLevelResponse) {
        RequestParams params = new RequestParams();
        params.put("site", "bash.im");
        params.put("name", "bash");

        client.get(context, NetworkConstants.SERVER+NetworkConstants.GET_METHOD, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                topLevelResponse.onSuccess(response);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                topLevelResponse.onFailure();
            }
        });
    }

    @Override
    public void stopRequests(Context context) {
        client.cancelAllRequests(true);
    }
}
