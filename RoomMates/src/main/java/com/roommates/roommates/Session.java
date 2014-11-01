package com.roommates.roommates;

/**
 * Clase para controlar la sesi&oacute;n actual.
 */
public class Session {
    public static String email;
    public static String password;

    public static String currentApartmentID;
    public static String currentApartmentName;
    public static String currentRole;

    public static String name;
    public static String color;

    public static boolean valid = false;

    public static void reset() {
        password = null;
        currentApartmentName = null;
        currentApartmentID = null;
        currentRole = null;
        name = null;
        color = null;

        valid = false;
    }
}
