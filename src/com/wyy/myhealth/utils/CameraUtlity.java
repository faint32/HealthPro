package com.wyy.myhealth.utils;

import java.util.List;

import android.hardware.Camera;

public class CameraUtlity {

	public static boolean hasFlash(Camera camera) {
		if (camera == null) {
			return false;
		}

		Camera.Parameters parameters = camera.getParameters();

		if (parameters.getFlashMode() == null) {
			return false;
		}

		List<String> supportedFlashModes = parameters.getSupportedFlashModes();
		if (supportedFlashModes == null
				|| supportedFlashModes.isEmpty()
				|| supportedFlashModes.size() == 1
				&& supportedFlashModes.get(0).equals(
						Camera.Parameters.FLASH_MODE_OFF)) {
			return false;
		}

		return true;
	}

}
