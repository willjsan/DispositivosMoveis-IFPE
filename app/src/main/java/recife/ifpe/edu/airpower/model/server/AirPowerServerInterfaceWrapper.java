package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

public abstract class AirPowerServerInterfaceWrapper {

    /**
     * Every AirPowerServer Connection manager MUST implement this interface
     */
    interface IConnectionManager {
        AirPowerConnection getAirPowerConnection(String deviceToken, int requestType);

        void writeAirPowerConnection(AirPowerConnection connection,
                                     IServerResponseCallback callback);
    }

    interface IMeasureCallback {
        void onResult(String measurement);
    }

    /**
     * This interface is user to handle server response
     */
    interface IServerResponseCallback {

        /**
         * This method should only me called in success case
         *
         * @param response from server
         */
        void onSuccess(String response);

        /**
         * This method should only me called on failure
         *
         * @param response error from server
         */
        void onFailure(String response);
    }

    public interface IResultCallback {
        void onResult(String result);
    }

    /**
     * This interface is user to handle device's registration process
     */
    public interface IRegisterCallback {
        void onTokenReceived(String token);
    }
}
