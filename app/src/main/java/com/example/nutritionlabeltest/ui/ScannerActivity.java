package com.example.nutritionlabeltest.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraProvider;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.nutritionlabeltest.R;
import com.example.nutritionlabeltest.custom.NorthUI;
import com.example.nutritionlabeltest.custom.NutritionLabelParser;
import com.example.nutritionlabeltest.custom.UnitCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ScannerActivity extends AppCompatActivity {

    // UI
    private PreviewView previewView;

    // Other
    private CameraSelector cameraSelector;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ProcessCameraProvider processCameraProvider;
    private Preview cameraPreview;
    private ImageAnalysis imageAnalysis;
    private NutritionLabelParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        doUI();

        parser = new NutritionLabelParser(this);

        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                processCameraProvider = cameraProviderFuture.get();
                bindCameraPreview();
                bindInputAnalyzer();
            } catch (InterruptedException | ExecutionException e) {
                // Should never be reached
            }

        }, ContextCompat.getMainExecutor(this));
    }

    private void doUI() {
        NorthUI.setAndroidUI(this, R.color.background_dark);
        previewView = findViewById(R.id.preview_view);
    }

    private void bindInputAnalyzer() {
        TextRecognizer recognizer =
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        imageAnalysis = new ImageAnalysis.Builder()
                .setTargetRotation((int) previewView.getRotation())
                .build();

        Executor cameraExecutor = Executors.newSingleThreadExecutor();

        imageAnalysis.setAnalyzer(cameraExecutor, new ImageAnalysis.Analyzer() {
            @OptIn(markerClass = ExperimentalGetImage.class) @Override
            public void analyze(@NonNull ImageProxy imageProxy) {
                processImageProxy(recognizer, imageProxy);

                imageProxy.close();
            }
        });

        processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis);
    }

    @ExperimentalGetImage private void processImageProxy(TextRecognizer recognizer, ImageProxy imageProxy) {
        InputImage image = InputImage.fromMediaImage(imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());

        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                // Task completed successfully
                                // ...

                                if (visionText.getText().isEmpty()) return;

                                for (Text.TextBlock block : visionText.getTextBlocks()) {
                                    Rect boundingBox = block.getBoundingBox();
                                    Point[] cornerPoints = block.getCornerPoints();
                                    String text = block.getText();

                                    for (Text.Line line: block.getLines()) {
                                        parser.enqueueParse(line.getText());
                                        parser.parse();
                                    }
                                }

                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });



    }

    private void bindCameraPreview() {
        cameraPreview = new Preview.Builder()
                .setTargetRotation((int) previewView.getRotation())
                .build();

        cameraPreview.setSurfaceProvider(previewView.getSurfaceProvider());

        processCameraProvider.bindToLifecycle(this, cameraSelector, cameraPreview);
    }

    public static void startScanner(Context context, @Nullable UnitCallback callback) {
        context.startActivity(new Intent(context, ScannerActivity.class));
    }
}