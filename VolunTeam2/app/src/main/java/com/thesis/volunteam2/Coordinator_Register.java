package com.thesis.volunteam2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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


public class Coordinator_Register extends AppCompatActivity {

    ReturnHost rhost = new ReturnHost();
    String host = rhost.returnHost();
    private String JSON_URL = "http://"+host+"/Vol/register_volunteer.php";

    RadioGroup radioGroup;
    RadioButton rb_female;
    RadioButton rb_male;
    String gender;
    String user = "Coordinator";
    EditText name, password, email;
    String Name, Password, Email, Id, User;
    TextView gender_error;
    Context ctx=this;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    private ProgressDialog nDialog;
    String NAME, EMAIL, PASSWORD, ID, USER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_volunteer);

        name = (EditText) findViewById(R.id.register_name);
        password = (EditText) findViewById(R.id.register_password);
        email = (EditText) findViewById(R.id.register_email);
        gender_error = (TextView) findViewById(R.id.gender_validate);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rb_female = (RadioButton)findViewById(R.id.rb_female);
        rb_male = (RadioButton)findViewById(R.id.rb_male);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        name.addTextChangedListener(new MyTextWatcher(name));
        email.addTextChangedListener(new MyTextWatcher(email));
        password.addTextChangedListener(new MyTextWatcher(password));

    }
    public  void register_cancel(View v){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);

    }

    public void register(View v){

        submitForm();
    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        if(!validateGender()){
            return;

        }

        BackGround b = new BackGround();
        b.execute(Name, gender, Password, Email, user);

    }


    private boolean validateGender(){
        gender = rb_male.getText().toString();
        gender = rb_female.getText().toString();


        if (radioGroup.getCheckedRadioButtonId()== rb_female.getId()){
            gender = "Female";
        }

        else if (radioGroup.getCheckedRadioButtonId()== rb_male.getId()){

            gender = "Male" ;
        }
        else {
            gender_error.setTextColor(Color.RED);
            gender_error.setText("Please choose your gender");
            return false;

        }

        return true;
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
            return false;}else {
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
                case R.id.register_name:
                    validateName();
                    break;
                case R.id.register_email:
                    validateEmail();
                    break;
                case R.id.register_password:
                    validatePassword();
                    break;
                case R.id.rb_female:
                    validateGender();
                    break;
                case R.id.rb_male:
                    validateGender();
                    break;

            }
        }
    }
    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            nDialog = new ProgressDialog(Coordinator_Register.this);
            // nDialog.setTitle("CREATING ACCOUNT");
            nDialog.setMessage("Creating Account...");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {


            String name = params[0];
            String gender = params [1];
            String password = params[2];
            String email = params[3];
            String user = params [4];
            // String id = params [5];
            String data="";
            int tmp;
            //String response = "";
            try {
                URL url = new URL(JSON_URL);
                String urlParams = "name="+name+"&gender="+gender+"&password="+password+"&email="+email+"&user="+user;

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
            }

            catch (MalformedURLException e) {
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
                gender = user_data.getString("gender");
                PASSWORD = user_data.getString("password");
                EMAIL = user_data.getString("email");
                USER = user_data.getString("user");
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: "+e.getMessage();
            }

            if (s.equals("0")) {

                s = "Email Already Exist.";

            }
            else{
                sessionManager.createLoginSession(EMAIL,PASSWORD,ID);
                Intent intent = new Intent(ctx,MainActivity.class);
                startActivity(intent);
                s= "Welcome";

            }
        }
    }
}








