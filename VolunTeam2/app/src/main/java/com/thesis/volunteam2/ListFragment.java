package com.thesis.volunteam2;


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

import com.thesis.volunteam2.Adapter.ItemListAdapter;
import com.thesis.volunteam2.Manager.ReturnHost;
import com.thesis.volunteam2.Model.ListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;




public class ListFragment extends Fragment {
    ReturnHost rhost = new ReturnHost();
    String host = rhost.returnHost();
    private String JSON_URL = "http://" + host + "//volunteam/home.php";
    String result = "";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "event_name";
    private static final String TAG_LOC = "event_location";
    private static final String TAG_DATE = "event_fromDate";
    private static final String TAG_TIME = "event_fromTime";
    private static final String TAG_IMG = "event_pic";
    private RecyclerView rvItemList;
    private ItemListAdapter adapter;
    List<ListItem> data;
    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        rvItemList = (RecyclerView)v.findViewById(R.id.rvItemList);
        rvItemList.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        rvItemList.setLayoutManager(manager);
        rvItemList.setAdapter(adapter);

        new AsyncFetch().execute();
        return v;
    }

    private class AsyncFetch extends AsyncTask<String,String,String > {

        @Override
        protected String doInBackground(String... params) {

            BufferedReader bufferedReader;

            try {
                URL url = new URL(JSON_URL);
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

                    ListItem listData = new ListItem();

                    listData.id = c.getString(TAG_ID);
                    listData.name = c.getString(TAG_NAME);
                    listData.location = c.getString(TAG_LOC);
                    listData.date = c.getString(TAG_DATE);
                    listData.time = c.getString(TAG_TIME);
                    listData.img = c.getString(TAG_IMG);
                    data.add(listData);
                }
                adapter = new ItemListAdapter(getActivity(), data);
                rvItemList.setAdapter(adapter);
            } catch (JSONException e) {
                Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_LONG);
            }
        }
    }
}


