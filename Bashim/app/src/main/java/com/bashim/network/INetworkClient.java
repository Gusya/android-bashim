package com.bashim.network;

import android.content.Context;

/**
 * Created by Gusya on 28/01/2017.
 */

public interface INetworkClient {

    interface Response<T>{
        void onSuccess(T result);
        void onFailure();
    }

    void execute (Response response, int num);

    void stopRequests(Context context);

}
