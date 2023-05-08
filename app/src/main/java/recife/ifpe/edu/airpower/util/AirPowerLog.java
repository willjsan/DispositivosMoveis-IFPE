package recife.ifpe.edu.airpower.util;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */
import android.util.Log;

public class AirPowerLog {
    private static final String TAG = "AirPowerApp";
    public static final boolean ISLOGABLE = true;

    public static void d(String subtag, String message) {
        Log.d(TAG, " [" + subtag + "] : " + message);
    }

    public static void d(String subtag, Thread thread, String message) {
        Log.d(TAG, " [" + subtag + "] " + " [" + thread.getName() + "] " + message);
    }

    public static void e(String subtag, String message) {
        Log.e(TAG, " [" + subtag + "] : " + message);
    }

    public static void w(String subtag, String message) {
        Log.w(TAG, " [" + subtag + "] : " + message);
    }
}