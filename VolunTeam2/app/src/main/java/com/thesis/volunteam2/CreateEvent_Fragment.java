package com.thesis.volunteam2;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thesis.volunteam2.Manager.ReturnHost;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class CreateEvent_Fragment extends Fragment {
    private final String TAG = this.getClass().getName();
    ReturnHost rhost = new ReturnHost();
    String host = rhost.returnHost();
    EditText name, checkedView,loc,editfromDate,edittoDate, edittoTime,editfromTime, editEventDesc, specify1,specify2,no1,no2,editNum;
    Button btnCreate;
    TextView viewText;
    LinearLayout addSkills,addSkills2;
    Spinner spinner1, spinner2;
    String item, item2;
    ImageView add;
    String data;
    String gNeed= "", vNeed = "";
    boolean showingFirst = true;
    private String JSON_URL = "http://"+host+"/volunteam/addEvent.php";



    public CreateEvent_Fragment newInstance() {
CreateEvent_Fragment fragment = new CreateEvent_Fragment();
        return fragment;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        //
      View v =inflater.inflate(R.layout.event_create_try, container, false);
        name = (EditText)v.findViewById(R.id.editEventName);
        loc = (EditText)v.findViewById(R.id.editEventLoc);
        editfromDate = (EditText)v.findViewById(R.id.editfromDate);
        edittoDate = (EditText)v.findViewById(R.id.edittoDate);
        editfromTime = (EditText)v.findViewById(R.id.editfromTime);
        editEventDesc = (EditText)v.findViewById(R.id.editEventDesc);
        edittoTime = (EditText)v.findViewById(R.id.edittoTime);
        checkedView = (EditText)v.findViewById(R.id.editEventNeeds);
        btnCreate = (Button)v.findViewById(R.id.btn);
        viewText  = (TextView)v.findViewById(R.id.viewText);
        addSkills = (LinearLayout)v.findViewById(R.id.addSkills);
        addSkills2=(LinearLayout)v.findViewById(R.id.addSkills2);
        spinner1 = (Spinner)v.findViewById(R.id.spinner1);
        spinner2 = (Spinner)v.findViewById(R.id.spinner2);
        specify1 = (EditText)v.findViewById(R.id.editSpecify1);
        specify2 = (EditText)v.findViewById(R.id.editSpecify2);
        no1 = (EditText)v.findViewById(R.id.editAmount1);
        no2 = (EditText)v.findViewById(R.id.editAmount2);
        editNum = (EditText)v.findViewById(R.id.editNum);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    item = "";
                }else {
                    item = parent.getItemAtPosition(position).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    item2 = "";
                }else {
                    item2 = parent.getItemAtPosition(position).toString();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        add = (ImageView)v.findViewById(R.id.add1);


        viewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (showingFirst == true){
                   addSkills.setVisibility(View.VISIBLE);
                   add.setVisibility(View.VISIBLE);
                   viewText.setText("Hide");
                   showingFirst = false;
               }else {
                   addSkills.setVisibility(View.GONE);
                   add.setVisibility(View.GONE);
                   viewText.setText("Click here!");
                   showingFirst = true;
               }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showingFirst == true){
                    addSkills2.setVisibility(View.VISIBLE);
                    showingFirst = false;
                }else{
                    addSkills2.setVisibility(View.GONE);
                    showingFirst = true;
                }
            }
        });

//        relief = (CheckBox)v.findViewById(R.id.checkBox5);
//        nurse = (CheckBox)v.findViewById(R.id.checkBox4);
//        doctor = (CheckBox)v.findViewById(R.id.checkBox3);
//        driver = (CheckBox)v.findViewById(R.id.checkBox2);
//        teacher = (CheckBox)v.findViewById(R.id.checkBox7);
//        others = (CheckBox)v.findViewById(R.id.checkBox6);



