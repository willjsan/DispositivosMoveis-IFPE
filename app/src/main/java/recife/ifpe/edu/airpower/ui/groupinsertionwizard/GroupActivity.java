package recife.ifpe.edu.airpower.ui.groupinsertionwizard;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import recife.ifpe.edu.airpower.R;
import recife.ifpe.edu.airpower.model.repo.AirPowerRepository;
import recife.ifpe.edu.airpower.ui.UIInterfaceWrapper;
import recife.ifpe.edu.airpower.util.AirPowerConstants;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class GroupActivity extends AppCompatActivity implements
        UIInterfaceWrapper.FragmentUtil,
        UIInterfaceWrapper.INavigate {

    private static final String TAG = GroupActivity.class.getSimpleName();
    private String mAction;
    private AirPowerRepository mRepo;
    private boolean mCanBackPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        if (AirPowerLog.ISLOGABLE) AirPowerLog.d(TAG, "onCreate");
        Intent intent = getIntent();
        if (intent == null) return;
        mRepo = AirPowerRepository.getInstance(getApplicationContext());
        mAction = intent.getAction();
        switch (mAction) {
            case AirPowerConstants.ACTION_NEW_GROUP:
                actionNewGroup();
                break;

            default:
                if (AirPowerLog.ISLOGABLE) {
                    AirPowerLog.e(TAG, "missing action");
                }
        }
    }

    private void actionNewGroup() {
        if (AirPowerLog.ISLOGABLE) {
            AirPowerLog.d(TAG, "actionNewGroup");
        }
        setTitle("Add New Group");
        Fragment wizard = GroupMenuFragment.newInstance();
        this.openFragment(wizard, false);
    }

    @Override
    public void openFragment(Fragment fragment, boolean addToBackStack) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.group_wizard_fragment_holder, fragment);
            if (addToBackStack) {
                transaction.addToBackStack("willian");
            }
            transaction.commit();
        } catch (NullPointerException e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Fail when getting fragment manager");
        }
    }

    @Override
    public void setBackPress(boolean canBackPress) {
        mCanBackPress = canBackPress;
    }
}