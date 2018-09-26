package com.smartdriving.smartdriving2blank;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Kendy Colon on 2/04/2016.
 * Project Smart Driving Created by Kendy R. Colon
 * Anyone who's able to see this source code should only be read, no modifications, publications,
 * or any other use without Kendy R. Colon Permission.
 */
public class MainActBlank extends AppCompatActivity {
    final String DB_NAME="jdbc:mysql://31.220.105.168/smartdri_smDB";
    final String DB_USERNAME="smartdri_admin";
    final String DB_PASSWORD="diciembre26";

    Double Distance_From_Closest_DBpoint=5.0;
    float btn_counter;

    //Maps and Gps instances
    private GoogleMap map;

    /*Instace of the class gps, in charged of doing all the work related to the gps services*/
    GPSTracker gps;
    DistanceCalc DistanceCalc2 = new DistanceCalc(MainActBlank.this);

    //Database connection Variables
    private String DBstatus;
    ResultSet resultSet;
    Statement st;

    //Trying to implement ArrayList
    List<Double> ML_latitude = new ArrayList<>();
    List<Double> ML_longitude = new ArrayList<>();
    List<String> ML_name = new ArrayList<>();
    private Bitmap img;
    private BitmapDescriptor bitmapDescriptor;

    /*Device local database class*/
    UserLocalStore userLocalStore;

    //DRiVING Alerts settings
    boolean first_time=true;
    boolean away_fromlast=true;
    int countt=0;

    //7-20-16 4:17 pm location type string implementation
    String near_location_type;

