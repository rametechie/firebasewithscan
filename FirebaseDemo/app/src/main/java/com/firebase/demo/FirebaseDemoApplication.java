package com.firebase.demo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.walmartlabs.electrode.core.ElectrodeCore;

import java.util.Arrays;
import java.util.HashSet;

import walmartlabs.electrode.core.Electrode;
import walmartlabs.electrode.scanner.Barcode;
import walmartlabs.electrode.scanner.Detector;
import walmartlabs.electrode.scanner.DetectorProvider;

//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;


public class FirebaseDemoApplication extends Application {
    private static String TAG = FirebaseDemoApplication.class.getSimpleName();





    @Override
    public void onCreate() {
        super.onCreate();

        setupElectrode();


    }



    /*
    public List<ProductSingle> getmRecommended() {
        return mRecommended;
    }

    public void setmRecommended(List<ProductSingle> mRecommended) {
        this.mRecommended = mRecommended;
    }
     */




    protected void setupElectrode() {
        Electrode.init(new SngCore(this));
    }

    public class SngCore extends ElectrodeCore {


        public SngCore(Context context) {
            super(context);

        }



//        @Override
//        protected OkHttpClient createHttpClient() {
//            return createOkHttpClient(SngApplication.this, 10, 10, 30);
//        }



        @Override
        public DetectorProvider createDetectorProvider() {
            return new DetectorProvider() {
                @Override
                public Detector getDetector(@NonNull Activity activity,
                                            @NonNull Barcode.Type... types) {
                //    if (AppConfig.SCANNER_TYPE_ZXING.equals("zxing")) {
                        return new ZxingDetector(activity, new HashSet<>(Arrays.asList(types)));
                 //   }
                 //   return new GoogleDetector(activity, new HashSet<>(Arrays.asList(types)));
                }
            };
        }
    }

}
