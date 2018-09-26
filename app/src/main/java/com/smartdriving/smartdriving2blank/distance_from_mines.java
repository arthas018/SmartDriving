package com.smartdriving.smartdriving2blank;

import java.util.Comparator;

/**
 * Created by Ken on 7/18/2016.
 */
public class distance_from_mines implements Comparator<LOCATIONS_INFO> {

    @Override
    public int compare(LOCATIONS_INFO a, LOCATIONS_INFO b) {

        if(a.getLocation_distance()> b.getLocation_distance()){
            return 1;
        }else{
            return -1;
        }

    }

}