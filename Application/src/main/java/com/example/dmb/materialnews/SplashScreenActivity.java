package com.example.dmb.materialnews;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dmbteam.wordpress.fetcher.cmn.Category;
import com.dmbteam.wordpress.fetcher.cmn.Post;
import com.dmbteam.wordpress.fetcher.worker.WordpressFetcher;
import com.dmbteam.wordpress.fetcher.worker.WordpressFetcherable;
import com.example.dmb.data.MagazineAppDataHolder;

// TODO: Auto-generated Javadoc
/**
 * The Class SplashScreenActivity.
 */
public class SplashScreenActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create an object of type SplashHandler
		SplashHandler mHandler = new SplashHandler();
		// Set the layout for this activity
		setContentView(R.layout.splash);
		// Create a Message object
		Message msg = new Message();
		// Assign a unique code to the message.
		// Later, this code will be used to identify the message in Handler
		// class.
		msg.what = 0;

		WordpressFetcher.getInstance(this).fetchPostsForCategoryWithCallback(
				new SplashScreenFetcher(), null, 0);

		// Send the message with a delay of 3 seconds(3000 = 3 sec).
		mHandler.sendMessageDelayed(msg, 3000);
	}

	/**
	 * The Class SplashHandler.
	 */
	private class SplashHandler extends Handler {

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			// switch to identify the message by its code
			switch (msg.what) {
			default:
			case 0:
				super.handleMessage(msg);

				// Create an intent to start the new activity.
				// Next activity to start is MainActivity
				Intent intent = new Intent();
				intent.setClass(SplashScreenActivity.this, MainActivity.class);
				startActivity(intent);
				// Finish the current activity
				SplashScreenActivity.this.finish();
			}
		}
	}

	/**
	 * The Class SplashScreenFetcher.
	 */
	private class SplashScreenFetcher implements WordpressFetcherable {

		/* (non-Javadoc)
		 * @see com.dmbteam.wordpress.fetcher.worker.WordpressFetcherable#resultAllCategories(java.util.List)
		 */
		@Override
		public void resultAllCategories(List<Category> categories) {
		}

		/* (non-Javadoc)
		 * @see com.dmbteam.wordpress.fetcher.worker.WordpressFetcherable#resultAllPostsForCategory(java.util.List)
		 */
		@Override
		public void resultAllPostsForCategory(List<Post> posts) {
			Log.i("Magazine", "Posts was loaded with size " + posts.size());
			MagazineAppDataHolder.getInstance().addToCurrentPosts(posts);
		}

	}
}