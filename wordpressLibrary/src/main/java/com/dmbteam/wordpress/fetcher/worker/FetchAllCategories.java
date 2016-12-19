package com.dmbteam.wordpress.fetcher.worker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import com.dmbteam.wordpress.fetcher.cmn.Category;
import com.dmbteam.wordpress.fetcher.cmn.OfflineModelKey;
import com.dmbteam.wordpress.fetcher.util.CategoryIndexComparator;
import com.dmbteam.wordpress.fetcher.util.JsonUtil;
import com.dmbteam.wordpress.fetcher.util.PreferencesUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * This class is used to fetch all Categories from Wordpress. The operation is
 * done in <code>AsyncTask</code>
 * 
 * @author |dmb TEAM|
 * 
 */
public class FetchAllCategories extends
		AsyncTask<Void, Void, ArrayList<Category>> {

	private static final String TAG = null;

	/**
	 * Constant with URL to fetch all Wordpress categories
	 */
	public static String WORDPRESS_FETCH_ALL_CATEGORIES_URL = "%s?json=get_category_index";

	/**
	 * The <code>Callback</code> that is going to handle the fetched categories.
	 */
	private WordpressFetcherable mCallbackListener;

	private Context mContext;

	private String mKeyToSave;

	/**
	 * Class constructor
	 * 
	 * @param responceActivity
	 *            The <code>Activity</code> that is going to handle the fetched
	 *            categories.
	 */
	public FetchAllCategories(Context context,
			WordpressFetcherable callBackListener) {
		this.mContext = context;
		this.mCallbackListener = callBackListener;
	}

	/**
	 * Override this method to perform a computation on a background thread. The
	 * specified parameters are the parameters passed to execute(Params...) by
	 * the caller of this task. This method can call
	 * publishProgress(Progress...) to publish updates on the UI thread.
	 * 
	 * @param result
	 *            The result of the operation computed by
	 *            doInBackground(Params...).
	 * 
	 */
	@Override
	protected ArrayList<Category> doInBackground(Void... params) {

		ArrayList<Category> allCategories = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(
					WORDPRESS_FETCH_ALL_CATEGORIES_URL));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();

				PreferencesUtil.saveJsonStringWithKey(mContext, responseString,
						mKeyToSave);

				allCategories = parseAndReturnObjects(responseString);

			} else {
				// Closes the connection.
				response.getEntity().getContent().close();

			}
		} catch (Exception e) {

			String savedStringResponce = PreferencesUtil.getWithKey(mKeyToSave,
					mContext);

			try {
				allCategories = parseAndReturnObjects(savedStringResponce);
			} catch (Exception e1) {
				Log.i(TAG, "Throw Exception while parsing Category objects ");
			}

			Log.i(TAG, "Throw Exception while parsing Category objects ");
		}

		return allCategories;
	}

	/**
	 * Runs on the UI thread after doInBackground(Params...). The specified
	 * result is the value returned by doInBackground(Params...).
	 * 
	 * This method won't be invoked if the task was cancelled.
	 * 
	 * @param result
	 *            The result of the operation computed by
	 *            doInBackground(Params...).
	 */
	@Override
	protected void onPostExecute(ArrayList<Category> result) {
		super.onPostExecute(result);

		if (result != null && result.size() > 0) {
			Collections.sort(result, new CategoryIndexComparator());

			mCallbackListener.resultAllCategories(result);
		}
	}

	/**
	 * Runs on the UI thread before doInBackground(Params...).
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		WORDPRESS_FETCH_ALL_CATEGORIES_URL = String.format(
				WORDPRESS_FETCH_ALL_CATEGORIES_URL, WordpressFetcher
						.getInstance(mContext).getServerUrl());
		
		setUpOfflineKey();
	}

	private boolean isForbidden(Category category) {
		boolean result = false;
		if (WordpressFetcher.getInstance(mContext).getForbiddenCategories()
				.contains(category.getTitle())) {
			result = true;
		}
		return result;
	}

	private ArrayList<Category> parseAndReturnObjects(String responseString)
			throws Exception {
		JSONArray items = JsonUtil.parseJson(responseString).getJSONArray(
				"categories");

		ArrayList<Category> allCategories = new ArrayList<Category>();

		for (int i = 0; i < items.length(); i++) {
			Category category = new Category(items.getJSONObject(i));
			if (!isForbidden(category)) {
				allCategories.add(category);
			}
		}

		return allCategories;
	}

	private void setUpOfflineKey() {
		OfflineModelKey offlineKey = new OfflineModelKey(
				WORDPRESS_FETCH_ALL_CATEGORIES_URL);
		mKeyToSave = offlineKey.getUniqueKey();
	}
}