package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import recife.ifpe.edu.airpower.model.repo.model.user.AirPowerUser;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class AirPowerAuthenticationManagerImpl
        implements ServersInterfaceWrapper.IAirPowerAuthManager {

    private static String TAG = AirPowerAuthenticationManagerImpl.class.getSimpleName();

    private static AirPowerAuthenticationManagerImpl instance;

    private AirPowerAuthenticationManagerImpl() {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "instantiation");
    }

    public static AirPowerAuthenticationManagerImpl getInstance() {
        if (instance == null) {
            instance = new AirPowerAuthenticationManagerImpl();
        }
        return instance;
    }

    @Override
    public void register(AirPowerUser user, ServersInterfaceWrapper.IUserAuthCallback callback) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "register");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "register failure\n"
                        + task.getException().getMessage());
                        callback.onFailure(task.getException().getMessage());
                    }
                });

    }

    @Override
    public void signIn(AirPowerUser user, ServersInterfaceWrapper.IUserAuthCallback callback) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "signIn");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        callback.onSuccess(null);
                    } else {
                        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "signIn failure");
                        callback.onFailure(task.getException().getMessage());
                    }

                });
    }

    @Override
    public void signOut(ServersInterfaceWrapper.IUserAuthCallback callback) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "signOut");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.signOut();
            callback.onSuccess(null);
        } else {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.w(TAG, "user is null");
            callback.onFailure(null);
        }
    }

    @Override
    public void unRegister(AirPowerUser user, ServersInterfaceWrapper.IUserAuthCallback callback) {
        if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "unRegister: Stub");
    }
}