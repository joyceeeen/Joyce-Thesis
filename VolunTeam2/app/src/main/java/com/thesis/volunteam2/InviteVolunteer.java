package com.thesis.volunteam2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.thesis.volunteam2.Manager.ReturnHost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class InviteVolunteer extends Fragment {
    ReturnHost rhost = new ReturnHost();
    String host = rhost.returnHost();
    String result = "";
    private static final String TAG_ID = "volunteer_id";
    private static final String TAG_NAME = "volunteer_name";
    private static final String TAG_LOC = "volunteer_address";
    private static final String TAG_DATE = "volunteer_skills";
    private String JSON_URL = "http://" + host + "//volunteam/getVolunteer.php";
    ListView list;
    ArrayList<HashMap<String, String>> volunteerList;
    public InviteVolunteer(){
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.volunteerlist_fragment, container, false );
        list = (ListView) v.findViewById( R.id.volunteer_list );
        volunteerList = new ArrayList<HashMap<String, String>>();
        getData();
        return v;
    }
    protected void showList() {
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject c = jArray.getJSONObject(i);
                String volunteer_id = c.getString(TAG_ID);
                String volunteer_name = c.getString(TAG_NAME);
                String volunteer_address = c.getString(TAG_LOC);
                String volunteer_skills = c.getString(TAG_DATE);

                HashMap<String, String> event = new HashMap<String, String>();
                event.put(TAG_ID, volunteer_id);
                event.put(TAG_NAME,volunteer_name);
                event.put(TAG_LOC, volunteer_address);
                event.put(TAG_DATE, volunteer_skills);
                volunteerList.add(event);
            }
            ListAdapter adapter = new SimpleAdapter(getActivity(), volunteerList, R.layout.volunteerlist,
                    new String[]{TAG_ID, TAG_NAME, TAG_LOC, TAG_DATE},
                    new int[]{R.id.vId, R.id.vName, R.id.vAddress,R.id.vSkills});
            list.setAdapter(adapter );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getData() {
        class GetDataJSON extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                BufferedReader bufferedReader;
                try {
                    Intent intent = getActivity().getIntent();
                    Bundle bundle = intent.getExtras();
                    String vNeed = bundle.getString("event_volunteer");
                    URL url = new URL(JSON_URL+"?vNeed="+vNeed);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json);
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    Log.e("log_tag", "Error converting result" + e.toString());
                }
                return result;
            }
            @Override
            protected void onPostExecute(String result) {
                    showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

}
