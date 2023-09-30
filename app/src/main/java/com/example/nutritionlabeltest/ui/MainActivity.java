package com.example.nutritionlabeltest.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;

import com.example.nutritionlabeltest.R;
import com.example.nutritionlabeltest.custom.NorthUI;
import com.example.nutritionlabeltest.custom.UnitCallback;
import com.example.nutritionlabeltest.custom.Utils;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {

    // UI
    private Button actionBtn;

    // Other
    private String cameraPermission = Manifest.permission.CAMERA;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startScanner();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doUI();
    }

    private void doUI() {
        NorthUI.setAndroidUI(this, R.color.background_dark);
        actionBtn = findViewById(R.id.action_btn);

        actionBtn.setOnClickListener(view -> requestCameraAndStartScanner());
    }

    private void requestCameraAndStartScanner() {
        if (ContextCompat.checkSelfPermission(this, cameraPermission) == PackageManager.PERMISSION_GRANTED) {
            startScanner();
        } else {
            requestCameraPermission();
        }
    }

    private void startScanner() {
        ScannerActivity.startScanner(this, new UnitCallback() {
            @Override
            public void onSuccess() {

            }
        });
    }

    private void requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(cameraPermission)) {
            Context parentContext = this;
            Utils.cameraPermissionRequest(this, new UnitCallback() {
                @Override
                public void onSuccess() {
                    Utils.openPermissionSettings(parentContext);
                }
            });

        } else {
            requestPermissionLauncher.launch(cameraPermission);
        }
    }
}