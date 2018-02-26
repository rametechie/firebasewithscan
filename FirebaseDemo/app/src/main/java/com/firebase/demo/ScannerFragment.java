package com.firebase.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.firebase.demo.fragments.ProductListFragment;
import com.firebase.demo.model.Product;
import com.walmartlabs.electrode.scanner.SimpleSoundPlayer;

import com.firebase.demo.CallbackUtils;
import com.firebase.demo.PermissionUtils;
import walmartlabs.electrode.scanner.Barcode;
import walmartlabs.electrode.scanner.Detector;
import walmartlabs.electrode.scanner.EScanner;


public class ScannerFragment extends DelegateFragment implements
        ScannerOnDetectListener,
        DelegateFragment.MultiWindowSafeFragmentLifecycle {

    public static final String TAG = ScannerFragment.class.getSimpleName();

    private static final int REQUEST_CODE_PERMISSION = 123;
    private static final long RETRY_OPEN_SCANNER_AFTER_ERROR = 5000;
    private boolean mPermissionsRequested;
    private Handler mAnimateHandler = new Handler();
    //private View mPopup;
    private boolean mAnimated;

    private Detector mDetector;
    private Callbacks mCallbacks;
    private boolean mRetriedOpeningScanner = false;
    private AccurateBarcodeRetriever mAccurateBarcodeRetriever;
    private SimpleSoundPlayer mSoundPlayer;
    private MenuItem mMenuItemFlashToggle;
    private ScannerFragment.OnActionListener mShoppingListCallbacks;
    private ImageView goToCart;
    public static ScannerFragment newInstance() {
        return new ScannerFragment();
    }

    public ScannerFragment() {
        //addDelegate(new TitleDelegate(this, R.string.scanner_screen_title));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        AppCompatActivity app = (AppCompatActivity) getActivity();
        app.getSupportActionBar().setIcon(null);
        app.getSupportActionBar().setTitle("Barcode Scan");
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_scanner, container, false);

        goToCart = (ImageView) layout.findViewById(R.id.shopping_cart);
        goToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShoppingListCallbacks.openCart();


            }
        });


        if (mDetector == null) {
            mAccurateBarcodeRetriever = new AccurateBarcodeRetriever(false);
            mSoundPlayer = new SimpleSoundPlayer(getContext(), R.raw.beep);
            //Ensures we don't set this up twice as its expensive to do so.
            Log.d(TAG, "Barcode" + Barcode.Type.UPC_A + Barcode.Type.UPC_E +
                    Barcode.Type.EAN_8 + Barcode.Type.EAN_13 + Barcode.Type.CODE_128);

            mDetector = EScanner.newDetector(getActivity(), Barcode.Type.UPC_A, Barcode.Type.UPC_E,
                    Barcode.Type.EAN_8, Barcode.Type.EAN_13)
                    .buildUpon()
                    .tapToFocus()
                    .imageOnly()
                    .listener(this)
                    .build();
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            layout.addView(mDetector.getDetectorView(), 0, params);
        }

//        Button manualEntryButton = ViewUtil.findViewById(layout, R.id.manual_entry_btn);
//        manualEntryButton.setOnClickListener(new FastOnClickListenerPreventor() {
//            @Override
//            public void onClicked(View v) {
//                if (mCallbacks != null) {
//                    mCallbacks.onManualEntry();
//                }
//            }
//        });

