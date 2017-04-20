package com.bashim.network;

import android.content.Context;

import com.bashim.BashItemData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Gusya on 29/01/2017.
 */

public class RetrofitNetworkClient implements INetworkClient {

    private final Retrofit client;
    private Call retrofitCall;

    public RetrofitNetworkClient(Context context) {
        this.client =  new Retrofit.Builder()
                .baseUrl(NetworkConstants.SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void execute(final Response topLevelResponse, int num) {
        BashImService bashImService = client.create(BashImService.class);
        //call for bash.im -> get data as json array -> tranlate to BashItemData with GSON
        retrofitCall = bashImService.getBashImDataItems(num);

        retrofitCall.enqueue(new Callback<List<BashItemData>>() {

            @Override
            public void onResponse(Call<List<BashItemData>> call, retrofit2.Response<List<BashItemData>> response) {
                if(response.isSuccessful()){
                    List<BashItemData> data = response.body();
                    topLevelResponse.onSuccess(data);
                }
                else{
                    topLevelResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<List<BashItemData>> call, Throwable t) {
                topLevelResponse.onFailure();
            }
        });
    }

    @Override
    public void stopRequests(Context context) {
        if(retrofitCall != null){
            retrofitCall.cancel();
        }
    }

    public interface BashImService {

        @GET("get?site=bash.im&name=bash&num={num}")
        Call<List<BashItemData>> getBashImDataItems(@Path("num") int num);
    }
}
