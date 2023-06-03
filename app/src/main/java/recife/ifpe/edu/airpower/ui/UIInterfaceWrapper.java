package recife.ifpe.edu.airpower.ui;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import androidx.fragment.app.Fragment;

public abstract class UIInterfaceWrapper {
    public interface FragmentUtil {
        void openFragment(Fragment fragment, boolean addToBackStack);
    }
}