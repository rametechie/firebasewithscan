package com.firebase.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.firebase.demo.fragments.ProductListFragment;
import com.firebase.demo.model.Product;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class ShoppingActivity extends AppCompatActivity implements
        //AddItemFragment.Callbacks,


        ScannerFragment.Callbacks, ScannerFragment.OnActionListener {

    public static final String EXTRA_STORE = "EXTRA_STORE";
    public static final String EXTRA_SHOW_COMMON_ITEMS = "EXTRA_SHOW_COMMON_ITEMS";
    public static final String EXTRA_SHOW_ITEM_CONFIRMATION = "EXTRA_SHOW_ITEM_CONFIRMATION";
    public static final String EXTRA_CART_ITEM = "EXTRA_CART_ITEM";
    private static final String BACKSTACK_ADD_ITEM = "BACKSTACK_ADD_ITEM";

    private boolean mShowCommonItems;
    private boolean mShowItemConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }

        if (savedInstanceState == null) {
            if (getIntent() != null) {
                mShowCommonItems = getIntent().getBooleanExtra(EXTRA_SHOW_COMMON_ITEMS, false);
                mShowItemConfirmation = getIntent().getBooleanExtra(EXTRA_SHOW_ITEM_CONFIRMATION, false);
                if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) ==
                        ConnectionResult.SUCCESS &&
                        getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                            ScannerFragment.newInstance(), ScannerFragment.TAG)
                            .commit();
                } else {
                    startManualInputActivity();
                }
            }
        } else {
            mShowCommonItems = savedInstanceState.getBoolean(EXTRA_SHOW_COMMON_ITEMS);
            mShowItemConfirmation = savedInstanceState.getBoolean(EXTRA_SHOW_ITEM_CONFIRMATION);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goHome();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!ClubDetectionManager.isInClub()) {
//            goHome();
//        }
    }

    @SuppressLint("CommitTransaction")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
  //      if (requestCode == ManualInputActivity.REQUEST_MANUAL_INPUT && resultCode == RESULT_OK && data != null) {
            // goHome without resuming the scanner fragment
//            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
//            if (fragment instanceof ScannerFragment) {
//                getSupportFragmentManager().beginTransaction().remove(fragment).commitNowAllowingStateLoss();
//            }
          //  CartItem cartItem = data.getParcelableExtra(ManualInputActivity.EXTRA_CART_ITEM);
//            if (cartItem != null) {
//                Intent intent = new Intent();
//                intent.putExtra(EXTRA_CART_ITEM, cartItem);
//                setResult(RESULT_OK, intent);
//            }
//            goHome();
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_SHOW_COMMON_ITEMS, mShowCommonItems);
        outState.putBoolean(EXTRA_SHOW_ITEM_CONFIRMATION, mShowItemConfirmation);
    }

    private void startManualInputActivity() {
//        Intent intent = new Intent(ShoppingActivity.this, ManualInputActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.putExtra(ManualInputActivity.EXTRA_SHOW_COMMON_ITEMS, mShowCommonItems);
//        intent.putExtra(ManualInputActivity.EXTRA_SHOW_ITEM_CONFIRMATION, mShowItemConfirmation);
//        startActivityForResult(intent, ManualInputActivity.REQUEST_MANUAL_INPUT);
    }

    protected void finishAddItem() {
        getSupportFragmentManager().popBackStack();
        if (getScannerFragment() != null) {
            getScannerFragment().startScanning();
        }
    }

    private void goHome() {
        Intent upIntent = NavUtils.getParentActivityIntent(ShoppingActivity.this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
            finish();
        } else {
            NavUtils.navigateUpTo(this, upIntent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resumeScanningIfForegrounded();
    }

    private void addItemToCart(String format, String value, boolean showPopup, String trigger) {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.content_frame,
//                        AddItemFragment.newInstance(format, value, showPopup, trigger, getScannerType()),
//                        AddItemFragment.TAG)
//                .addToBackStack(BACKSTACK_ADD_ITEM)
//                .commit();
    }

    private void resumeScanningIfForegrounded() {
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag(AddItemFragment.TAG);
//        if (fragment == null && !isFinishing() && getScannerFragment() != null) {
//            getScannerFragment().startScanning();
//        }
    }

    @Nullable
    private ScannerFragment getScannerFragment() {
        return (ScannerFragment) getSupportFragmentManager().findFragmentByTag(ScannerFragment.TAG);
    }
//
//    public String getScannerType() {
//        return AppConfig.getScannerType();
//    }

    @Override
    public void onBarcodeScanned(String format, String value) {
        launchStudentActivity(value);
//        HardwareUtils.vibrate(this);
     //   boolean hasScannedFirstItem = AnalyticPreferences.hasScannedFirstItem(this);

     //   if (!hasScannedFirstItem) {
//            AnalyticPreferences.setHasScannedFirstItem(this, true);
//            AnalyticPreferences.saveTime(this,
//                    AnalyticConstants.KEY_START_TIME_SCAN_TO_CHECKOUT,
//                    TimeUtil.elapsedRealTimeMs());
    //    }

     //   addItemToCart(format, value, false, Analytics.Value.SCAN);
    }



    @Override
    public void onManualEntry() {
       // startManualInputActivity();
    }

    @Override
    public void onCameraPermissionDenied() {
        startManualInputActivity();
    }

//    @Override
//    public boolean onAddItem(CartItem item) {
//        if (!SessionManager.getCartLimits().checkLimits(this, item, item.getQuantity())) {
//            finish();
//            return false;
//        }
//        return true;
//    }

//    @Override
//    public void onItemFound(CartItem cartItem) {
//        Intent intent = new Intent();
//        intent.putExtra(EXTRA_CART_ITEM, cartItem);
//        setResult(RESULT_OK, intent);
//        goHome();
//    }

//    @Override
//    public void onItemNotFound(String format, String identifier) {
//        finishAddItem();
//    }
private void launchMainActivity(String barcodeValue) {
    // Go to MainActivity


    Intent intent = new Intent(ShoppingActivity.this, MainActivity.class);
    intent.putExtra("productkey",barcodeValue);
    startActivity(intent);

}

    private void launchStudentActivity(String barcodeValue) {
        // Go to MainActivity


        Intent intent = new Intent(ShoppingActivity.this, CartActivity.class);
        intent.putExtra("productkey",barcodeValue);
        startActivity(intent);

    }


    @Override
    public void openCart() {
        Intent intent = new Intent(ShoppingActivity.this, CartActivity.class);

        startActivity(intent);
    }
}
