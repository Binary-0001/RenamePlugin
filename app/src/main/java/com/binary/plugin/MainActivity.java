package com.binary.plugin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xinmeng.shadow.mediation.MediationConfig;
import com.xinmeng.shadow.mediation.MediationManager;

@ClassRename
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MediationConfig config = new MediationConfig.Builder()
                .build();
        MediationManager.init(config);
    }

    public void doASM(View view) {
        Intent intent = new Intent();
        HookUtils.createIntent(this, intent);
        startActivity(intent);
    }
}