    @Bind(R.id.btnLocation)
    Button  btnprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_act_blank);
        userLocalStore = new UserLocalStore(this);

        /*Map activity settings set up*/
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().isMapToolbarEnabled();

        btnprofile = (Button) findViewById(R.id.btnLocation);


        if(sex()== true){
            btnprofile.setBackgroundResource(R.drawable.ninja_female);
        }else{
            btnprofile.setBackgroundResource(R.drawable.ninja_icon_64);
        }

        /*When the app is open by user, call StartUpTask to update and place all the data from
        * the database
        */
        new StartUPTask().execute((Void[]) null);
        DistanceCalc2.START_DistanceCalc_DATA_Calculations();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_act_blank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onClick_sharing_actv(View v) {

        /* When Clicked start the sharing fragment, whichs has all the objects that can be share
           by the user.*/
            FragmentManager manager = getFragmentManager();
            Sharing_actv sharing_actv = new Sharing_actv();
            sharing_actv.show(manager, "Sharing_Actv");
    }

    //Not supported yet
    public void onClick_search_view(View v){}

 public void onClick_profile(View v) {
     //If a user data is saved on the local database, show the user profile
     if(authenticate() ==true){

         Intent intent1 = new Intent(this, User_info.class);
         startActivity(intent1);

      //If not user data is found, display the Log in activity
     }else {
         Intent intent = new Intent(this, Log_in_activity.class);
         startActivity(intent);
     }
     }

    //Not supported yet
    public boolean isMapToolbarEnabled() {
        return true;
    }


    /*Driving Mode is the function that help the user to drive to it's destination
     * knowing everything that it's ahead of them. For example Traffic, Police, Road work, & more
      * that I will be adding in future versions of the app.*/
    public void onClick_Driving(View v) {
        Uri notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://com.smartdriving.smartdriving2blank/raw/alert");
       final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);

        //new audio alers 7-20-16 4:20pm
        Uri notification1 = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://com.smartdriving.smartdriving2blank/raw/accident");
        final Ringtone ACCIDENT = RingtoneManager.getRingtone(getApplicationContext(), notification1);
        Uri notification2 = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://com.smartdriving.smartdriving2blank/raw/police_department");
        final Ringtone POLICE_DEPARMENT = RingtoneManager.getRingtone(getApplicationContext(), notification2);
        Uri notification3 = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://com.smartdriving.smartdriving2blank/raw/police_trap");
        final Ringtone POLICE_TRAP = RingtoneManager.getRingtone(getApplicationContext(), notification3);
        Uri notification4 = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://com.smartdriving.smartdriving2blank/raw/radar");
        final Ringtone RADAR = RingtoneManager.getRingtone(getApplicationContext(), notification4);
        Uri notification5 = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://com.smartdriving.smartdriving2blank/raw/road_work");
        final Ringtone ROAD_WORK = RingtoneManager.getRingtone(getApplicationContext(), notification5);
        Uri notification6 = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://com.smartdriving.smartdriving2blank/raw/school_area");
        final Ringtone SCHOOL_AREA = RingtoneManager.getRingtone(getApplicationContext(), notification6);
        Uri notification7 = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://com.smartdriving.smartdriving2blank/raw/traffic_ahead");
        final Ringtone TRAFFIC_AHEAD = RingtoneManager.getRingtone(getApplicationContext(), notification7);


        Button button = (Button) v;
        btn_counter++;

        if (btn_counter % 2 == 0) {
            button.setBackgroundResource(R.drawable.transparent_4);
           // ((Button) v).setText("Driving Mode");
        } else {
            //Switch Button Text to Smart On when Pressed On
           // ((Button) v).setText("Smart ON..");
            button.setBackgroundResource(R.drawable.on_64);
           // button.setBackground(R.drawable.button_exit42);

            //Also Displays a Notification to the UI
            Toast.makeText(getApplicationContext(),
                    "Driving's Mode On: ",
                    Toast.LENGTH_LONG).show();

            gps = new GPSTracker(MainActBlank.this);

            /*If the application can get the user current location when the app is opened zoom
            * into the user current position*/
            if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                /* Creating an object type LatLng that will be the current location
                 and giving it two double values LAT/LONG */
                LatLng CurrentPosition = new LatLng(latitude, longitude);

                /*Check before updating the cam zoom to avoid zooming into 0,0 Location
                when gps is not on */

                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(CurrentPosition, 15);
                map.animateCamera(update);
            }

            /** Where the magic happens. Outer Thread that controls the sleep time and the functions
             that make the driving mode possible.
             */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        /**
                         **While Statement that is going to be true only when the user click onces to turn the Driving mode on,
                         * also if the user clicks Again than driving mode is off
                        */
                        while (btn_counter % 2 != 0  ) {

                            /**When the while Statement is true, I'll sleep 10 seconds every time I do my job
                             *  getting my current location and know what points are around me.
                           */
                            Thread.sleep(3250);
                            try{
                                /*
                                ** Second Thread UiThread Allows me to run and Create instance on another objects in my UI
                                * Also I used it to Get the Distance Between closest point from my class DistanceCalc
                                * Than check where I am and what points is the closest.
                                 */
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        try {
                                            DistanceCalc DistanceCalc = new DistanceCalc(MainActBlank.this);
                                          //  Distance_From_Closest_DBpoint = DistanceCalc.getDistance_Between_Closest_Point();
                                            //Testing 5-2-16 on class
                                            //7-20-16 4:14PM
                                            LOCATIONS_INFO near_point = DistanceCalc.getDistance_Between_Closest_Point();

                                            near_location_type  =near_point.location_type;
                                            Distance_From_Closest_DBpoint = near_point.getLocation_distance();






                                            gps = new GPSTracker(MainActBlank.this);

            /*If the application can get the user current location when the app is opened zoom
            * into the user current position*/
                                            if (gps.canGetLocation()) {
                                                double latitude = gps.getLatitude();
                                                double longitude = gps.getLongitude();

                /* Creating an object type LatLng that will be the current location
                 and giving it two double values LAT/LONG */
                                                LatLng CurrentPosition = new LatLng(latitude, longitude);

                /*Check before updating the cam zoom to avoid zooming into 0,0 Location
                when gps is not on */

                                                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(CurrentPosition, 15);
                                                map.animateCamera(update);
                                            }

                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                /*
                                ** Code Where I determine weather to send the user a Notification alerting of how close we are to a Point

                                **If we are in a range of less than a mile and bigger than 1/2 mile get into the if statement a play a notification
                                * Also se value of first_time to false so next time when the threads wakes up we are gonna execute the second statement if
                                * we are less than 1/2 from the closest point, else nothing happens untill we approach a new Point
                                * *//**
                                if ((Distance_From_Closest_DBpoint < 1) && (Distance_From_Closest_DBpoint >.5) && first_time==true ){
                                   r.play();
                                    //Log.w Used for Debuggin Reasons to see data coming from DistanceCalc
                                    Log.w("Getting close!!!","" +  Distance_From_Closest_DBpoint);
                                    first_time =false;
                                    System.err.println("******* On First IF......First: "+first_time);
                                }
                                else if((Distance_From_Closest_DBpoint < .5)){
                                    r.play();
                                    //Log.w Used for Debuggin Reasons to see data coming from DistanceCalc
                                    Log.w("Getting close!!!","" +  Distance_From_Closest_DBpoint);
                                    first_time=true;
                                    System.err.println("******* On Second else If.........First: "+first_time);
                                 }*/

                                if ((Distance_From_Closest_DBpoint < .9) && (Distance_From_Closest_DBpoint > .5) ){
                                    countt++;
                                    System.err.println("Here!!! "+countt  +" "+Distance_From_Closest_DBpoint);
                                   // r.play();
                                    //7-20-16 4:25PM
                                    if (near_location_type.equalsIgnoreCase("ACCIDENT")){
                                        ACCIDENT.play();
                                    }
                                    else if (near_location_type.equalsIgnoreCase("POLICE VISIBLE")){
                                        POLICE_DEPARMENT.play();
                                    }
                                    else if (near_location_type.equalsIgnoreCase("POLICE HIDDING")){
                                        POLICE_TRAP.play();
                                    }
                                    else if (near_location_type.equalsIgnoreCase("RADAR")){
                                        RADAR.play();
                                    }
                                    else if(near_location_type.equalsIgnoreCase("SCHOOL AREA")){
                                        SCHOOL_AREA.play();
                                    }
                                    else if(near_location_type.equalsIgnoreCase("TRAFFIC")){
                                        TRAFFIC_AHEAD.play();
                                    }
                                    else if(near_location_type.equalsIgnoreCase("ROAD WORK")){
                                        ROAD_WORK.play();
                                    }
                                }

                                else {
                                    System.err.println("Last else if close to any point" + Distance_From_Closest_DBpoint);
                                }

                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    //My Report to the User when Driving Mode is Going Offline
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Disabling Driving Mode..",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).start();

        } //End of Driving Mode Functions
    }


    protected class StartUPTask extends AsyncTask<Void, Void, Void > {
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_NAME, DB_USERNAME, DB_PASSWORD);
                st= conn.createStatement();

                /* Get all the current data shared available*/
                String sql = "select * from info_shared";
                resultSet = st.executeQuery(sql);

                try {
                    /*While the result set has next, lest put those values into 3 different list
                     * The first one ML_latitude holds all my Latitude points
                     *The second one ML_longitude holds all my Longitude points
                     * The third one ML_name holds the "Name/type" of the data shared Traffic
                     * /Police/etc
                     */
                    while (resultSet.next()) {
                        ML_latitude.add(resultSet.getDouble("loc_lactitud"));
                        ML_longitude.add(resultSet.getDouble("loc_longitud"));
                        ML_name.add(resultSet.getString("loc_name")) ;
                    }
                    /*When the while loop is done, close the statement connection*/
                    st.close();

                    /*Catch any SQL if the data request fails*/
                } catch (SQLException e) {
                    DBstatus = "DataBase Connection Failed";
                    e.printStackTrace();
                }
                    DBstatus = "Activities Updated";

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            /*When the doInbackgraound method is done, let the user know if the all the data was
            * update or not.*/
            Toast.makeText(getApplicationContext(), DBstatus, Toast.LENGTH_LONG).show();

            gps = new GPSTracker(MainActBlank.this);

            /*If the application can get the user current location when the app is opened zoom
            * into the user current position*/
            if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                /* Creating an object type LatLng that will be the current location
                 and giving it two double values LAT/LONG */
                LatLng CurrentPosition = new LatLng(latitude, longitude);

                /*Check before updating the cam zoom to avoid zooming into 0,0 Location
                when gps is not on */
                if(latitude !=0 & longitude !=0) {
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(CurrentPosition, 11);
                map.animateCamera(update);
                }

                /*
                * On this for loop we going to go through it X times depending on the size of the
                * list ML_latitude.
                * Depending of what's stored in the current ML_name value, the Marker Icon
                * to display is going to changed.
                */
                for (int i = 0; i < ML_latitude.size(); i++) {
                    if (ML_name.get(i).equalsIgnoreCase("Road Work")) {
                        img = BitmapFactory.decodeResource(getResources(), R.drawable.construction_32);
                        BitmapDescriptor bitmapDescriptor2 = BitmapDescriptorFactory.fromBitmap(img);
                        map.addMarker(new MarkerOptions().position(new LatLng(ML_latitude.get(i), ML_longitude.get(i))).title(ML_name.get(i)).icon(bitmapDescriptor2));
                    }
                    else if (ML_name.get(i).equalsIgnoreCase("Traffic"))
                    {
                        img = BitmapFactory.decodeResource(getResources(), R.drawable.entypo_64);
                        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(img);
                        map.addMarker(new MarkerOptions().position(new LatLng(ML_latitude.get(i), ML_longitude.get(i))).title(ML_name.get(i)).icon(bitmapDescriptor));
                    }
                    else
                    {
                        img = BitmapFactory.decodeResource(getResources(), R.drawable.police_32);
                        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(img);
                        map.addMarker(new MarkerOptions().position(new LatLng(ML_latitude.get(i), ML_longitude.get(i))).title(ML_name.get(i)).icon(bitmapDescriptor));
                    }
                }
                /*If the application cannot access the GPS services show an alert to the user
                */
                }else {
                gps.showSettingsAlert();
            }
                super.onPostExecute(result);
            }
        }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    /*This method allows who ever calls it to know if there's any user data stored in the
    * device/phone database */
    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }

    private boolean sex(){
        return userLocalStore.getUserSex();
    }


    }


