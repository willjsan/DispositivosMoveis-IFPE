package recife.ifpe.edu.airpower.util;
/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import recife.ifpe.edu.airpower.R;

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
            drawable = ResourcesCompat.getDrawable(resources, idDrawable, null); // TODO test it
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Can't retrieve drawable resource");
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
}
