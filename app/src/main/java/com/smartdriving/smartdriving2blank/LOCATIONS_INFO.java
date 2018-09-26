package com.smartdriving.smartdriving2blank;

/**
 * Created by Ken on 6/21/2016.
 */
public class LOCATIONS_INFO  implements Comparable<LOCATIONS_INFO>{

    double CalculatedDistance_from_me;


    String location_type;

    public LOCATIONS_INFO(String type, double distance_from_me){

        location_type = type;
        CalculatedDistance_from_me =distance_from_me;
    }


    public double getLocation_distance() {
        return CalculatedDistance_from_me;
    }

    public void setLocation_latitude(double location_latitude) {
        this.CalculatedDistance_from_me = location_latitude;
    }



    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    @Override
    public String toString()
    {
        return location_type+ " "+CalculatedDistance_from_me;

    }

    @Override
    public int compareTo(LOCATIONS_INFO other_location) {
        final int BEFORE=-1;
        final int EQUAL=0;
        final int AFTER=1;

        if( this == other_location){
            return EQUAL;
        }
        if (this.CalculatedDistance_from_me > other_location.CalculatedDistance_from_me){
            return AFTER;
        }
        if (this.CalculatedDistance_from_me < other_location.CalculatedDistance_from_me){
            return BEFORE;
        }
        if (this.CalculatedDistance_from_me > other_location.CalculatedDistance_from_me){
            return AFTER;
        }
        return this.location_type.compareTo(other_location.location_type);

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
