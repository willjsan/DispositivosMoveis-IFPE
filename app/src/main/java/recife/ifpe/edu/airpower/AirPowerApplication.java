package recife.ifpe.edu.airpower;/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Context;

import recife.ifpe.edu.airpower.model.repo.database.AirPowerDatabase;

public class AirPowerApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        getDataBaseInstance(this);
    }

    private void getDataBaseInstance(Context context) {
        AirPowerDatabase.getDataBaseInstance(context);
    }
}
