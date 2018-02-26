/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.firebase.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.demo.fragments.OrderListFragment;
import com.firebase.demo.fragments.ProductListFragment;
import com.firebase.demo.model.Product;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends BaseActivity implements ProductListFragment.OnActionListener{

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

   
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                value = extras.getString("productkey");
            }

        
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    //    mDrawerList = (ListView) findViewById(R.id.left_drawer);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
//        // ActionBarDrawerToggle ties together the the proper interactions
//        // between the sliding drawer and the action bar app icon
//        mDrawerToggle = new ActionBarDrawerToggle(
//                this,                  /* host Activity */
//                mDrawerLayout,         /* DrawerLayout object */
//                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
//                R.string.drawer_open,  /* "open drawer" description for accessibility */
//                R.string.drawer_close  /* "close drawer" description for accessibility */
//        ) {
//            public void onDrawerClosed(View view) {
//                getActionBar().setTitle(mTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//
//            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle(mDrawerTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        loadBillingFragment(value);
    }

    void loadBillingFragment(String value) {
        // Instantiate a new fragment.
        int id = 1;
        Bundle bundle = new Bundle();
        bundle.putString("productid", value);
        ProductListFragment productlistFragment = new ProductListFragment();
        productlistFragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_fl__fragments, productlistFragment);
        ft.commitAllowingStateLoss();

    }


    @Override
    public int onAddedProduct(Product productAdded) {
        return 0;
    }

    @Override
    public int onRemovedProduct(Product productRemoved) {
        return 0;
    }

    @Override
    public void openCart() {
        Log.d("openCart","opencart");
        OrderListFragment orderlistFragment = new OrderListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_fl__fragments, orderlistFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void openScan() {
        Intent intent = new Intent(MainActivity.this, ShoppingActivity.class);

        startActivity(intent);

    }
}