//        mPopup = ViewUtil.findViewById(layout, R.id.scanner_popup);
//        mPopup.setVisibility(View.INVISIBLE);
        return layout;
    }

    @Override
    public void onResume() {
       // trackScreen(SCREEN_SCANNER);
        super.onResume();

        onFragmentStart();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            // TODO: This should not be started until any animation is finished
            resumeScanning();
        } else if (!mPermissionsRequested) {
            mPermissionsRequested = true;
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
        }
        if (false) {
            if (!mAnimated) {
                mAnimateHandler.postDelayed(() -> {
                    mAnimated = true;
                    animate();
                   // mPopup.setVisibility(View.VISIBLE);
                }, 5000);
            }
        } else {
            mAnimated = true;
         //   mPopup.setVisibility(View.GONE);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_barcode_scanner, menu);
        mMenuItemFlashToggle = menu.findItem(R.id.action_flash_toggle);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_flash_toggle:
                toggleFlash();
                break;
        }
        return true;

    }

    private void toggleFlash() {
        if (mDetector != null && mDetector.isInitialized() && mDetector.isTorchAvailable()) {
            mDetector.setTorchEnabled(!mDetector.isTorchEnabled());
            if (mMenuItemFlashToggle != null) {
//                int torchVectorDrawableResId = mDetector.isTorchEnabled()
//                        ? R.drawable.ic_vector_flash_off_white_24dp
//                        : R.drawable.ic_vector_flash_on_white_24dp;
//                mMenuItemFlashToggle.setIcon(torchVectorDrawableResId);
            }
        }
    }

    private void animate() {
//        ObjectAnimator translationY = ObjectAnimator.ofFloat(mPopup, View.TRANSLATION_Y, mPopup.getBottom());
//        mPopup.setTop(-mPopup.getHeight());
//        mPopup.setBottom(0);
//        translationY.setDuration(1800);
//    //    translationY.setInterpolator(new PopupBounceInterpolator());
//        translationY.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        for (int i = 0; i < permissions.length; ++i) {
            if (Manifest.permission.CAMERA.equals(permissions[i])) {
                if (PackageManager.PERMISSION_GRANTED == grantResults[i]) {
                    startScanning();
//                    new AnalyticsEvent.Builder(EVENT_CAMERA_PERMISSION)
//                            .add(Attribute.PERMISSION_STATUS, Value.PERMISSION_STATUS_ALLOWED)
//                            .post();
                } else {
//                    new AnalyticsEvent.Builder(EVENT_CAMERA_PERMISSION)
//                            .add(PERMISSION_STATUS, PERMISSION_STATUS_DENIED)
//                            .post();
                    PermissionUtils.showCameraRationale(getActivity(), new PermissionUtils.Callback() {
                        @Override
                        public void onNotShown() {
                            if (mCallbacks != null) {
//                                new AnalyticsEvent.Builder(EVENT_CAMERA_PERMISSION)
//                                        .add(PERMISSION_STATUS, PERMISSION_STATUS_DENIED_PERMANENTLY)
//                                        .post();
                                mCallbacks.onCameraPermissionDenied();
                            }
                        }

                        @Override
                        public void onRationaleShown(boolean settings) {
                            if (mCallbacks != null) {
                                if (settings) {
                                    if (PermissionUtils.hasCameraPermission(getActivity())) {
//                                        new AnalyticsEvent.Builder(EVENT_CAMERA_PERMISSION)
//                                                .add(PERMISSION_STATUS, PERMISSION_STATUS_ALLOWED)
//                                                .post();
                                        startScanning();
                                    } else {
//                                        new AnalyticsEvent.Builder(EVENT_CAMERA_PERMISSION)
//                                                .add(PERMISSION_STATUS, PERMISSION_STATUS_DENIED)
//                                                .post();
                                        mCallbacks.onCameraPermissionDenied();
                                    }
                                } else {
//                                    new AnalyticsEvent.Builder(EVENT_CAMERA_PERMISSION)
//                                            .add(PERMISSION_STATUS, PERMISSION_STATUS_DENIED)
//                                            .post();
                                    mCallbacks.onCameraPermissionDenied();
                                }
                            }
                        }
                    });
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void startScanning() {
        if (mCallbacks != null) {
            if (mDetector != null) {
                mDetector.start();
                hideFlashToggleIfNecessary();
//                new AnalyticsEvent.Builder(AnalyticConstants.EVENT_SCANNER_START)
//                        .add(Attribute.IS_INITIALIZED, mDetector.isInitialized())
//                        .add(Attribute.SCANNER_TYPE, getScannerType()).post();
            }
            if (mSoundPlayer != null) {
                mSoundPlayer.start();
            }
        }
    }

    private void hideFlashToggleIfNecessary() {
        if (mMenuItemFlashToggle != null) {
            if (!mDetector.isTorchAvailable()) {
                mMenuItemFlashToggle.setVisible(false);
            } else {
                mMenuItemFlashToggle.setVisible(true);
            }
        }
    }

    public void resumeScanning() {
        mDetector.start();
    }

    @Override
    public void stopScanning() {
        if (mCallbacks != null) {
            if (mDetector != null) {
                mDetector.stop();
            }
            if (mSoundPlayer != null) {
                mSoundPlayer.stop();
            }
        }
    }

    @Override
    public void releaseScanner() {
        if (mDetector != null) {
            mDetector.release();
            mDetector = null;
        }
        if (mSoundPlayer != null) {
            mSoundPlayer.release();
            mSoundPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        stopScanning();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        onFragmentStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseScanner();
    }


//    public static final String SCANNER_TYPE_ZXING = "zxing";
//    public static final String SCANNER_TYPE_GOOGLE = "google";
//
    public String getScannerType() {
        return "zxing";
    }

    @Override
    public boolean onBarcodeScanned(@NonNull Barcode barcode) {
        String type = getTypeString(barcode.getType());
        if (type != null && mCallbacks != null) {
//            if (barcode.hasValue() && WebViewUtils.isWebBarcode(barcode.getValue())) {
//                WebBarcode webBarcode = WebViewUtils.getWebBarcode(barcode.getValue());
//                if (webBarcode != null && !TextUtils.isEmpty(webBarcode.getTitle()) && !TextUtils.isEmpty(webBarcode.getUrl())) {
//                    WebViewUtils.trackWebBarcodeScan(webBarcode.getUpc());
//                    ELog.d(TAG, "Title: " + webBarcode.getTitle() + ", UPC: " + webBarcode.getUpc() + ", URL: " + webBarcode.getUrl());
//                    WebViewActivity.start(getActivity(), webBarcode.getTitle(), webBarcode.getUrl(), true);
//                    return false;
//                }
//            }
            if (mAccurateBarcodeRetriever.isAccurateBarcodeValue(barcode)) {
                mSoundPlayer.playSound();
                trackScan(barcode);
                stopScanning();
                mCallbacks.onBarcodeScanned(type, barcode.getValue());

                Log.d(TAG,barcode.getValue());
            }
        }
        return false;
    }

    @Override
    public void onError(Detector.OnDetectListener.ErrorType type, int statusCode) {
        mAnimateHandler.removeCallbacksAndMessages(null);
//        if (AppConfig.SCANNER_TYPE_GOOGLE.equals(getScannerType())) {
//            if (statusCode == Detector.SC_CAMERA_DETECTOR_NOT_OPERATIONAL && !mRetriedOpeningScanner) {
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mDetector != null) {
//                            mRetriedOpeningScanner = true;
//                            startScanning();
////                            new AnalyticsEvent.Builder(AnalyticConstants.EVENT_SCANNER_START)
////                                    .add(Analytics.Attribute.IS_INITIALIZED, mDetector.isInitialized())
////                                    .add(Analytics.Attribute.SCANNER_TYPE, getScannerType()).post();
//                        }
//                    }
//                }, RETRY_OPEN_SCANNER_AFTER_ERROR);
//            } else {
//                if (isResumed() && getActivity() != null && !getActivity().isFinishing()) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
//                            .setCancelable(true)
//                            .setPositiveButton(android.R.string.ok, null);
//                    builder.setMessage(R.string.scanner_not_operational);
//                    builder.show();
//                }
//            }
//        }
    }

    private static String getTypeString(Barcode.Type type) {
        switch (type) {
            case UPC_A:
                return CheckDigitUtil.SERVICE_STRING_UPC_A;
            case UPC_E:
                return CheckDigitUtil.SERVICE_STRING_UPC_E;
            case EAN_8:
                return CheckDigitUtil.SERVICE_STRING_EAN_8;
            case EAN_13:
                return CheckDigitUtil.SERVICE_STRING_EAN_13;
        }
        return null;
    }

    private void trackScan(@NonNull Barcode barcode) {
//        new AnalyticsEvent.Builder(EVENT_SCAN_ITEM)
//                .add(BARCODE, barcode)
//                .post();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = CallbackUtils.assertCallbacks(this, context, Callbacks.class);

          }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mShoppingListCallbacks = (ShoppingActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onFragmentStart() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            // TODO: This should not be started until any animation is finished
            startScanning();
//            new AnalyticsEvent.Builder(AnalyticConstants.EVENT_CAMERA_PERMISSION)
//                    .add(Attribute.PERMISSION_STATUS, Value.PERMISSION_STATUS_ALLOWED)
//                    .post();
        } else if (!mPermissionsRequested) {
            mPermissionsRequested = true;
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
        }
        if (false) {
            if (!mAnimated) {
                mAnimateHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAnimated = true;
                        animate();
                       // mPopup.setVisibility(View.VISIBLE);
                    }
                }, 5000);
            }
        } else {
            mAnimated = true;
//            mPopup.setVisibility(View.GONE);


        }
    }

    @Override
    public void onFragmentStop() {
        mAnimateHandler.removeCallbacksAndMessages(null);
        stopScanning();
    }

    public interface Callbacks {
        void onBarcodeScanned(String format, String identifier);

        void onManualEntry();

        void onCameraPermissionDenied();
    }
    public interface OnActionListener {

        void openCart();

    }


}