package com.example.camera_configs;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Size;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "com.example.camera_configs/configs";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler((call, result) -> {
                    if (call.method.equals("getCameraConfigs")) {
                        try {
                            List<String> configs = getCameraConfigurations();
                            result.success(configs);
                        } catch (Exception e) {
                            result.error("UNAVAILABLE", "Failed to fetch camera configurations", e.getMessage());
                        }
                    } else {
                        result.notImplemented();
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private List<String> getCameraConfigurations() throws Exception {
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        List<String> cameraConfigs = new ArrayList<>();

        for (String cameraId : cameraManager.getCameraIdList()) {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            if (map != null) {
                cameraConfigs.add("Camera ID: " + cameraId);

                // Fetch output resolutions for JPEG
                Size[] jpegSizes = map.getOutputSizes(android.graphics.ImageFormat.JPEG);
                if (jpegSizes != null) {
                    for (Size size : jpegSizes) {
                        cameraConfigs.add("JPEG: " + size.getWidth() + "x" + size.getHeight());
                    }
                }

                // // Fetch output resolutions for YUV
                // Size[] yuvSizes = map.getOutputSizes(android.graphics.ImageFormat.YUV_420_888);
                // if (yuvSizes != null) {
                //     for (Size size : yuvSizes) {
                //         cameraConfigs.add("YUV: " + size.getWidth() + "x" + size.getHeight());
                //     }
                // }
            }
        }

        return cameraConfigs;
    }
}
