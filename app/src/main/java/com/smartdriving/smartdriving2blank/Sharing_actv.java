package com.smartdriving.smartdriving2blank;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.sql.Statement;

/**
 * Created by Kendy Colon on 3/24/2016.
 */
public class Sharing_actv extends DialogFragment implements View.OnClickListener{
Button Traffic, Police_actv, Police_Hidding, Road_Work;
//    MainActBlank main = new MainActBlank();

    private String DBstatus;
    ResultSet rs;
    float count=0;
    Statement st;
    String Insert_st;
    GPSTracker gps;
    private GoogleMap map;

    final String DB_NAME="jdbc:mysql://31.220.105.168/smartdri_smDB";
    final String DB_USERNAME="smartdri_admin";
    final String DB_PASSWORD="diciembre26";


@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
        savedInstanceState)
{

    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    map.setMyLocationEnabled(true);
    map.getUiSettings().setMyLocationButtonEnabled(true);
    map.getUiSettings().setZoomControlsEnabled(true);
    map.getUiSettings().setAllGesturesEnabled(true);
    map.getUiSettings().isMapToolbarEnabled();

    View view = inflater.inflate(R.layout.sharing_activity, null);
    Traffic = (Button) view.findViewById(R.id.Traffic);
    Police_actv = (Button) view.findViewById(R.id.Police_actv);
    Police_Hidding = (Button) view.findViewById(R.id.Police_Hidding);
    Road_Work = (Button) view.findViewById(R.id.Road_Work);

    Traffic.setOnClickListener(this);
    Police_actv.setOnClickListener(this);
    Police_Hidding.setOnClickListener(this);
    Road_Work.setOnClickListener(this);
    setCancelable(true);
    getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    return view;


  //  return inflater.inflate(R.layout.sharing_activity, null);
}

    @Override
    public void onClick(View view) {
        count++;
        gps = new GPSTracker(getActivity());
        //Distances = new DistanceCalc(MainActBlank.this);

        LatLng CurrentPosition = null;
        if (gps.canGetLocation() == true) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            //Creating a object type LatLng and giving it two double values
            CurrentPosition = new LatLng(latitude, longitude);

            //Get the distance between two coordinates point to alert user for activies on their sonrouding.
            //  double DistanceBF = Distances.getDistance();
            //Displays the user current location & +otherDat
            Toast.makeText(getActivity(),
                    "Current Location -\nLat: " + latitude
                            + "\nLong: " + longitude
                    ,
                    Toast.LENGTH_LONG).show();
        }
            if (view.getId() == R.id.Traffic) {
                //Close the fragment "window" after an activity is shared
                closefragment();


                Toast.makeText(getActivity(), "Traffic was reported", Toast.LENGTH_LONG).show();
 String mypath = "http://icons.iconarchive.com/icons/icons-land/vista-map-markers/64/Map-Marker-Bubble-Pink-icon.png";


                    Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.entypo_64);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(img);


                    //Creates a marker on CurrentLocation & Zooms in camera to it.
                map.addMarker(new MarkerOptions()
                        .position(CurrentPosition)
                        .title("Traffic")
                            .icon(bitmapDescriptor));

                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(CurrentPosition, 16);
                    map.animateCamera(update);


                Insert_st = "Insert into info_shared(id, loc_lactitud, loc_longitud, loc_name)"+
                        "Values('"+ null + "','" +
                        gps.getLatitude() + "','" +
                        gps.getLongitude() + "','" +
                        "Traffic" + "');";

                  new INSERT_POINT_DB().execute((Void[]) null);


        } else if (view.getId() == R.id.Police_actv) {
                //Close the fragment "window" after an activity is shared
                closefragment();


            //Creates a marker on CurrentLocation & Zooms in camera to it.
                Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.police_32);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(img);

                map.addMarker(new MarkerOptions()
                        .position(CurrentPosition)
                        .title("Police Activity")
                        .icon(bitmapDescriptor));
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(CurrentPosition, 16);
            map.animateCamera(update);


                Insert_st = "Insert into info_shared(id, loc_lactitud, loc_longitud, loc_name)"+
                        "Values('"+ null + "','" +
                        gps.getLatitude() + "','" +
                        gps.getLongitude() + "','" +
                        "Police Visible" + "');";

                new INSERT_POINT_DB().execute((Void[]) null);

            Toast.makeText(getActivity(), "Police Visible was reported", Toast.LENGTH_LONG).show();


                } else if (view.getId() == R.id.Police_Hidding) {
                //Close the fragment "window" after an activity is shared
                closefragment();

                //Creates a marker on CurrentLocation & Zooms in camera to it.
                Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.police_32);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(img);

                map.addMarker(new MarkerOptions()
                        .position(CurrentPosition)
                        .title("Police Hidding")
                        .icon(bitmapDescriptor));


                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(CurrentPosition, 16);
                map.animateCamera(update);

                Insert_st = "Insert into info_shared(id, loc_lactitud, loc_longitud, loc_name)"+
                        "Values('"+ null + "','" +
                        gps.getLatitude() + "','" +
                        gps.getLongitude()+ "','" +
                        "Police Hidding" + "');";

                new INSERT_POINT_DB().execute((Void[]) null);

            Toast.makeText(getActivity(), "Police Hidding was reported", Toast.LENGTH_LONG).show();


                } else if (view.getId() == R.id.Road_Work) {
                //Close the fragment "window" after an activity is shared
                closefragment();

                //Creates a marker on CurrentLocation & Zooms in camera to it.
                Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.construction_32);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(img);

                map.addMarker(new MarkerOptions()
                        .position(CurrentPosition)
                        .title("Road Work")
                        .icon(bitmapDescriptor));


                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(CurrentPosition, 16);
                map.animateCamera(update);

                Insert_st = "Insert into info_shared(id, loc_lactitud, loc_longitud, loc_name)"+
                        "Values('"+ null + "','" +
                        gps.getLatitude() + "','" +
                        gps.getLongitude() + "','" +
                        "Road Work" + "');";

                new INSERT_POINT_DB().execute((Void[]) null);

            Toast.makeText(getActivity(), "Road Work was reported", Toast.LENGTH_LONG).show();
        } else {

        }


    }



    protected class INSERT_POINT_DB extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
             //   Connection conn = DriverManager.getConnection("jdbc:mysql://31.220.105.168/smartdri_smDB", "smartdri_admin", "diciembre26");
                Connection conn = DriverManager.getConnection(DB_NAME, DB_USERNAME, DB_PASSWORD);

                st= conn.createStatement();
                st.executeUpdate(Insert_st);


                if (conn != null) {
                    DBstatus = "Activities Updated";
                } else {
                    DBstatus = "DataBase Connection Failed";
                }

            } catch (Exception ex) {
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

    private void closefragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

}
