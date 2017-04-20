package com.bashim;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bashim.network.AsyncHttpNetworkClient;
import com.bashim.network.INetworkClient;
import com.bashim.network.OkHttpNetworkClient;
import com.bashim.network.RetrofitNetworkClient;
import com.bashim.network.VolleyNetworkClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private BashItemRecyclerAdapter recyclerAdapter;
    private INetworkClient netClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //enable swipe-to-refresh functionality
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.activity_main);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BashItemData fakeItem = new BashItemData(
                        "Fake site",
                        "Fake name",
                        "Fake Desc",
                        "%2F01234556789",
                        "True story"
                        );
                recyclerAdapter.insertItem(fakeItem, 0);
                recyclerView.scrollToPosition(0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recycler_veiw);

        //use linear layout manager
        RecyclerView.LayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        //use BashItemData list with adapter
        recyclerAdapter = new BashItemRecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);

        //add divider between elements
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        //Swipe support
        //We delete item on swipe gesture
        ItemTouchHelper mIth = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                                   RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        // remove from adapter
                        int pos = viewHolder.getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION) {
                            Toast.makeText(MainActivity.this,
                                    ""+pos,
                                    Toast.LENGTH_SHORT).show();
                            recyclerAdapter.removeItem(pos);
                        }

                    }
                });
        mIth.attachToRecyclerView(recyclerView);

        //it's a clear start, so just fetch some new data
        if(netClient == null){
            //enable visual refresh
            swipeRefreshLayout.setRefreshing(true);

            //default http library is retrofit
            netClient = new RetrofitNetworkClient(getApplicationContext());
            requestData(netClient);
        }
    }

//    public void sendRequest(View view){
//
//        //for sake of simplicity just clear all previous data in adapter
//        recyclerAdapter.removeAll();
//
//        //create network client
//        switch (view.getId()){
//            case R.id.volley_button:
//                netClient = new VolleyNetworkClient(getApplicationContext());
//                break;
//            case R.id.okhttp_button:
//                netClient = new OkHttpNetworkClient(getApplicationContext());
//                break;
//            case R.id.asynchttp_button:
//                netClient = new AsyncHttpNetworkClient(getApplicationContext());
//                break;
//            case R.id.retrofit_button:
//                netClient = new RetrofitNetworkClient(getApplicationContext());
//                break;
//        }
//
//        //request data with selected http client
//        requestData(netClient);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //if we are going to destroy application
        //we should cancel all ongoing requests
        if(netClient != null) netClient.stopRequests(getApplicationContext());
    }

    private final void requestData(INetworkClient client){
        if(client == null) return;
        //enable visual refresh
        swipeRefreshLayout.setRefreshing(true);

        //perform actual request
        client.execute(new INetworkClient.Response<List<BashItemData>>() {

            @Override
            public void onSuccess(List<BashItemData> result) {
                //we got all data, update adapter
                recyclerAdapter.insertAll(result);
                //and disable visual refresh
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure() {
                Toast.makeText(MainActivity.this,
                        "Failed to get new items!",
                        Toast.LENGTH_SHORT).show();

                //we got an error, so just disable visual refresh
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
