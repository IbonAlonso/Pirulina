package com.ibgo.pirulina.model;

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
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
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
