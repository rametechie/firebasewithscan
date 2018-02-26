package com.firebase.demo;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import walmartlabs.electrode.scanner.Barcode;

//import static com.samsclub.sng.util.CollectionUtils.getMostCommonOccurence;

@SuppressWarnings("unused")
public class AccurateBarcodeRetriever {

    private final ArrayList<String> mScannedBarcodes;
    private final boolean mIsEnabled;
    private long mNumScanSamples;

    public AccurateBarcodeRetriever(boolean isEnabled) {
        this(isEnabled, 3);
    }

    @SuppressWarnings("WeakerAccess")
    public AccurateBarcodeRetriever(boolean isEnabled, long numScanSamples) {
        mIsEnabled = isEnabled;
        mScannedBarcodes = new ArrayList<>(6);
        mNumScanSamples = numScanSamples;
    }

    public long getNumScanSamples() {
        return mNumScanSamples;
    }

    public void setNumScanSamples(long numScanSamples) {
        mNumScanSamples = numScanSamples;
    }

    public boolean isAccurateBarcodeValue(@NonNull Barcode barcode) {
        if (!mIsEnabled) {
            return true;
        }
        if (mScannedBarcodes.size() <= mNumScanSamples) {
            mScannedBarcodes.add(barcode.getValue());
            return false;
        } else {
            String mostCommonOccurrence = getMostCommonOccurence(mScannedBarcodes);
            if (TextUtils.isEmpty(mostCommonOccurrence)) {
                resetValues();
                return false;
            } else if (mostCommonOccurrence.equals(barcode.getValue())) {
                resetValues();
                return true;
            }
            removeValuesOf(barcode);
            return false;
        }
    }

    private void removeValuesOf(@NonNull Barcode barcode) {
        for (int i = 0; i < mScannedBarcodes.size(); i++) {
            String scannedBarcode = mScannedBarcodes.get(i);
            if (scannedBarcode.equals(barcode.getValue())) {
                mScannedBarcodes.remove(i);
                //decrement index to continue iterating properly
                i--;
            }
        }
    }

    private void resetValues() {
        mScannedBarcodes.clear();
    }

    public static String getMostCommonOccurence(@NonNull List<String> list) {
        Map<String, Integer> occurrencesMap = new HashMap<>(list.size());
        Map.Entry<String, Integer> mostCommonEntry = null;
        for (String listItem : list) {
            Integer count = occurrencesMap.get(listItem);
            occurrencesMap.put(listItem, count == null ? 1 : count + 1);
        }
        for (Map.Entry<String, Integer> set : occurrencesMap.entrySet()) {
            if (mostCommonEntry == null
                    || set.getValue() > mostCommonEntry.getValue()) {
                mostCommonEntry = set;
            }
        }

        if (occurrencesMap.size() == list.size() || mostCommonEntry == null) {
            return "";
        }

        return mostCommonEntry.getKey();
    }
}
