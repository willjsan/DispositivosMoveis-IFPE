package recife.ifpe.edu.airpower.util;/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class AirPowerUtil {

    public static Drawable getDrawable(String name, Context context) {
        Resources resources = context.getResources();
        int idDrawable = resources.getIdentifier(name, AirPowerConstants.KEY_COD_DRAWABLE, context.getPackageName());
        return resources.getDrawable(idDrawable);
    }
}