//        relief.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(relief.isChecked()) {
//                    textNeed = "Relief Bagger";
//                    checkedView.append(textNeed);
//                }
//                else
//                {
//                }
//            }
//        } );
//        nurse.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (nurse.isChecked()){
//                    textNeed =nurse.getText().toString();
//                    checkedView.append(","+textNeed);
//                }
//                else{
//                }
//            }
//        } );
//        doctor.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (doctor.isChecked()){
//                    textNeed =doctor.getText().toString();
//                    checkedView.append(","+textNeed);
//                }
//                else {
//                }
//            }
//        } );
//        driver.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (driver.isChecked()){
//                    textNeed =driver.getText().toString();
//                    checkedView.append(","+textNeed);
//                }
//                else {
//                }
//            }
//        } );
//        teacher.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (teacher.isChecked()){
//                    textNeed =teacher.getText().toString();
//                    checkedView.append(","+textNeed);
//                }
//                else {
//                }
//            }
//        } );
//        others.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (others.isChecked()){
//                    textNeed =others.getText().toString();
//                    checkedView.append(","+textNeed);
//                    Toast.makeText( getContext(),checkedView.getText().toString(),Toast.LENGTH_LONG ).show();
//                }
//                else {
//                }
//            }
//        } );




        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty() && loc.getText().toString().isEmpty() && editfromDate.getText().toString().isEmpty() && checkedView.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"All fields are required",Toast.LENGTH_LONG).show();
                }

                else{
                    String eventName = name.getText().toString();
                    String eventLoc = loc.getText().toString();
                    String eventfromDate = editfromDate.getText().toString();
                    String eventDesc = editEventDesc.getText().toString();
                    String eventtoDate = edittoDate.getText().toString();
                    String eventNeed = checkedView.getText().toString();
                    String eventfromTime = editfromTime.getText().toString();
                    String eventtoTime = edittoTime.getText().toString();
                    String eventGneed = item;
                    String eventGneed2 = item2;
                    String eventSneed = specify1.getText().toString();
                    String eventSneed2 = specify2.getText().toString();
                    String noOfneeds= no1.getText().toString();
                    String noOfneeds2 = no2.getText().toString();
                    String user_id = "1";
//                    String noOfneed = editNum.getText().toString();
                    new doInsertEvent(getContext()).execute(eventName,eventLoc,eventfromDate,eventtoDate,eventNeed,eventfromTime,eventtoTime,eventDesc,eventGneed,eventGneed2,eventSneed,eventSneed2,noOfneeds,noOfneeds2,user_id);
                }
            }


        });

        return v;
    }


    public void onStart(){
        super.onStart();
        editfromDate.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    android.app.FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    dialog.show(ft, "");
                }
            }

        });
        edittoDate.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    android.app.FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    dialog.show(ft, "");
                }
            }

        });
        editfromTime.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    DateDialog.TimeDialog dialog = new DateDialog.TimeDialog( v );
                    android.app.FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    dialog.show( ft, "" );
                }

            }
        } );
        edittoTime.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    DateDialog.TimeDialog dialog = new DateDialog.TimeDialog( v );
                    android.app.FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    dialog.show( ft, "" );
                }

            }
        } );
    }



    private class doInsertEvent extends AsyncTask<String,Void,String> {
        private Context context;

        public doInsertEvent(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String eventName = params[0];
            String eventLoc = params[1];
            String eventfromDate = params[2];
            String eventtoDate = params[3];
            String eventNeed = params[4];
            String eventfromTime = params[5];
            String eventtoTime = params[6];
            String eventDesc = params[7];
            String eventGneed = params[8];
            String eventGneed2 = params[9];
            String eventSneed =  params[10];
            String eventSneed2 =  params[11];
            String noOfneeds = params[12];
            String noOfneeds2 = params[13];
            String user_id = params[14];
//            String noOfneed = params[14];


            BufferedReader bufferedReader;
            String result;
            try {
                data = "?event_name=" + URLEncoder.encode(eventName, "UTF-8");
                data += "&event_location=" + URLEncoder.encode(eventLoc, "UTF-8");
                data += "&event_fromDate=" + URLEncoder.encode(eventfromDate, "UTF-8");
                data += "&event_toDate=" + URLEncoder.encode(eventtoDate, "UTF-8");
                data += "&event_volunteer=" + URLEncoder.encode(eventNeed, "UTF-8");
                data += "&event_fromTime=" + URLEncoder.encode(eventfromTime, "UTF-8");
                data += "&event_toTime=" + URLEncoder.encode(eventtoTime, "UTF-8");
                data += "&event_description=" + URLEncoder.encode(eventDesc, "UTF-8");
                data += "&event_gneed=" + URLEncoder.encode(eventGneed, "UTF-8");
                data += "&event_gneed2=" + URLEncoder.encode(eventGneed2, "UTF-8");
                data += "&event_sneed=" + URLEncoder.encode(eventSneed, "UTF-8");
                data += "&event_sneed2=" + URLEncoder.encode(eventSneed2, "UTF-8");
                data += "&event_noNeeds=" + URLEncoder.encode(noOfneeds, "UTF-8");
                data += "&event_noNeeds2=" + URLEncoder.encode(noOfneeds2, "UTF-8");
                data += "&user_id=" + URLEncoder.encode(user_id, "UTF-8");
//                data += "&event_noNeed=" + URLEncoder.encode(noOfneed, "UTF-8");
                URL url = new URL(JSON_URL + data);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = bufferedReader.readLine();
                return result;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            String jsonStr = result;
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String query_result = jsonObj.getString("query_result");
                        if (query_result.equals("SUCCESS")) {
                        Toast.makeText(context,"Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        String vneed = checkedView.getText().toString();
                        bundle.putString("event_volunteer",vneed);
                        bundle.putString("type","1");
                        Intent intent = new Intent(getActivity(),BlankActivity.class);
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
                    } else if (query_result.equals("FAILURE")) {
                        Toast.makeText(context, "Data could not be inserted.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
