package com.smartdriving.smartdriving2blank;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kendy Colon on 4/4/2016.
 */
public class User_info extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.user_name)
    TextView _user_name;

    @Bind(R.id.user_email)
    TextView _user_email;

    @Bind(R.id.change_password)
    TextView _change_password_link;

    @Bind(R.id.ranking)
    TextView _user_ranking;

    @Bind(R.id.community_points)
    TextView _community_points;

    @Bind(R.id.user_sex)
    TextView _user_sex2;

    String user_sexo;


    Switch user_sex_switch= null;

    UserLocalStore userLocalStore;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        ButterKnife.bind(this);

        userLocalStore = new UserLocalStore(this);




        _change_password_link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity

            }
        });
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }



    public void displayUserDetails(){



        /*
        * Get's user from the device database if it exist and sets it up*/
        User user = userLocalStore.getLoggedInUser();
        _user_name.setText("User Name: "+user.user_name);
        _user_email.setText("Email: " + user.user_email);
        _user_ranking.setText(user.user_ranking);
       _community_points.setText("Ranking Points: " + Integer.toString(user.user_rankingpoints));

        user_sexo = user.user_sex;

        if( user_sexo.equalsIgnoreCase("f")){
            _user_sex2.setText("Sex: Female");

        }else{
            _user_sex2.setText("Sex: Male");
        }



       String sex=user.user_sex;

        if(user.user_rankingpoints >80) {
            _user_ranking.setText("Master Ninja");

        }else{

            _user_ranking.setText("Student Ninja");

        }
    }

    /*
    * Check the local database for any user that was logged in before and did not log out
    */
    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }


    /*
    * First thing that it's done when this class is envoke
    */
    @Override
    protected void onStart() {
        super.onStart();
        /*
        When the activity its call first I have to check my device databse to see if
        I have any user that Logged In before, If is true, I display the details of that account,
        Else I'll display the sign in activity to the user
         * */
        if (authenticate() == true) {
            displayUserDetails();
        }
    }

/*
* When my user click on log out on their profile, I'll delete all the data saved in the device
* database, and set UserLoggedIn to false, so when I'm invoke I will display my login activity
*/
    public void onClick_logout(View view) {
        userLocalStore.clearUserInfo();
        userLocalStore.setUserLoggedIn(false);

        /*
        * After a user clicks on logout, this activity will die and the app goes back to the main activity
        */
        finish();
    }
    }




