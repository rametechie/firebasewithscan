package com.firebase.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.walmartlabs.electrode.scanner.AbstractDetector;
import com.walmartlabs.electrode.scanner.CameraUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import walmartlabs.electrode.scanner.Barcode;
import walmartlabs.electrode.util.logging.ELog;

/**
 * Detector using the Zxing barcode detection library
 */
public class ZxingDetector extends AbstractDetector {

    private static final String TAG = "com.firebase.demo.ZxingDetector";

    private final static Map<BarcodeFormat, Barcode.Type> sDetectionTypeMap;

    static {
        // @formatter:off
        final Map<BarcodeFormat, Barcode.Type> map = new HashMap<>();
        map.put(BarcodeFormat.CODE_128, Barcode.Type.CODE_128);
        map.put(BarcodeFormat.CODE_93, Barcode.Type.CODE_93);
        map.put(BarcodeFormat.CODE_39, Barcode.Type.CODE_39);
        map.put(BarcodeFormat.UPC_A, Barcode.Type.UPC_A);
        map.put(BarcodeFormat.UPC_E, Barcode.Type.UPC_E);
        map.put(BarcodeFormat.QR_CODE, Barcode.Type.QR_CODE);
        map.put(BarcodeFormat.EAN_13, Barcode.Type.EAN_13);
        map.put(BarcodeFormat.EAN_8, Barcode.Type.EAN_8);
        sDetectionTypeMap = Collections.unmodifiableMap(map);
        // @formatter:on
    }

    @NonNull
    private final Set<Barcode.Type> mEnabledTypes;
    private final Context mContext;
    private ZXingScannerView mScannerView;
    private boolean mIsInitialized;
    private boolean mSoundEnabled;

    public ZxingDetector(@NonNull Context context,
                         @NonNull Set<Barcode.Type> enabledTypes) {
        super(context);
        mContext = context;
        mEnabledTypes = Collections.unmodifiableSet(enabledTypes);
    }

    private List<BarcodeFormat> filterEnabledTypes(@NonNull Set<Barcode.Type> enabledTypes) {
        List<BarcodeFormat> resultingTypes = new ArrayList<>();

        for (Map.Entry<BarcodeFormat, Barcode.Type> entry : sDetectionTypeMap.entrySet()) {
            if (enabledTypes.contains(entry.getValue())) {
                resultingTypes.add(entry.getKey());
            }
        }

        return resultingTypes;
    }

    @Override
    public void takePicture(@Nullable ShutterCallback shutterCallback, @Nullable PictureCallback pictureCallback) {

    }

    @Override
    @NonNull
    public Set<Barcode.Type> getSupportedTypes() {
        return Collections.unmodifiableSet(new HashSet<>(sDetectionTypeMap.values()));
    }

    @Override
    @NonNull
    public Set<Barcode.Type> getEnabledTypes() {
        return mEnabledTypes;
    }

    @Override
    public void init() {
        if (!isInitialized()) {
            mScannerView = new ZXingScannerView(mContext) {
                @Override
                protected IViewFinder createViewFinderView(Context context) {
                    return new CustomViewFinderView(context);
                }
            };
            mScannerView.setFormats(filterEnabledTypes(mEnabledTypes));
            mScannerView.setAutoFocus(true);
            mScannerView.setSquareViewFinder(false);
            mIsInitialized = true;
        }
    }

    @Override
    public boolean isInitialized() {
        return mIsInitialized;
    }

    @Override
    public void setTapToFocusEnabled(boolean enabled) {
        ELog.w(TAG, "Not Supported: setTapToFocusEnabled " + enabled);
    }

    @Override
    public void setImageEnabled(boolean enabled) {
        if (!enabled) {
            ELog.w(TAG, "Not Supported: setImageEnabled " + enabled);
        }
    }

    @Override
    public void setSoundEnabled(boolean enabled) {
        mSoundEnabled = enabled;
    }

    @Override
    public boolean isSoundEnabled() {
        return mSoundEnabled;
    }

    @Override
    public void setTorchEnabled(boolean enabled) {
        // ignored
    }

    @Override
    public boolean isTorchEnabled() {
        return false;
    }

    @Override
    public boolean isTorchAvailable() {
        return CameraUtil.isTorchPresent(mContext);
    }

    @Override
    public View getDetectorView() {
        init();
        return mScannerView;
    }

    @SuppressLint("MissingPermission")
    @Override
    @RequiresPermission(Manifest.permission.CAMERA)
    public void start() {
        //noinspection MissingPermission
        super.start();
        mScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result rawResult) {
                if (rawResult != null) {
                    final Barcode barcode = barcodeFromPayload(rawResult);

                    Barcode.Type type = barcode.getType();
                    if (mEnabledTypes.contains(type)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyBarcodeScanned(barcode);
                            }
                        });
                    } else {
                        ELog.i(TAG, "Scanned unsupported barcode: " + barcode.getValue() + " of type " + barcode.getType());
                    }

                } else {
                    onError(0);
                }
            }
        });
        mScannerView.startCamera();
    }

    private void onError(final int errorCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyError(OnDetectListener.ErrorType.ERROR, errorCode);
            }
        });
    }

    @NonNull
    private static Barcode barcodeFromPayload(@NonNull Result payload) {
        Barcode.Type type = barcodeTypeFromFormat(payload.getBarcodeFormat());
        String value = payload.getText();

        return new Barcode(type, value);
    }

    private static Barcode.Type barcodeTypeFromFormat(BarcodeFormat barcodeFormat) {
        final Barcode.Type type = sDetectionTypeMap.get(barcodeFormat);
        return type != null ? type : Barcode.Type.UNKNOWN;
    }

    @Override
    public void stop() {
        super.stop();
        mScannerView.stopCamera();
    }

    @Override
    public void release() {
        super.release();
    }

    /**
     * Custom view port that don't draw anything.
     * The app creates its own camera port view as an overlay on the scanner
     */
    private static class CustomViewFinderView extends ViewFinderView {
        private int offsetTop;

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            offsetTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    30f, getResources().getDisplayMetrics());
            int offsetSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    8f, getResources().getDisplayMetrics());
            setViewFinderOffset(offsetSize);
        }

        @Override
        public Rect getFramingRect() {
            return super.getFramingRect();
        }

        @Override
        public void onDraw(Canvas canvas) {
            // super.onDraw(canvas); // Don't draw anything, as the app creates its own view in top

            /* Add this line for showing the scanning port */
            // if (BuildConfig.DEBUG_QA_BUILD) {
            //     canvas.drawRect(getFramingRect(), mBorderPaint);
            // }
        }

        /**
         * Update the scanning area upon view size changes
         */
        @Override
        public synchronized void updateFramingRect() {
            super.updateFramingRect();
            getFramingRect().offset(0, -offsetTop);
        }
    }
}
