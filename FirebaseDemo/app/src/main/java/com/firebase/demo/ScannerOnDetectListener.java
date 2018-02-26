package com.firebase.demo;

import walmartlabs.electrode.scanner.Detector.OnDetectListener;

public interface ScannerOnDetectListener extends OnDetectListener {

    void startScanning();

    void stopScanning();

    void releaseScanner();

}
