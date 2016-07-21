package com.thesis.volunteam2.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thesis.volunteam2.Model.VolunteerList;
import com.thesis.volunteam2.R;
import com.thesis.volunteam2.Manager.ReturnHost;

import java.util.List;

/**
 * Created by Jeysown on 7/14/2016.
 */

public class VolunteerListAdapter extends RecyclerView.Adapter<VolunteerListAdapter.VolunteerListViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<VolunteerList> data;
    ReturnHost rhost = new ReturnHost();
    String host = rhost.returnHost();

    private String JSON_URL = "http://" + host + "//volunteam/pictures";

    public VolunteerListAdapter(Context context,List<VolunteerList> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public VolunteerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.mod_invite_view, parent,false);
        VolunteerListViewHolder holder=new VolunteerListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VolunteerListViewHolder holder, int position) {
        VolunteerList current = data.get(position);
        holder.txt_v_name.setText(current.vName);
        holder.txt_v_addrees.setText(current.vAddress);
        holder.txt_v_skills.setText(current.vSkills);
        Picasso.with(context)
                .load(JSON_URL+"/"+current.vPic)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(holder.txt_v_img);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VolunteerListViewHolder extends RecyclerView.ViewHolder {
        ImageView txt_v_img;
        TextView txt_v_name,txt_v_addrees,txt_v_skills;

        public VolunteerListViewHolder(View itemView) {
            super(itemView);

            txt_v_img= (ImageView) itemView.findViewById(R.id.vPic);
            txt_v_name = (TextView) itemView.findViewById(R.id.vName);
            txt_v_addrees = (TextView) itemView.findViewById(R.id.vAddress);
            txt_v_skills= (TextView) itemView.findViewById(R.id.vSkills);
        }
    }
}
