package recife.ifpe.edu.airpower.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import recife.ifpe.edu.airpower.R;

public class MainActivity extends AppCompatActivity {

    Button mButtonHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonHome = findViewById(R.id.button_menu_home);

        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DeviceDetailActivity.class);
                startActivity(i);
            }
        });
    }
}