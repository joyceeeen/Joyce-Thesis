package com.thesis.volunteam2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.thesis.volunteam2.Adapter.ItemListAdapter;
import com.thesis.volunteam2.Adapter.OrganizationListAdapter;
import com.thesis.volunteam2.Manager.ReturnHost;
import com.thesis.volunteam2.Manager.SessionManager;
import com.thesis.volunteam2.Model.ListItem;
import com.thesis.volunteam2.Model.OrganizationList;

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
 * Created by Jeysown on 7/21/2016.
 */

public class MyOrganization_Fragment extends Fragment {
    private CoordinatorLayout mCoordinator;
    String result = "", userID="";
    ReturnHost rhost = new ReturnHost();
    String host = rhost.returnHost();
    private RecyclerView rvItemList;
    private OrganizationListAdapter adapter;
    List<OrganizationList> data;
    private static final String TAG_ORGNAME="organization_name";
    private static final String TAG_ORGID="organization_id";
    private static final String TAG_ORGPIC="organization_profile";
    SessionManager sessionManager;
    private String JSON_URL = "http://"+host+"/volunteam/getOrganization.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        sessionManager = new SessionManager(getContext());
        //login Status
        sessionManager.checkLogin();
        //getID
        userID = sessionManager.getUSERID();
        rvItemList = (RecyclerView)v.findViewById(R.id.rvItemList);
        rvItemList.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
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
                URL url = new URL(JSON_URL+"?userID="+userID);
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

                    OrganizationList listData = new OrganizationList();
                    listData.orgID = c.getString(TAG_ORGID);
                    listData.orgName = c.getString(TAG_ORGNAME);
                    listData.orgPic = c.getString(TAG_ORGPIC);
                    data.add(listData);
                }
                adapter = new OrganizationListAdapter(getActivity(), data);
                rvItemList.setAdapter(adapter);
            } catch (JSONException e) {
                Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_LONG);
            }
        }
    }

}
