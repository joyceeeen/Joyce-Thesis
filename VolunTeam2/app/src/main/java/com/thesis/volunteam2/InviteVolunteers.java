package com.thesis.volunteam2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thesis.volunteam2.Adapter.VolunteerListAdapter;
import com.thesis.volunteam2.Manager.ReturnHost;
import com.thesis.volunteam2.Model.VolunteerList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeysown on 7/14/2016.
 */

public class InviteVolunteers extends Fragment {
    ReturnHost rhost = new ReturnHost();
    String host = rhost.returnHost();
    private String JSON_URL = "http://" + host + "//volunteam/getVolunteer.php";
    String result = "";
    private static final String TAG_ID = "volunteer_id";
    private static final String TAG_NAME = "volunteer_name";
    private static final String TAG_ADDRS = "volunteer_address";
    private static final String TAG_SKILLS = "volunteer_skills";
    private static final String TAG_IMG = "volunteer_profilepic";
    private RecyclerView rvItemList;
    private VolunteerListAdapter adapter;
    List<VolunteerList> data;
    public InviteVolunteers() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.mod_invite_volunteers, container, false);

        rvItemList = (RecyclerView)v.findViewById(R.id.rvItemList);
        rvItemList.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        rvItemList.setLayoutManager(manager);
        rvItemList.setAdapter(adapter);

        new VolunteerFetch().execute();
        return v;
    }

    private class VolunteerFetch extends AsyncTask<String,String,String > {

        @Override
        protected String doInBackground(String... params) {

            BufferedReader bufferedReader;

            try {
                Intent intent = getActivity().getIntent();
                Bundle bundle = intent.getExtras();
                String vNeed = bundle.getString("event_volunteer");
                URL url = new URL(JSON_URL+"?vNeed="+vNeed);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                result = sb.toString();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result" + e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            data = new ArrayList<>();
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject c = jArray.getJSONObject(i);

                    VolunteerList listData = new VolunteerList();

                    listData.vName = c.getString(TAG_NAME);
                    listData.vAddress= c.getString(TAG_ADDRS);
                    listData.vSkills = c.getString(TAG_SKILLS);
                    listData.vPic = c.getString(TAG_IMG);
                    data.add(listData);
                }
                adapter = new VolunteerListAdapter(getActivity(), data);
                rvItemList.setAdapter(adapter);
            } catch (JSONException e) {
                Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_LONG);
            }
        }
    }
}
