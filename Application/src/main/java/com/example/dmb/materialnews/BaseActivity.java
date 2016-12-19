package com.example.dmb.materialnews;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.dmb.settings.AppConstants;
import com.example.dmb.util.ThemesManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Base class for all activities in the project
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected Toolbar mToolbar;
    protected AdView mAdmobView;

    /* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemesManager.setCorrectTheme(this);
        setContentView(getLayoutId());
        initToolbar();
        initActionBarSettings();
        initViews();
        initAdmob();
    }

    /**
     * Initializes the toolbar
     */
    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    protected void initAdmob() {
        mAdmobView = (AdView) findViewById(R.id.home_admob);
        if (AppConstants.isAdmobEnabled) {
            mAdmobView.setVisibility(View.VISIBLE);
            AdRequest.Builder builder =  new AdRequest.Builder();
            AdRequest adRequest = builder.build();

            // Start loading the ad in the background.
            mAdmobView.loadAd(adRequest);

        } else {
            mAdmobView.setVisibility(View.GONE);
        }
    }

    /**
     * Initializes the settings of the actionbar
     */
    protected void initActionBarSettings() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    /**
     * The resource ID of the layout for the content
     */
    protected abstract int getLayoutId();

    /**
     * Initializes all views in the activity
     */
    protected abstract void initViews();

}
