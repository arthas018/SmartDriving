package com.smartdriving.smartdriving2blank;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kendy Colon on 4/4/2016.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
         }
    /*
      *This method save all the data of a user into a protected file in the Device so the user
      * will be able to be logged in each time the App is being used, If user decides to logout
      * all the data will be deleted
     */
    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name", user.user_name);
        spEditor.putString("email", user.user_email);
        spEditor.putString("sex", user.user_sex);
        spEditor.putString("password,", user.user_password);
        spEditor.putString("ranking", user.user_ranking);
        spEditor.putInt("points", user.user_rankingpoints);
        spEditor.commit();
    }

    /*
      *Here it gets all the data stored previously on the device to automatically make the user
      * login into the App
     */
    public User getLoggedInUser() {
        String name = userLocalDatabase.getString("name", "");
        String email = userLocalDatabase.getString("email", "");
        String sex = userLocalDatabase.getString("sex", "");


        String password = userLocalDatabase.getString("password", "");
        String ranking = userLocalDatabase.getString("ranking", "");
        int rankingpoints = userLocalDatabase.getInt("points", 0);

        User storedUser = new User(name, email, password, ranking, sex, rankingpoints);
        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    /*
      *Delete all the data saved in the device when user logsout of the account
     */
    public void clearUserInfo(){
        SharedPreferences.Editor spEditor= userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean("loggedIn", false) ==true)
        {
            return true;
        }else{
            return false;
        }
    }
    /*
      *Method in progress, not completed yet
     */
    public boolean getUserSex(){
         String Usex= userLocalDatabase.getString("sex","");

        // true is female
        if(Usex.equalsIgnoreCase("f"))
        {
            return true;
        }
        else{
            return false;
        }
    }

}

