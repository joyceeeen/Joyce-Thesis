package com.thesis.volunteam2;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


/**
 * Created by Jeysown on 7/21/2016.
 */

public class OrganizationProfile_Activity extends AppCompatActivity{

    NestedScrollView nestedScrollView;
    private CoordinatorLayout mCoordinator;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details_holder);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Event Details");

        nestedScrollView = (NestedScrollView)findViewById(R.id.nested_content_main);
        View content = getLayoutInflater().inflate(R.layout.mod_event_details,null);
        nestedScrollView.addView(content);

    }

}
