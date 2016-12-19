package com.example.dmb.materialnews;

import android.app.Application;


import com.dmbteam.wordpress.fetcher.worker.WordpressFetcher;
import com.example.dmb.settings.AppConstants;


/**
 * The Class MagazineAppContext.
 */
public class MagazineAppContext extends Application {

	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		// Init fetcher with your server URL
		WordpressFetcher.getInstance(this).setServerUrl(
				AppConstants.WORDPRESS_SERVER_URL);
	}
}
