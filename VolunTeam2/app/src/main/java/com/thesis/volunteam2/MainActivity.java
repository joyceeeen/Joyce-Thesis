package com.thesis.volunteam2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thesis.volunteam2.Manager.ReturnHost;
import com.thesis.volunteam2.Manager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    TextView name, type;
    View headerView;
    ImageView vPic;
    NavigationView navigationView;
    ReturnHost rhost = new ReturnHost();
    String host = rhost.returnHost();
    String result = "",user_type="",userID = "";
    SessionManager sessionManager;

    private String JSON_URL = "http://"+host+"/volunteam/getUserInfo.php";
    private String JSON_URLimg = "http://"+host+"/volunteam/pictures";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        sessionManager = new SessionManager(getApplicationContext());
        //login Status
        sessionManager.checkLogin();
        //getID
        userID = sessionManager.getUSERID();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getData();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        name = (TextView)headerView.findViewById(R.id.volunteer_name);
        type = (TextView)headerView.findViewById(R.id.type);
        vPic = (ImageView)headerView.findViewById(R.id.vPic);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(user_type.equals("coordinator")){
            navigationView.inflateMenu(R.menu.activity_main_drawer);
            navigationView.setNavigationItemSelectedListener(this);
        }else{
            navigationView.inflateMenu(R.menu.volunteer_nav_drawer);
            navigationView.setNavigationItemSelectedListener(this);

        }

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add( R.id.content_main, new TabFragment()).addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        ;
        //noinspection SimplifiableIfStatement
        if (id == R.id.create) {
            CreateEvent_Fragment createFragment = new CreateEvent_Fragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace( R.id.content_main, createFragment );
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Create Event");
        }
        else if (id == R.id.edit) {
            return true;
        }
        if (id == R.id.logout) {
            sessionManager.logOutUser();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            TabFragment fragment = new TabFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content_main,fragment);
            transaction.commit();
            getSupportActionBar().setTitle("Oppurtunities");

        } else if (id == R.id.nav_activity) {
            Toast.makeText(this, "Activity", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_application) {
            Toast.makeText(this, "Application", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_history) {
            Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_group) {
            MyOrganization_Fragment fragment = new MyOrganization_Fragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content_main,fragment);
            transaction.commit();
            getSupportActionBar().setTitle("My Organization/s");
        } else if (id == R.id.nav_edit) {
            EditAccount fragment = new EditAccount();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content_main,fragment);
            transaction.commit();
            getSupportActionBar().setTitle("Edit Account");
        } else if (id == R.id.nav_logout) {
            sessionManager.logOutUser();
        }else if (id == R.id.nav_help) {
            Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_about) {
            Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void getData(){
        class GetDataJSON extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }
            @Override
            protected String doInBackground(String... params) {


                BufferedReader bufferedReader;

                try {
                    URL url = new URL(JSON_URL+"?userID="+userID);
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
    protected void showData(){
        try{
            JSONArray jArray = new JSONArray(result);
            for (int i=0;i<jArray.length();i++){
                JSONObject c = jArray.getJSONObject(i);
                String user_name = c.getString("name");
//                String user_gender = c.getString("gender");
//                String user_email = c.getString("email");
//                String user_password= c.getString("password");
                user_type = c.getString("user");
                String profile_pic = c.getString("profile_pic");



                name.setText(user_name);
                type.setText("("+user_type+")");
                Picasso.with(getApplicationContext())
                        .load(JSON_URLimg+"/"+profile_pic)
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile)
                        .into(vPic);
//                date.setText(event_date);
//                time.setText(event_fromTime);
//                need.setText(event_volunteer);
//                desc.setText(event_description);
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}

