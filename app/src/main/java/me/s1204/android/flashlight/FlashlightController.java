package me.s1204.android.flashlight;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;

public class FlashlightController extends Activity {

    private boolean flashlight = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller);
        findViewById(R.id.flashlight).setOnClickListener(v -> flashlight());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishAndRemoveTask();
    }

    private void flashlight() {
        flashlight = !flashlight;
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        if (cameraManager == null) {
            return;
        }

        try {
            String cameraId = findFlashlight(cameraManager);
            if (cameraId != null) {
                cameraManager.setTorchMode(cameraId, flashlight);
            }
        } catch (CameraAccessException ignored) {
        }
    }

    private String findFlashlight(CameraManager cameraManager) throws CameraAccessException {
        for (String id : cameraManager.getCameraIdList()) {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
            Boolean hasFlash = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            if (hasFlash != null && hasFlash) {
                return id;
            }
        }
        return null;
    }
}
