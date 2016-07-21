package com.thesis.volunteam2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thesis.volunteam2.Manager.ReturnHost;
import com.thesis.volunteam2.Manager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText email, password, name;
    String Email, Password, Name, Id;
    ReturnHost rhost = new ReturnHost();
    String host = rhost.returnHost();
    private String JSON_URL = "http://"+host+"/Vol/login_volunteer.php";


    Context ctx=this;
    String NAME=null, ID = null,  PASSWORD=null, EMAIL=null, gender = null, USER = null;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    Typeface customFont,customFont2;
    private ProgressDialog nDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        email = (EditText) findViewById(R.id.main_email);
        password = (EditText) findViewById(R.id.main_password);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        email.addTextChangedListener(new MyTextWatcher(email));
        password.addTextChangedListener(new MyTextWatcher(password));

        customFont2 = Typeface.createFromAsset(getAssets(), "fonts/Ladybug.ttf");
        TextView myTextView2 = (TextView)findViewById(R.id.textview);
        myTextView2.setTextSize(45);
        myTextView2.setTypeface(customFont2);

    }

    public void main_register_volunteer(View v){

        startActivity(new Intent(this,Volunteer_Register.class));
    }
    public void main_register_coordinator(View v){

        startActivity(new Intent(this,Coordinator_Register.class));
    }
    public void main_login(View v){

        submitForm();

    }
    private void submitForm() {

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        BackGround b = new BackGround();
        b.execute(Email, Password);

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
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
                case R.id.register_email:
                    validateEmail();
                    break;
                case R.id.register_password:
                    validatePassword();
                    break;
            }
        }
    }
    class BackGround extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            nDialog = new ProgressDialog(LoginActivity.this);
            // nDialog.setTitle("CREATING ACCOUNT");
            nDialog.setMessage("Logging in...");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            String data="";
            int tmp;

            try {
                URL url = new URL (JSON_URL);
                String urlParams = "email="+email+"&password="+password;

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

        @Override
        protected void onPostExecute(String s) {
            final SessionManager sessionManager = new SessionManager(getApplicationContext());
            nDialog.dismiss();
            String err=null;
            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_data");
                ID = user_data.getString("id");
                NAME = user_data.getString("name");
                PASSWORD = user_data.getString("password");
                EMAIL = user_data.getString("email");
                USER = user_data.getString("user");
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: "+e.getMessage();
            }


            if(s.contains("user_data")) {


                    sessionManager.createLoginSession(EMAIL,PASSWORD,ID);
                    Intent i = new Intent(ctx, MainActivity.class);
                    startActivity(i);
            }

           else if (s.equals("0")){

                s = "Wrong Email or Password";
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }


            else{
                s= "Error in connection";
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

        }
    }
}


