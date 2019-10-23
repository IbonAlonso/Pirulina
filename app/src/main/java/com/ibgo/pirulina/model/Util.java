package com.ibgo.pirulina.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;

public abstract class Util {

    public static Comparator<String> COMPARE_STRINGS = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    };

    public static String md5(String s) {
        String md5Hex = DigestUtils
                .md5Hex(s).toUpperCase();
        return md5Hex;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static double calculateDistance(Position currentPos, Position targetPos) {
        final double EARTH_RADIUS = 6378;
        double latDiff = toRad(currentPos.latitude - targetPos.latitude);
        double lonDiff = toRad(currentPos.longitude - targetPos.longitude);

        double x = Math.pow(Math.sin(latDiff / 2), 2) + Math.cos(toRad(currentPos.latitude))
                * Math.cos(toRad(targetPos.latitude)) * Math.pow(Math.sin(lonDiff / 2), 2);
        double a = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x));

        return EARTH_RADIUS * a;
    }

    public static double toRad(double value) {
        return (Math.PI / 180) * value;
    }

    public static class Position {
        public double latitude;
        public double longitude;

        public Position(String coordenadas) {
            String[] coords = coordenadas.split(",");
            latitude = Double.parseDouble(coords[0]);
            longitude = Double.parseDouble(coords[1]);
        }

        public Position() {
        }
    }
}
