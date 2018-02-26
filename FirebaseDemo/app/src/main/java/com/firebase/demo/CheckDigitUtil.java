package com.firebase.demo;

public class CheckDigitUtil {
    static final String SERVICE_STRING_UPC_A = "UPC";
    static final String SERVICE_STRING_UPC_E = "UPCE";
    static final String SERVICE_STRING_EAN_8 = "EAN8";
    static final String SERVICE_STRING_EAN_13 = "EAN13";

    public static boolean isCheckDigitValid(String barcode) {
        if (barcode != null && barcode.length() > 1) {
            int checkDigit = Character.getNumericValue(barcode.charAt(barcode.length() - 1));

            int sum = 0;
            int index = 0;
            for (int p = barcode.length() - 2; p >= 0; p--) {
                int digit = Character.getNumericValue(barcode.charAt(p));
                if (index % 2 == 0) {
                    sum += digit * 3;
                } else {
                    sum += digit;
                }
                index++;
            }

            return (10 - sum % 10) % 10 == checkDigit;
        }

        return false;
    }

    public static String getType(String barcode) {
        String type = null;
        if (barcode != null && !barcode.isEmpty()) {
            type = CheckDigitUtil.SERVICE_STRING_UPC_E;
            if (barcode.length() >= 13) {
                type = CheckDigitUtil.SERVICE_STRING_EAN_13;
            } else if (barcode.length() == 12) {
                type = CheckDigitUtil.SERVICE_STRING_UPC_A;
            }
        }
        return type;
    }
}
