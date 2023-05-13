package recife.ifpe.edu.airpower.util;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

public interface AirPowerConstants {

    String KEY_COD_DRAWABLE = "drawable";
    String KEY_DEVICE_DESCRIPTION = "device_description";
    String DEVICE_ITEM_INDEX = "device_iem_intex";
    String KEY_DEVICE_ID = "key_device_id";
    String KEY_EDIT_DEVICE = "edit_device";
    String KEY_ACTION = "key_action";

    int DEVICE_CONNECTION_SUCCESS = 1;
    int DEVICE_CONNECTION_FAIL = 2;
    int NETWORK_CONNECTION_SUCCESS = 3;
    int NETWORK_CONNECTION_FAIL = 4;

    int ACTION_NONE = -1;
    int ACTION_NEW_DEVICE = 1;
    int ACTION_EDIT_DEVICE = 2;
}