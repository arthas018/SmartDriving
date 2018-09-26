package com.smartdriving.smartdriving2blank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kendy Colon on 4/3/2016.
 */
public class Log_in_activity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    final String DB_NAME = "jdbc:mysql://31.220.105.168/smartdri_smDB";
    final String DB_USERNAME = "smartdri_admin";
    final String DB_PASSWORD = "diciembre26";
    Statement st;
    String Insert_st;
    private String email;
    private String password;
    ResultSet rs;
    boolean DBvalidation=false;

    protected String DB_user_email;
    protected String DB_user_name;
    protected String DB_user_password;
    protected String DB_user_sex;
    protected int DB_ranking_points;

    UserLocalStore userLocalStore;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        userLocalStore = new UserLocalStore(this);
        super.onCreate(savedInstanceState);
            setContentView(R.layout.sign_in_activity);
            ButterKnife.bind(this);

            _loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });
            _signupLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), Register.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                }
            });

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(Log_in_activity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        // TODO: Implement your own authentication logic here.
        //if (DBvalidation == true) {
            final Intent intent = new Intent(this, User_info.class);

        Handler refresh = new Handler(Looper.getMainLooper());
        refresh.post(new Runnable() {
            @Override
            public void run() {
                // call my user authentication to check data pass/email
                new USER_AUTHENTICATION().execute((Void[]) null);
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                if(DB_user_name !=null && DBvalidation==true) {

                                    System.out.println(rs);
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    onLoginSuccess();
                                    // onLoginFailed();
                                    progressDialog.dismiss();

                                    //(String name, String email, String password, String ranking, int rankingpoints)
                                    //public User(String name, String email, String password, String ranking, String user_sex, int rankingpoints)

                                    User user= new User(DB_user_name,DB_user_email,DB_user_password, null,DB_user_sex, DB_ranking_points);
                                    userLocalStore.storeUserData(user);
                                    userLocalStore.setUserLoggedIn(true);

                                    startActivity(intent);
                                }
                                else {
                                    progressDialog.dismiss();
                                    onLoginFailed();
                                }
                                System.out.println("IIIIIIIIIII MADEEEEEEEEEEEEEEEEEEEEEE 22323242T");
                            }
                        }, 3000);

            }});
            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                //this.finish();
            }
        }
    }


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        // onActivityResult(0,0, null);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed Check Email/pass", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean DBvalidation(){
        return DBvalidation;
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

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

    protected class USER_AUTHENTICATION extends AsyncTask<Void, Void, User> {
        @Override
        protected User doInBackground(Void... params) {
            User Incominguser= null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_NAME, DB_USERNAME, DB_PASSWORD);
                st = conn.createStatement();

                System.err.println("*********"+email);
                System.err.println("*********"+password);

               String sql = ("SELECT * FROM regi$tred_u$er$ where u$er_email='" + email + "' AND u$er_password='" + password + "'");

                rs = st.executeQuery(sql);
                try {

                    if (!rs.next()) {
                        System.err.println("xxxxxxxxxxxxEMPTTTTTTTTTTTYYYYYYY");
                        DBvalidation = false;


                    } else {
                        System.out.println("Was Validated");
                        DBvalidation = true;


                        DB_user_name = (rs.getString("u$er_name"));
                        DB_user_password = (rs.getString("u$er_password"));
                        DB_user_email = (rs.getString("u$er_email"));
                        DB_user_sex = (rs.getString("u$er_sex"));
                        DB_ranking_points = (rs.getInt("u$er_ranking_points"));
                        // DB_ranking_point= (Integer.toString(DB_ranking_points));

                        Incominguser = new User(DB_user_name, DB_user_email, "", "", DB_user_sex, DB_ranking_points);
                        System.out.println("************************************" + DB_user_name + DB_user_email + DB_user_sex + DB_ranking_points);
                    }
                    rs.close();
                    rs = null;


                } catch (SQLException e) {
                    e.printStackTrace();
                }


            } catch (Exception ex) {
                ex.printStackTrace();
                //Show Exception if connection fails

            }
            return Incominguser;
        }

        @Override
        protected void onPostExecute(User Incominguser) {

            if(DB_user_name !=null && DBvalidation()==true) {
                User_info user_info = new User_info();

                System.out.println(DB_user_name);
                System.out.println(DB_user_email);
                System.out.println(DB_user_password);
                System.out.println(DB_user_sex);
                System.out.println(Integer.toString(DB_ranking_points));


                System.out.println(DB_user_name);
                System.out.println(DB_user_email);
                System.out.println(DB_user_password);
            }

            super.onPostExecute(Incominguser);
        }
    }

    private void logUserIn(User Incominguser){
        userLocalStore.storeUserData(Incominguser);
        userLocalStore.setUserLoggedIn(true);
    }

    }


