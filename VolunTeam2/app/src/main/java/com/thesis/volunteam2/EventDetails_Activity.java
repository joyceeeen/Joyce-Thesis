package com.thesis.volunteam2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.thesis.volunteam2.Manager.ReturnHost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EventDetails_Activity extends AppCompatActivity {
    private CoordinatorLayout mCoordinator;
    String result = "";
    TextView name,location,date,time,desc,need;
    ReturnHost rhost = new ReturnHost();
    String host = rhost.returnHost();
    private static final String TAG_ID="id";
    private static final String TAG_NAME="event_name";
    private static final String TAG_LOC="event_location";
    private static final String TAG_DATE="event_fromDate";
    private static final String TAG_TIME="event_fromTime";
    private static final String TAG_DESC="event_description";
    private static final String TAG_NEED="event_volunteer";
    private static final String TAG_ORGNAME="organization_name";
    NestedScrollView nestedScrollView;
    private String JSON_URL = "http://"+host+"/volunteam/getEvents.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details_holder);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Event Details");

        nestedScrollView = (NestedScrollView)findViewById(R.id.nested_content_main);
        View content = getLayoutInflater().inflate(R.layout.mod_event_details,null);
        nestedScrollView.addView(content);



        name= (TextView)findViewById(R.id.currentAct );
        location=(TextView)findViewById(R.id.location);
        date=(TextView)findViewById(R.id.date);
        time=(TextView)findViewById(R.id.time);
        desc=(TextView)findViewById(R.id.description);
        need=(TextView)findViewById( R.id.need );


        getData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void showData(){
        try{
            JSONArray jArray = new JSONArray(result);
            for (int i=0;i<jArray.length();i++){

                JSONObject c = jArray.getJSONObject(i);
                String event_name = c.getString(TAG_NAME);
                String event_location = c.getString(TAG_LOC);
                String event_date = c.getString(TAG_DATE);
                String event_fromTime = c.getString(TAG_TIME);
                String event_description = c.getString(TAG_DESC);
                String event_volunteer = c.getString(TAG_NEED);

                name.setText(event_name);
                location.setText(event_location);
                date.setText(event_date);
                time.setText(event_fromTime);
                need.setText(event_volunteer);
                desc.setText(event_description);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void getData(){
        class GetDataJSON extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }
            @Override
            protected String doInBackground(String... params) {
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
                String eventID = bundle.getString("id");
                BufferedReader bufferedReader;

                try {
                    URL url = new URL(JSON_URL+"?eventID="+eventID);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while((json = bufferedReader.readLine()) != null){
                        sb.append(json+"\n");
                    }
                    result = sb.toString();
                }
                catch(Exception e){
                    Log.e("log_tag", "Error converting result"+e.toString());
                }
                return result;
            }
            @Override
            protected void onPostExecute(String result)
            {
                showData();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
}
