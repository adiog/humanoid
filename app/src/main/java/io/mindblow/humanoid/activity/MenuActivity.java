// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import io.mindblow.humanoid.R;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ActivityHelper.onCreateButton(this, this, R.id.buttonMaster);
        ActivityHelper.onCreateButton(this, this, R.id.buttonSlave);
        ActivityHelper.onCreateButton(this, this, R.id.buttonExit);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonMaster: {
                Intent intent = new Intent(this, MappingActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.buttonSlave: {
                Intent intent = new Intent(this, SelectActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.buttonExit: {
                finish();
                break;
            }
        }
    }
}
