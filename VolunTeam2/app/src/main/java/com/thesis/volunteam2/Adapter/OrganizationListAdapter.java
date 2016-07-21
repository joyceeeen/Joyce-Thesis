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
import com.thesis.volunteam2.Manager.ReturnHost;
import com.thesis.volunteam2.Model.OrganizationList;
import com.thesis.volunteam2.OrganizationProfile_Activity;
import com.thesis.volunteam2.R;

import java.util.List;

/**
 * Created by Jeysown on 7/21/2016.
 */

public class OrganizationListAdapter extends RecyclerView.Adapter<OrganizationListAdapter.OrganizationListHolder>  {
    ReturnHost rhost = new ReturnHost();
    String host = rhost.returnHost();
    private String JSON_URL = "http://" + host + "//volunteam/pictures";
    private Context context;
    private LayoutInflater inflater;
    List<OrganizationList> data;


    public OrganizationListAdapter(Context context, List<OrganizationList> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public OrganizationListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.organization_content, parent,false);
        OrganizationListHolder holder= new OrganizationListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(OrganizationListHolder holder, int position) {
        final OrganizationList current = data.get(position);
        holder.org_name.setText(current.orgName);
        Picasso.with(context)
                .load(JSON_URL+"/"+current.orgPic)
                .placeholder(R.drawable.org)
                .error(R.drawable.org)
                .into(holder.org_pic);
        holder.org_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String pid = current.orgID;
                bundle.putString("org_id",pid);
                Intent intent = new Intent(context,OrganizationProfile_Activity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class OrganizationListHolder extends RecyclerView.ViewHolder {
        TextView org_name;
        ImageView org_pic;
        CardView org_view;

        public OrganizationListHolder(View itemView) {
            super(itemView);
            org_view = (CardView)itemView.findViewById(R.id.orgView);
            org_name = (TextView)itemView.findViewById(R.id.orgName);
            org_pic = (ImageView)itemView.findViewById(R.id.orgPic);
        }
    }
}
