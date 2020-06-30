// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import io.mindblow.humanoid.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class SelectActivity extends AppCompatActivity implements View.OnClickListener {
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    private BluetoothAdapter bluetoothAdapter;
    LinearLayout pairedPeersListView;

    private Activity getThis() {
        return this;
    }

    private Map<String, String> deviceAddressMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        setTitle("Select Simulation Device:");

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceAddressMap = new HashMap<>();

        pairedPeersListView = findViewById(R.id.mapping_peers);
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            findViewById(R.id.mapping_peers).setVisibility(View.VISIBLE);
            findViewById(R.id.missing_peers).setVisibility(View.INVISIBLE);
            pairedPeersListView.removeAllViews();
            for (BluetoothDevice device : pairedDevices) {
                Button button = new Button(this);
                button.setText(device.getName());
                button.setOnClickListener(buttonOnClick);
                pairedPeersListView.addView(button);
                deviceAddressMap.put(device.getName(), device.getAddress());
            }
        } else {
            findViewById(R.id.mapping_peers).setVisibility(View.INVISIBLE);
            findViewById(R.id.missing_peers).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.missing_peers).setEnabled(false);

        ActivityHelper.onCreateButton(this, this, R.id.buttonBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack: {
                finish();
                break;
            }
        }
    }

    private View.OnClickListener buttonOnClick
            = new View.OnClickListener() {
        public void onClick(View v) {
            String deviceLabel = ((Button) v).getText().toString();
            String address = deviceAddressMap.get(deviceLabel);

            Intent intent = new Intent(getThis(), SlaveActivity.class);
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            startActivity(intent);

            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
}
