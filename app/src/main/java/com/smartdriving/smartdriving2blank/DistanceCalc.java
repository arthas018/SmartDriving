package com.smartdriving.smartdriving2blank;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Kendy Colon on 2/27/2016.
 */

public class DistanceCalc extends GPSTracker {
    ResultSet rs ;
    Statement st;
    Double Distance;

    //Meters in a Mile
    final double MetersConv = 1609.34;

    double CurrentLat;
    double CurrentLong;
    double INmeters;
    double Conversion;

    DistanceCalcData OTRA = new DistanceCalcData();

    static List<Double> ML_latitude = new ArrayList<>();
    static  List<Double> ML_longitude = new ArrayList<>();
    static List<String> ML_name = new ArrayList<>();
    List<Double> CurrentDistance_From_DB_Point = new ArrayList<>();

    //Implementation of the treeset to try to calculated distance in between and what kind of point it is.
    TreeSet<LOCATIONS_INFO> points_tree = new TreeSet<>(new distance_from_mines());


    //Added ib 6-21-16 implementing audio notifications
    List<LOCATIONS_INFO> CALCULATED_LOCATIONS = new ArrayList<>();
    LOCATIONS_INFO calculated_location;


    float[] results = new float[1];



    public DistanceCalc(Context context) {
        super(context);
    }


    public void setDistance(Double Distance) {
        this.Distance = Distance;
    }



    public void START_DistanceCalc_DATA_Calculations()
    {
        new DistanceCalcData().execute((Void[]) null);
    }


    /**
    public Double getDistance_Between_Closest_Point()
    {
       Double DISTANCE_FROM_CLOSET_DBPOINT = OTRA.onPostExecute();
        return DISTANCE_FROM_CLOSET_DBPOINT;
    }
*/
    public LOCATIONS_INFO getDistance_Between_Closest_Point()
    {
        LOCATIONS_INFO OUTGOING_POINT = OTRA.onPostExecute();
        return OUTGOING_POINT;
    }

  protected class DistanceCalcData extends AsyncTask<Void, Void, LOCATIONS_INFO > {
        @Override
        protected LOCATIONS_INFO doInBackground(Void... arg0) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://31.220.105.168/smartdri_smDB", "smartdri_admin", "diciembre26");

                st= conn.createStatement();
                String sql = "select * from info_shared";
                rs = st.executeQuery(sql);

                try {
                    while (rs.next()) {
                        ML_latitude.add(rs.getDouble("loc_lactitud"));
                        ML_longitude.add(rs.getDouble("loc_longitud"));
                        ML_name.add(rs.getString("loc_name")) ;
                    }st.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (Exception ex) {
                //Show Exception if connection fails
                ex.printStackTrace();
            }
            return null;
        }


       protected LOCATIONS_INFO onPostExecute() {
           if (canGetLocation()) {
               //Gets My Actual Location Coornidates
               CurrentLat = location.getLatitude();
               CurrentLong = location.getLongitude();

               //NOTHING IS COMING FROM THE MAIN ACT .. FIND OUT WHY DATA IS NOT COMMING OR STORING IN CURRENT DISTANC ARRAYLIST
                for (int i = 0; i < ML_latitude.size(); i++) {
                    Location.distanceBetween(CurrentLat, CurrentLong, ML_latitude.get(i), ML_longitude.get(i), results);

                    INmeters = results[0];
                    Conversion = (INmeters / MetersConv);

                    //Adds Each Distance from all the points on the DB to the List Below
                    CurrentDistance_From_DB_Point.add(Conversion);

                    //adding the object to my treeset 7-20-16 12:56AM
                    LOCATIONS_INFO point_calculated_distance = new LOCATIONS_INFO(ML_name.get(i), Conversion);
                    points_tree.add(point_calculated_distance);

                    point_calculated_distance=null;

                    calculated_location = new LOCATIONS_INFO (ML_name.get(i), Conversion);
                    CALCULATED_LOCATIONS.add(calculated_location);
                }
           }
           //Sort method sorts the specified array of doubles into ascending numerical order.
           Collections.sort(CurrentDistance_From_DB_Point);
           //Collections.sort(CALCULATED_LOCATIONS);


           System.out.println("#########---------VALUE IS COMING IN HOT-----######");
           System.err.println("#########---------VALUE IS COMING IN HOT-----######");

           System.out.println(">>>>>>>>>>>>>>> Tree: "+points_tree.first());
           System.err.println(">>>>>>>>>>>>>>>List:  "+CurrentDistance_From_DB_Point.get(0));

           /**
            * 7-20-16 1:26am
            * Create a class that I can pass a LOCATIONS_INFO object and return a String value.
            * This class task is to determine what kind of points is on the LOCATIONS_INFO object
            * To do that, get the object in the first place, brake it into an array of strings, and get the first two parts which is the point type.
            * Option #1 is to get the object and ask LOCATIONS_INFO for the type of that object.? LOCATIONS_INFO.getType() ? base on that return a string
            * when thats done, on the running treat  read the string get use the x Resource audio and play it. It could be in a if statement? :) Nice!! Im going to sleep now! 1:30am
            */


           /**
            *  5:00 pm 7/18/206
            *  I should make it somehow that I would created an object right after I calculate my distances with distance from x-point and With my current location and the result created
            *  an object with x name and add it to the TreeSet.
           */





           System.err.println("**************************" + CALCULATED_LOCATIONS.get(2));



           Distance = CurrentDistance_From_DB_Point.get(0).doubleValue();

           return  points_tree.first();

        }
      }



    }




   


  