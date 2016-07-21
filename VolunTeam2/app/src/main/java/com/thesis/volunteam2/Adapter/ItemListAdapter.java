package com.thesis.volunteam2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thesis.volunteam2.EventDetails_Activity;
import com.thesis.volunteam2.Model.ListItem;
import com.thesis.volunteam2.R;
import com.thesis.volunteam2.Manager.ReturnHost;


import java.util.List;

/**
 * Created by Jeysown on 7/10/2016.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    List<ListItem> data;
    ReturnHost rhost = new ReturnHost();
    String host = rhost.returnHost();
    private String JSON_URL = "http://" + host + "//volunteam/pictures";

    public ItemListAdapter(Context context,List<ListItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ItemListAdapter.ItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.list_cardview, parent,false);
        ItemListViewHolder holder=new ItemListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemListAdapter.ItemListViewHolder holder, int position) {

        ListItem current = data.get(position);
        holder.txt_id.setText(current.id);
        holder.txt_event_name.setText(current.name);
        holder.txt_event_location.setText(current.location);
        holder.txt_event_date.setText(current.date);
        holder.txt_event_fromTime.setText(current.time);
        Picasso.with(context)
                .load(JSON_URL+"/"+current.img)
                .placeholder(R.drawable.org)
                .error(R.drawable.org)
                .into(holder.txt_event_img);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String pid = ((TextView)v.findViewById(R.id.id)).getText().toString();
                bundle.putString("id",pid);
                Intent intent = new Intent(context,EventDetails_Activity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ItemListViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        TextView  txt_id;
        ImageView txt_event_img;
        TextView txt_event_name;
        TextView txt_event_location;
        TextView txt_event_fromTime;
        TextView txt_event_date;
        public ItemListViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            txt_id= (TextView) itemView.findViewById(R.id.id);
            txt_event_img= (ImageView) itemView.findViewById(R.id.event_img);
            txt_event_name = (TextView) itemView.findViewById(R.id.eventName);
            txt_event_location = (TextView) itemView.findViewById(R.id.location);
            txt_event_date = (TextView) itemView.findViewById(R.id.date);
            txt_event_fromTime = (TextView) itemView.findViewById(R.id.fromTime);
        }
    }
}
