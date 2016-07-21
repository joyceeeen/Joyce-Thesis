package com.thesis.volunteam2;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thesis.volunteam2.Manager.ReturnHost;
import com.thesis.volunteam2.Manager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EditAccount extends Fragment {

    String userID = "", result = "", user_type="", user_gender= "";
    ReturnHost rhost = new ReturnHost();
    String host = rhost.returnHost();
    private String JSON_URL = "http://"+host+"/volunteam/update_volunteer.php";
    private String JSON_URLimg = "http://"+host+"/volunteam/pictures";
    EditText name, password, email;
    RadioButton rb_female;
    RadioButton rb_male;
    RadioGroup radioGroup;
    Button cancel, save;
    String Name, Password, Email, gender, Err, Id, user;
    private ProgressDialog nDialog;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    public EditAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_account, container, false);
        //
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //SESSION
        SessionManager sessionManager = new SessionManager(getContext());
        userID = sessionManager.getUSERID();

        //get
        getData();

        //EDIT TEXT
        name = (EditText)view.findViewById(R.id.edit_name);
        password = (EditText)view.findViewById(R.id.edit_password);
        email = (EditText)view.findViewById(R.id.edit_email);
        rb_female = (RadioButton)view.findViewById(R.id.rb_female);
        rb_male = (RadioButton)view.findViewById(R.id.rb_male);
        if (user_gender.equals("male")){
            rb_male.setChecked(true);
        }else{
            rb_female.setChecked(true);
        }

        cancel = (Button)view.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        save = (Button)view.findViewById(R.id.register_register);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                //alertDialogBuilder.setTitle("Are you sure you want to edit your account?") ;
                alertDialogBuilder
                        .setMessage("Are you sure you want to edit your account?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            // @Override
                            public void onClick(DialogInterface dialog, int id) {
                                submitForm();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            //@Override
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(getContext(),MainActivity.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
        return view;

    }
    public void submitForm(){
            if (!validateName()) {
                return;
            }

            if (!validateEmail()) {
                return;
            }

            if (!validatePassword()) {
                return;
            }

            gender = rb_male.getText().toString();
            gender = rb_female.getText().toString();

            if (radioGroup.getCheckedRadioButtonId()== rb_female.getId()){
                gender = "Female";
            }

            if (radioGroup.getCheckedRadioButtonId()== rb_male.getId()){

                gender = "Male" ;
            }

            BackGroundTask b = new BackGroundTask();
            b.execute(Id, Name, gender, Password, Email, user);

        }


    private boolean validateName() {
        Name = name.getText().toString().trim();
        if (name.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(name);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        Email = email.getText().toString().trim();

        if (email.length() == 0 || !isValidEmail(Email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(email);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        Password = password.getText().toString().trim();
        if (password.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(password);
            return false;
        }
        else if (password.length() <=7) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password_length));
            requestFocus(password);
            return false;}
        else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.register_name:
                    validateName();
                    break;
                case R.id.register_email:
                    validateEmail();
                    break;
                case R.id.register_password:
                    validatePassword();
                    break;

            }
        }
    }

    class BackGroundTask extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            nDialog = new ProgressDialog(getContext());
            nDialog.setMessage("Updating Account...");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String id = params [0];
            String name = params[1];
            String gender = params [2];
            String password = params[3];
            String email = params[4];
            String user = params [5];
            String data="";
            int tmp;
            try {
                URL url = new URL(JSON_URL);
                String urlParams = "id="+id+"&name="+name+"&gender="+gender+"&password="+password+"&email="+email+"&user="+user;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }
        protected void onPostExecute(String s) {

            nDialog.dismiss();

            if (s.equals("1")) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                s = "Successfully updated.";
            }
            else {
                s ="Error in Connection";
            }
            Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();

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
                user_gender = c.getString("gender");
                String user_email = c.getString("email");
                String user_password= c.getString("password");
                user_type = c.getString("user");
//              String profile_pic = c.getString("profile_pic");
                name.setText(user_name);
                email.setText(user_email);
                password.setText(user_password);

//                Picasso.with(getContext())
//                        .load(JSON_URLimg+"/"+profile_pic)
//                        .placeholder(R.drawable.profile)
//                        .error(R.drawable.profile)
//                        .into(vPic);
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
