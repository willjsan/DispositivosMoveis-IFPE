package recife.ifpe.edu.airpower.ui.groupinsertionwizard;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.model.user.AirPowerUser;
import recife.ifpe.edu.airpower.model.server.AirPowerAuthenticationManagerImpl;
import recife.ifpe.edu.airpower.model.server.ServersInterfaceWrapper;
import recife.ifpe.edu.airpower.ui.UIInterfaceWrapper;
import recife.ifpe.edu.airpower.ui.access.AuthActivity;
import recife.ifpe.edu.airpower.util.AirPowerLog;


public class GroupMenuFragment extends Fragment {

    private static final String TAG = GroupMenuFragment.class.getSimpleName();

    private UIInterfaceWrapper.FragmentUtil mFragmentUtil;

    private Button mNewGroupButton;
    private Button nEditGroupButton;
    private Button nDeleteGroupButton;
    private Button nAddGuestButton;

    public GroupMenuFragment() {
    }


    public static GroupMenuFragment newInstance() {
        GroupMenuFragment fragment = new GroupMenuFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_menu,
                container, false);
        findViewsByIds(view);
        setListeners();

        return view;
    }

    private void setListeners() {
        mNewGroupButton.setOnClickListener(view -> {
            mFragmentUtil.openFragment(GroupInsertionFragment.newInstance(), true);
        });
        nEditGroupButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AuthActivity.class);
            popUpToast("test");
            startActivity(intent);
        });
        nDeleteGroupButton.setOnClickListener(view -> {
            popUpToast("Not Implemented yet");
        });
        nAddGuestButton.setOnClickListener(view -> {
            popUpToast("Not Implemented yet");
        });
    }

    private void popUpToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void findViewsByIds(View view) {
        mNewGroupButton = view.findViewById(R.id.card_menu_new_group_button);
        nEditGroupButton = view.findViewById(R.id.bt_group_menu_item_edit_group);
        nDeleteGroupButton = view.findViewById(R.id.bt_group_menu_item_delete_group);
        nAddGuestButton = view.findViewById(R.id.bt_group_menu_item_add_guest);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UIInterfaceWrapper.FragmentUtil) {
            mFragmentUtil = (UIInterfaceWrapper.FragmentUtil) context;
        } else {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "no .FragmentUtil impl");
        }
    }
}