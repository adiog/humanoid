// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import io.mindblow.humanoid.R;
import io.mindblow.humanoid.bluetooth.ClientMatcher;
import io.mindblow.humanoid.canvas.model.Segment;


public class MappingActivity extends AppCompatActivity implements View.OnClickListener {
    private BluetoothAdapter bluetoothAdapter;

    private HashMap<Segment, String> segmentDeviceMap = new HashMap<>();
    private HashMap<String, Segment> deviceSegmentMap = new HashMap<>();
    private HashMap<Integer, Segment> spinnerSegmentMap = new HashMap<>();
    private HashMap<Segment, Integer> segmentSpinnerIdMap = new HashMap<>();

    private HashMap<String, String> deviceAddressMap = new HashMap<>();

    private HashMap<Integer, Spinner> idSpinnerMap = new HashMap<>();

    Vector<String> devices = new Vector<>();

    Segment[] useSegments = new Segment[]{
/*            Segment.ForearmLeft,
            Segment.UpperArmLeft,
            Segment.ForearmRight,
            Segment.UpperArmRight,*/
            Segment.LowerLegLeft,
            Segment.UpperLegLeft,
            Segment.LowerLegRight,
            Segment.UpperLegRight
    };

    private String getSegmentLabel(Segment segment) {
        switch (segment) {
            case ForearmLeft:
                return "Forearm Left";
            case UpperArmLeft:
                return "Upper Arm Left";
            case ForearmRight:
                return "Forearm Right";
            case UpperArmRight:
                return "Upper Arm Right";
            case LowerLegLeft:
                return "Crus Left";
            case UpperLegLeft:
                return "Thigh Left";
            case LowerLegRight:
                return "Crus Right";
            case UpperLegRight:
                return "Thigh Right";
            default:
                return "Unknown";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);

        ActivityHelper.onCreateButton(this, this, R.id.buttonMaster);
        ActivityHelper.onCreateButton(this, this, R.id.buttonBack);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        LinearLayout mappingPeers = findViewById(R.id.mapping_peers);
        mappingPeers.removeAllViews();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        devices = new Vector<>();
        segmentDeviceMap = new HashMap<>();
        deviceSegmentMap = new HashMap<>();
        spinnerSegmentMap = new HashMap<>();
        segmentSpinnerIdMap = new HashMap<>();

        deviceAddressMap = new HashMap<>();

        idSpinnerMap = new HashMap<>();

        ClientMatcher clientMatcher = new ClientMatcher(this);

        for (BluetoothDevice device : pairedDevices) {
            deviceAddressMap.put(device.getName(), device.getAddress());
            devices.add(device.getName());

            Segment segment = clientMatcher.match(device.getAddress());
            if (segment != null) {
                segmentDeviceMap.put(segment, device.getName());
                deviceSegmentMap.put(device.getName(), segment);
            }
        }

        for (Segment segment : useSegments) {
            TextView label = new TextView(this);
            label.setText(getSegmentLabel(segment));

            Spinner spinner = new Spinner(this);
            spinner.setId(View.generateViewId());
            spinnerSegmentMap.put(spinner.getId(), segment);
            segmentSpinnerIdMap.put(segment, spinner.getId());
            idSpinnerMap.put(spinner.getId(), spinner);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
            for (String name : devices) {
                adapter.add(name);
            }
            adapter.add("unassigned");
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new SpinnerListener(segment, spinner));
            String markDevice = segmentDeviceMap.get(segment);
            if (markDevice != null) {
                spinner.setSelection(devices.indexOf(markDevice));
            } else {
                spinner.setSelection(pairedDevices.size());
            }

            mappingPeers.addView(label);
            mappingPeers.addView(spinner);
        }
    }

    class SpinnerListener implements AdapterView.OnItemSelectedListener {
        Segment segment;
        Spinner spinner;

        public SpinnerListener(Segment segment, Spinner spinner) {
            this.segment = segment;
            this.spinner = spinner;
            Log.v("SPINNER", String.format("%s %s", getSegmentLabel(segment), String.valueOf(spinner.getId())));
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            if (position < devices.size()) {
                String deviceSelected = devices.elementAt(position);

                try {
                    Segment segmentOff = deviceSegmentMap.get(deviceSelected);
                    if (segmentOff != null) {
                        Spinner spinnerOff = idSpinnerMap.get(segmentSpinnerIdMap.get(segmentOff));
                        spinnerOff.setSelection(devices.size(), false);
                        if (deviceSegmentMap.containsKey(deviceSelected)) {
                            deviceSegmentMap.remove(deviceSelected);
                        }
                        if (segmentDeviceMap.containsKey(segmentOff)) {
                            segmentDeviceMap.remove(segmentOff);
                        }
                    }
                } catch (Exception e) {

                }

                deviceSegmentMap.put(deviceSelected, segment);
                segmentDeviceMap.put(segment, deviceSelected);
            } else {
                String device = segmentDeviceMap.get(segment);
                segmentDeviceMap.remove(segment);
                deviceSegmentMap.remove(device);
            }
            spinner.setSelection(position, false);

            SharedPreferences.Editor editor = getSharedPreferences("HUMANOID_PREFERENCES", MODE_PRIVATE).edit();
            String mapping = "";
            for (String device : deviceSegmentMap.keySet()) {
                Segment associatedSegment = deviceSegmentMap.get(device);
                if (!mapping.equals("")) {
                    mapping += ";";
                }
                mapping += String.format("%s#%d", deviceAddressMap.get(device), associatedSegment.ordinal());
            }
            editor.putString("mapping", mapping);
            editor.apply();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonMaster: {
                Intent intent = new Intent(this, MasterActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.buttonBack: {
                finish();
                break;
            }
        }
    }
}
