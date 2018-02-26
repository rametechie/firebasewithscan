package com.firebase.demo;


import android.support.v4.app.Fragment;


public class DelegateFragment extends Fragment {



    public interface MultiWindowSafeFragmentLifecycle {
        void onFragmentStart();

        void onFragmentStop();
    }
}
