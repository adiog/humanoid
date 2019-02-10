// This file is a part of Humanoid project.
// Copyright (C) 2019 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);
        Button buttonSettings = (Button) findViewById(R.id.buttonPref);
        buttonSettings.setOnClickListener(this);
        Button buttonExit = (Button) findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlay: {
                Intent intent = new Intent(this, PlayActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.buttonPref: {
                Intent intent = new Intent(this, PrefActivity.class);
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
