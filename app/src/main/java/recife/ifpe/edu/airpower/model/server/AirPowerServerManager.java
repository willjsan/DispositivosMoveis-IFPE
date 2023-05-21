package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import recife.ifpe.edu.airpower.model.repo.model.AirPowerDevice;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;

/**
 * This class manages AirPower server operation
 */
public class AirPowerServerManager {

    private static final String TAG = AirPowerServerManager.class.getSimpleName();
    public static final int TIMEOUT_MILLIS = 5000;
    private static AirPowerServerManager instance = null;
    private final AirPowerConnectionManager mConnectionManager;
    private AirPowerConnection mCurrentConnection;

    private AirPowerServerManager() {
        mConnectionManager = new AirPowerConnectionManager();

    }

    public static AirPowerServerManager getInstance() {
        if (instance == null) {
            instance = new AirPowerServerManager();
        }
        return instance;
    }

    /**
     * This method runs on a UI detached thread
     *
     * @param handler  handles server response to UI
     * @param callback handles server response routine
     */
    public void registerDevice(@NonNull Handler handler,
                               @NonNull AirPowerServerInterfaceWrapper.IRegisterCallback callback) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "registerDevice()");
        try {
            performServerTaskNewThread(getRegisterTask(handler, callback), TIMEOUT_MILLIS);
        } catch (TimeoutException e) {
            mConnectionManager.closeConnection(mCurrentConnection);
        }
    }

    /**
     * This method perform device unregister on Server
     *
     * @param device  who will be unregistered
     * @param handler to handle server response on UI
     */
    public void unregisterDevice(@NonNull AirPowerDevice device, @NonNull Handler handler) {

    }

    public Map<String, String>
    getDeviceConsumption(AirPowerDevice device,
                         AirPowerServerInterfaceWrapper.IResultCallback callback) {

        return null;
    }

    /**
     * Implements device register on server task routine
     *
     * @param handler  used to interact with UI
     * @param callback used to perform desired action when done
     * @return the device register task implementation
     */
    private Callable<Integer> getRegisterTask(@NonNull Handler handler,
                                              @NonNull AirPowerServerInterfaceWrapper
                                                      .IRegisterCallback callback) {
        return () -> {
            mCurrentConnection =
                    mConnectionManager.getAirPowerConnection(null,
                            AirPowerConnectionManager.DEVICE_REGISTRY);
            mConnectionManager.writeAirPowerConnection(mCurrentConnection,
                    getResponseCallbackImpl(handler, callback));
            return 0;
        };
    }

    /**
     * This method provides implementation for responseCallBack interface
     *
     * @param handler
     * @param callback
     * @return
     */
    private AirPowerServerInterfaceWrapper.IServerResponseCallback getResponseCallbackImpl(
            @NonNull Handler handler,
            @NonNull AirPowerServerInterfaceWrapper.IRegisterCallback callback) {
        return new AirPowerServerInterfaceWrapper.IServerResponseCallback() {
            @Override
            public void onSuccess(String response) {
                callback.onTokenReceived(response);
                handler.sendEmptyMessage(AirPowerConstants
                        .NETWORK_CONNECTION_SUCCESS);

                AirPowerLog.w(TAG, "onSuccess() message: " + response); // TODO remover
            }

            @Override
            public void onFailure(String response) {
                AirPowerLog.w(TAG, "onFailure() Server message: "
                        + response);
                handler.sendEmptyMessage(AirPowerConstants
                        .NETWORK_CONNECTION_FAILURE);
            }
        };
    }

    /**
     * This method performs tasks in a new thread and
     * throws and Exception when timeout is reached or when
     * a interrupt occurs
     *
     * @param task          to be performed
     * @param timeoutMillis time to wait before throw an exception
     * @throws TimeoutException the timeout was reached
     */
    private void performServerTaskNewThread(Callable<Integer> task, long timeoutMillis)
            throws TimeoutException {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "performServerTaskNewThread()");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(task);
        executor.shutdown();
        try {
            if (executor.awaitTermination(timeoutMillis, TimeUnit.MILLISECONDS)) {
                future.get();
                AirPowerLog.d(TAG, "task finished.");
            } else {
                throw new InterruptedException();
            }
        } catch (InterruptedException e) {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "Timeout achieved");
            throw new TimeoutException();
        } catch (ExecutionException e) {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "Can't get task result");
            future.cancel(true);
            throw new TimeoutException();
        } finally {
            executor.shutdownNow();
        }
    }
}