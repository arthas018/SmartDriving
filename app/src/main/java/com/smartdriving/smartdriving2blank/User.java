package com.smartdriving.smartdriving2blank;

/**
 * Created by Kendy Colon on 4/4/2016.
 */
public class User {
    String user_name, user_email, user_password, user_ranking, user_sex;
    int user_rankingpoints;


    public User(String name, String email, String password, String ranking, String user_sex, int rankingpoints){
        this.user_name=name;
        this.user_email=email;
        this.user_password=password;
        this.user_ranking=ranking;
        this.user_rankingpoints=rankingpoints;
        this.user_sex=user_sex;
    }

    public User(String email, String password){
        this.user_email=email;
        this.user_password=password;
        this.user_name="";
        this.user_ranking="Ninjita";
        this.user_rankingpoints=0;
        this.user_sex="M";
    }
}
