package recife.ifpe.edu.airpower.util;
/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.model.device.AirPowerDevice;

public class AirPowerUtil {

    private static String TAG = AirPowerUtil.class.getSimpleName();

    public static Drawable getDrawable(String name, Context context) {
        if (context == null) {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "getDrawable: context is null");
            return null;
        }
        Resources resources = context.getResources();
        Drawable drawable;
        try {
            int idDrawable = resources.getIdentifier(name, AirPowerConstants.KEY_COD_DRAWABLE,
                    context.getPackageName());
            drawable = ResourcesCompat.getDrawable(resources, idDrawable, null);
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.w(TAG, "can't retrieve drawable resource: getting default");
            drawable = ResourcesCompat
                    .getDrawable(resources, R.drawable.airpower_launcher_icon, null);
        }
        return drawable;
    }

    public static ProgressDialog getProgressDialog(Context context, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        return dialog;
    }

    /**
     * This should be used JUST for testing
     *
     * @return list of mocked devices
     */
    @Deprecated
    public static List<AirPowerDevice> getDevicesMock() {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.e(TAG, "getDevicesMock. FIX ME");
        return new ArrayList<>(Arrays.asList(
                new AirPowerDevice("name 1", "desc 1", "local 1", "airpower_launcher_icon", "token 1", "ssid 1", "pass 1", "url 1"),
                new AirPowerDevice("name 2", "desc 2", "local 2", "airpower_launcher_icon", "token 2", "ssid 2", "pass 2", "url 2"),
                new AirPowerDevice("name 3", "desc 3", "local 3", "airpower_launcher_icon", "token 3", "ssid 3", "pass 3", "url 3"),
                new AirPowerDevice("name 4", "desc 4", "local 4", "airpower_launcher_icon", "token 4", "ssid 4", "pass 4", "url 4"),
                new AirPowerDevice("name 5", "desc 5", "local 5", "airpower_launcher_icon", "token 5", "ssid 5", "pass 5", "url 5"),
                new AirPowerDevice("name 6", "desc 6", "local 6", "airpower_launcher_icon", "token 6", "ssid 6", "pass 6", "url 6"),
                new AirPowerDevice("name 7", "desc 7", "local 7", "airpower_launcher_icon", "token 7", "ssid 7", "pass 7", "url 7"),
                new AirPowerDevice("name 8", "desc 8", "local 8", "airpower_launcher_icon", "token 8", "ssid 8", "pass 8", "url 8"),
                new AirPowerDevice("name 9", "desc 9", "local 9", "airpower_launcher_icon", "token 9", "ssid 9", "pass 9", "url 9"),
                new AirPowerDevice("name 10", "desc 10", "local 10", "airpower_launcher_icon", "token 10", "ssid 10", "pass 10", "url 10")

        ));
    }

    public static String kelvinToCelsius(float temperatureKelvin) {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "kelvinToCelsius");
        float temperatureCelsius = temperatureKelvin - 273.15f;
        return String.format(Locale.getDefault(), "%.1f°C", temperatureCelsius);
    }

    public static String getCurrentDateTime() {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "getCurrentDateTime");
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }


    public static void getCurrentLocation(Context context,
                                          Activity activity,
                                          ILocationCallback callback) {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "getCurrentLocation");
        requestPermission(context, activity);
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        FusedLocationProviderClient providerClient =
                LocationServices.getFusedLocationProviderClient(context);
        providerClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,
                        new CancellationToken() {
                            @NonNull
                            @Override
                            public CancellationToken onCanceledRequested(
                                    @NonNull OnTokenCanceledListener onTokenCanceledListener) {
                                return null;
                            }

                            @Override
                            public boolean isCancellationRequested() {
                                return false;
                            }
                        })
                .addOnSuccessListener(location -> {
                    if (location == null) {
                        if (AirPowerLog.ISLOGABLE)
                            AirPowerLog.w(TAG, "location is null");
                        return;
                    }
                    if (AirPowerLog.ISLOGABLE)
                        AirPowerLog.d(TAG, "lat,lon:" + location.getLatitude() + ","
                                + location.getLongitude());
                    callback.onSuccess(location.getLatitude() + ","
                            + location.getLongitude());
                });
    }

    private static void requestPermission(Context context, Activity activity) {
        if (AirPowerLog.ISLOGABLE)
            AirPowerLog.d(TAG, "requestPermission");
        final int FINE_LOCATION_REQUEST = 10;
        try {
            int permissionCheck = ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION);
            boolean locationGrantedByUser = (permissionCheck == PackageManager.PERMISSION_GRANTED);
            if (locationGrantedByUser) return;
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    FINE_LOCATION_REQUEST);
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "error while getting permission");
        }
    }

    public interface ILocationCallback {
        void onSuccess(String localization);
    }
}
