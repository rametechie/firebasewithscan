package com.firebase.demo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

public class PermissionUtils {

    private static final String TAG = PermissionUtils.class.getSimpleName();

    public interface Callback {
        // onNotShown is called back when we do not show our own rationale dialog
        // (i.e., "never ask again" has not been checked)
        void onNotShown();

        // onRationaleShown is called back when we do not have the permission
        // and "never show again" has been selected
        // and we then show our own rationale dialog with option to goTo os settings
        // We call back after our dialog is satisfied with:
        // with settings=true is the user selects that option
        // otherwise we call with settings=false
        void onRationaleShown(boolean settings);
    }

    /**
     * shows our own rationale dialog for location if "never ask again" has been checked
     *
     * @param activity
     *         - our host activity
     * @param callback
     */
    public static void showLocationRationale(@NonNull final Activity activity,
                                             @NonNull final Callback callback) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            showRationale(activity, callback, activity.getString(R.string.permission_location));
        } else {
            callback.onNotShown();
        }
    }

    /**
     * shows our own rationale dialog for camera if "never ask again" has been checked
     *
     * @param activity
     *         - our host activity
     * @param callback
     */
    public static void showCameraRationale(@NonNull final Activity activity,
                                           @NonNull final Callback callback) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            showRationale(activity, callback, activity.getString(R.string.permission_camera));
        } else {
            callback.onNotShown();
        }
    }

    private static void showRationale(@NonNull final Activity activity,
                                      @NonNull final Callback callback,
                                      @NonNull String permissionName) {
        AlertDialog dialog = createPermissionRationaleDialog(activity, callback, permissionName);
        dialog.show();
    }

    private static AlertDialog createPermissionRationaleDialog(@NonNull final Activity activity,
                                                               @NonNull final Callback callback,
                                                               @NonNull final String permissionName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final Resources res = activity.getResources();
        builder.setTitle(res.getString(R.string.permission_title))
                .setCancelable(false)
                .setMessage(res.getString(R.string.permission_text, permissionName))
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    SettingsUtils.goToAppSettings(activity, res.getString(R.string.permission_toast, permissionName));
                    callback.onRationaleShown(true);
                })
                .setNegativeButton(res.getString(R.string.permission_no), (dialog, which) -> callback.onRationaleShown(false));

        return builder.create();
    }

    public static boolean hasCameraPermission(@NonNull Activity activity) {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
}
