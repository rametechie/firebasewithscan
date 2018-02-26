package com.firebase.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;


public class CallbackUtils {
    /**
     * Helper method for attaching callbacks to fragments.
     * This was created to avoid having all of the code below in every onAttach() for every fragment
     * with an interface.
     * Ensures the fragment's interface is implemented by its host whether the host is an activity
     * or fragment
     * and returns the host for use as a callbacks reference.
     *
     * @param fragment
     *         The fragment to attach the callbacks to.
     * @param context
     *         The host of the fragment. Usually an activity, but could also be a fragment.
     *         Do NOT pass 'this'. Pass the context parameter received in onAttach(HelpContext
     *         context)
     * @param callbacksClass
     *         The callbacks interface to attach to the fragment.
     * @param <T>
     *         The interface type. i.e. ScannerFragment.Callbacks
     *
     * @return The callbacks interface.
     */
    public static <T> T assertCallbacks(@NonNull Fragment fragment, @NonNull Context context, @NonNull Class<T> callbacksClass) {
        if (callbacksClass.isInstance(context)) {
            return callbacksClass.cast(context);
        } else {
            if (fragment.getParentFragment() != null && callbacksClass.isInstance(fragment.getParentFragment())) {
                return callbacksClass.cast(fragment.getParentFragment());
            } else {
                String contextClass = context.getClass().getSimpleName();
                String fragmentClass = fragment.getClass().getSimpleName();
                throw new RuntimeException(contextClass + " must implement " + fragmentClass + "." + callbacksClass.getSimpleName());
            }
        }
    }
}
