package com.firebase.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.List;

import walmartlabs.electrode.util.logging.ELog;


public class SettingsUtils {

    private static final String TAG = SettingsUtils.class.getSimpleName();

    /**
     * launches the Settings App permissions and shows a toast with directions to adjust permission
     *
     * @param activity
     *         - the host activity
     * @param toastText
     *         - text to show over settings
     */
    public static void goToAppSettings(@NonNull Activity activity, @NonNull String toastText) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        PackageManager manager = activity.getPackageManager();
        List<ResolveInfo> resolveInfoList = manager.queryIntentActivities(intent, 0);
        if (resolveInfoList.size() > 0) {
            activity.startActivity(intent);
            Toast.makeText(activity, toastText, Toast.LENGTH_LONG).show();
        } else {
            ELog.d(TAG, "Could not find settings app!");
            Toast.makeText(activity, "Cannot find settings app! Please manually change app settings.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * launches the Settings App permissions and shows a toast with directions to adjust permission
     *
     * @param activity
     *         - the host activity
     */
    public static void goToAppSettings(@NonNull Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        PackageManager manager = activity.getPackageManager();
        List<ResolveInfo> resolveInfoList = manager.queryIntentActivities(intent, 0);
        if (resolveInfoList.size() > 0) {
            activity.startActivity(intent);
        } else {
            ELog.d(TAG, "Could not find settings app!");
            Toast.makeText(activity, "Cannot find settings app! Please manually change app settings.", Toast.LENGTH_LONG).show();
        }
    }

    public static void goToWifiSettings(@NonNull Activity activity) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        PackageManager manager = activity.getPackageManager();
        List<ResolveInfo> resolveInfoList = manager.queryIntentActivities(intent, 0);
        if (resolveInfoList.size() > 0) {
            activity.startActivity(intent);
        } else {
            ELog.d(TAG, "Could not find settings app!");
            Toast.makeText(activity, "Cannot find settings app! Please manually change wifi settings.", Toast.LENGTH_LONG).show();
        }
    }
}
