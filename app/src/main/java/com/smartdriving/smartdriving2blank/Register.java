package com.smartdriving.smartdriving2blank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kendy Colon on 4/3/2016.
 */


public class Register extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;
    Statement st;
    String Insert_st;
    GPSTracker gps;
    private GoogleMap map;
    Boolean confirmed=false;

    final String DB_NAME="jdbc:mysql://31.220.105.168/smartdri_smDB";
    final String DB_USERNAME="smartdri_admin";
    final String DB_PASSWORD="diciembre26";
    private static final String TAG = "SignupActivity";
    UserLocalStore userLocalStore;

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_password2) EditText _passwordText2;
    @Bind(R.id.btn_signup)  Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;
    @Bind(R.id.singup_sex)   Switch _sex;

    private String user_name;
    private String user_email;
    private String user_password;
    private String user_password2;
    private String user_sex;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sing_up_activity);
        ButterKnife.bind(this);

        userLocalStore= new UserLocalStore(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                signup();
                Toast.makeText(getApplicationContext(), Insert_st, Toast.LENGTH_LONG).show();


            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Register.this);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();



        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                         user_name = _nameText.getText().toString();
                         user_email = _emailText.getText().toString();
                         user_password = _passwordText.getText().toString();
                         user_password2 = _passwordText2.getText().toString();

                        if(_sex.isChecked()){
                            user_sex = "F";
                        } else{
                            user_sex="M";
                        }



                        if(user_password.equalsIgnoreCase(user_password2)) {
                            Insert_st = "Insert INTO regi$tred_u$er$ " +
                                    "VALUES('null','" + user_name + "','" + user_email + "','" + user_password + "','" + user_sex + "','" + null + "');";

                            //"VALUES('null','" +user_name + "','" +user_email + "','" +user_password+"',null','null'"');";

                            new INSERT_USER_DATA_DB().execute((Void[]) null);


                            if (confirmed == true) {
                                User userRegistred = new User(user_name, user_email, user_password, user_sex, null, 0);
                                userLocalStore.storeUserData(userRegistred);
                                userLocalStore.setUserLoggedIn(true);

                                Intent intent = new Intent(getApplicationContext(), User_info.class);
                                startActivityForResult(intent, REQUEST_SIGNUP);
                            }
                        }



                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    protected class INSERT_USER_DATA_DB extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                //   Connection conn = DriverManager.getConnection("jdbc:mysql://31.220.105.168/smartdri_smDB", "smartdri_admin", "diciembre26");
                Connection conn = DriverManager.getConnection(DB_NAME, DB_USERNAME, DB_PASSWORD);


                System.out.println("LOOOOOOOOOOOK HEREEEEEEE" + Insert_st);
                st= conn.createStatement();
                st.executeUpdate(Insert_st);
                confirmed =true;

                if (conn != null) {

                    System.out.println("Activities Updated");
                } else {
                   System.out.println("DataBase Connection Failed");
                }

            } catch (Exception ex) {
                confirmed=false;
                ex.printStackTrace();
                //Show Exception if connection fails

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Toast.makeText(getActivity(), "Record Updated Successfully", Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
        }
    }
}