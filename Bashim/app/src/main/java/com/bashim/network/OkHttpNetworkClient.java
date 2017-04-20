package com.bashim.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Gusya on 28/01/2017.
 */

public class OkHttpNetworkClient implements INetworkClient {

    private final OkHttpClient client;
    private Call currentCall;

    public OkHttpNetworkClient(Context context){
        this.client = new OkHttpClient();
    }

    @Override
    public void execute(final Response topLevelResponse) {
        Request request = new Request.Builder()
                .url(NetworkConstants.SERVER+NetworkConstants.GET_METHOD+NetworkConstants.GET_FROM_BASH_IM)
                .build();

        currentCall = client.newCall(request);

        final Handler handler = new Handler(Looper.getMainLooper());
        currentCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        topLevelResponse.onFailure();
                    }
                });
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                ResponseBody rawBody = response.body();
                final JSONArray jsonResponse = parseBodyToJSONArray(rawBody);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        topLevelResponse.onSuccess(jsonResponse);
                    }
                });
            }
        });

    }

    @Override
    public void stopRequests(Context context) {
        if(currentCall != null) currentCall.cancel();
    }

    private JSONArray parseBodyToJSONArray(ResponseBody body){
        JSONArray array;
        try {
            array = new JSONArray(body.string());
        }
        catch (IOException io){
            array = new JSONArray();
        }
        catch (JSONException je){
            array = new JSONArray();
        }
        return array;
    }
}
