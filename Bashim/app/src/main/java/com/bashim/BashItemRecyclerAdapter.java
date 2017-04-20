package com.bashim;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Gusya on 24/01/2017.
 *
 * Adapter for Bash.im items represented as a list
 */

public class BashItemRecyclerAdapter extends RecyclerView.Adapter<BashItemRecyclerAdapter.ViewHolder> {

    private final List<BashItemData> mListData;

    //Constructor accepting list with data
    public BashItemRecyclerAdapter() {
        mListData = new LinkedList<BashItemData>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BashItemRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate item layout file
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_layout, parent, false);
        //we can set additional params here

        //Create ViewHolder object for inflated layout
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(BashItemRecyclerAdapter.ViewHolder holder, int position) {
        //populate provided ViewHolder with data from relevant position
        holder.title.setText(mListData.get(position).getDesc());
        holder.link.setText(mListData.get(position).getUniqueNumber());
        String text = Html.fromHtml(mListData.get(position).getElementPureHtml()).toString();
        holder.descriprion.setText(text);
    }

    // Return the size of list (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mListData.size();
    }

    //Additional methods to add/remove items from data list
    public void insertAll(List<BashItemData> list){
        int oldPos = mListData.size();
        mListData.addAll(list);
        notifyItemRangeChanged(oldPos, list.size());
    }

    public void insertItem(BashItemData data){
        insertItem(data, 0);
    }

    public void insertItem(BashItemData data, int position){
        mListData.add(position, data);
        notifyItemInserted(position);
    }

    public void removeAll(){
        mListData.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        mListData.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(BashItemData data){
        int index = mListData.indexOf(data);
        removeItem(index);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static final class ViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{

        private RelativeLayout rootLayout;
        public TextView title, descriprion, link;

        public ViewHolder(View itemView) {
            super(itemView);
            rootLayout = (RelativeLayout) itemView.findViewById(R.id.root_item_layout);
            title = (TextView)itemView.findViewById(R.id.title_item);
            descriprion = (TextView)itemView.findViewById(R.id.description_item);
            link = (TextView)itemView.findViewById(R.id.link_item);

            rootLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                Toast.makeText(view.getContext(), title.getText(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
