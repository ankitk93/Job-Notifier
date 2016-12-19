package com.dmbteam.wordpress.fetcher.worker;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dmbteam.wordpress.fetcher.cmn.Category;
import com.dmbteam.wordpress.fetcher.cmn.OfflineModelKey;
import com.dmbteam.wordpress.fetcher.cmn.Post;
import com.dmbteam.wordpress.fetcher.util.JsonUtil;
import com.dmbteam.wordpress.fetcher.util.PreferencesUtil;

/**
 * This class is used to fetch the recent posts from Wordpress by given
 * category. The operation is done in <code>AsyncTask</code>
 * 
 * @author |dmb TEAM|
 * 
 */
public class FetchRecentPostsByCategory extends
		AsyncTask<Void, Void, ArrayList<Post>> {

	private static final String TAG = FetchRecentPostsByCategory.class
			.getSimpleName();

	/**
	 * URL to fetch Wordpress recent posts by given category
	 */
	private String WORDPRESS_FETCH_POSTS_BY_CAT_URL = "%s?json=get_category_posts&category_id=%s&count=%s&page=%s";

	/**
	 * URL to fetch Wordpress recent posts by given category
	 */
	private String WORDPRESS_FETCH_RECENT_POSTS_URL = "%s?json=get_recent_posts&count=%s&page=%s";

	private WordpressFetcherable mCallbackListener;

	/**
	 * Category of the recent posts that are going to be fetched
	 */
	private Category mCategory;

	private int mPage;

	private Context mContext;

	private String mKeyToSave;

	/**
	 * Class constructor
	 * 
	 * @param responceActivity
	 *            The <code>Activity</code> that is going to handle the fetched
	 *            posts.
	 * @param category
	 *            Category of the recent posts that are going to be fetched
	 */
	public FetchRecentPostsByCategory(Context context,
			WordpressFetcherable callbackListener, Category category, int page) {
		this.mContext = context;
		this.mCallbackListener = callbackListener;
		this.mCategory = category;
		this.mPage = page;
	}

	/**
	 * Runs on the UI thread before doInBackground(Params...).
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		String page = "";

		try {
			page = URLEncoder.encode(mPage + "", "utf-8") + "";
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (mCategory != null) {

			String categoryId = "";

			try {
				categoryId = URLEncoder.encode(mCategory.getId() + "", "utf-8")
						+ "";
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			WORDPRESS_FETCH_POSTS_BY_CAT_URL = String
					.format(WORDPRESS_FETCH_POSTS_BY_CAT_URL, WordpressFetcher
							.getInstance(mContext).getServerUrl(), categoryId,
							WordpressFetcher.getInstance(mContext)
									.getPostCountResult(), page);
		} else {
			WORDPRESS_FETCH_POSTS_BY_CAT_URL = String
					.format(WORDPRESS_FETCH_RECENT_POSTS_URL, WordpressFetcher
							.getInstance(mContext).getServerUrl(),
							WordpressFetcher.getInstance(mContext)
									.getPostCountResult(), page);
		}

		setUpOfflineKey();
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
	protected ArrayList<Post> doInBackground(Void... params) {

		ArrayList<Post> recentPosts = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(
					WORDPRESS_FETCH_POSTS_BY_CAT_URL));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();

				String responseString = out.toString();

				PreferencesUtil.saveJsonStringWithKey(mContext, responseString,
						mKeyToSave);

				recentPosts = parseAndReturnObjects(responseString);
			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new Exception();

			}
		} catch (Exception e) {

			String savedStringResponce = PreferencesUtil.getWithKey(mKeyToSave,
					mContext);

			try {
				recentPosts = parseAndReturnObjects(savedStringResponce);
			} catch (Exception e1) {
				Log.i(TAG, "Throw Exception while parsing Post objects ");
			}

			Log.i(TAG, "Throw Exception while parsing Post objects ");
		}

		return recentPosts;
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
	protected void onPostExecute(ArrayList<Post> result) {
		super.onPostExecute(result);

		if (result == null) {
			result = new ArrayList<Post>();
		}

		mCallbackListener.resultAllPostsForCategory(result);
	}

	private ArrayList<Post> parseAndReturnObjects(String responseString)
			throws Exception {

		ArrayList<Post> recentPosts = new ArrayList<Post>();

		JSONObject jObject = JsonUtil.parseJson(responseString);
		JSONArray items = jObject.getJSONArray("posts");

		for (int i = 0; i < items.length(); i++) {
			if (mCategory != null) {
				recentPosts.add(new Post(items.getJSONObject(i), mCategory));
			} else {
				Post post = new Post(items.getJSONObject(i));
				post.setRecentPostsCounter(jObject.getInt("count_total"));
				recentPosts.add(post);
			}
		}

		return recentPosts;
	}

	private void setUpOfflineKey() {
		OfflineModelKey offlineKey = new OfflineModelKey(
				WORDPRESS_FETCH_POSTS_BY_CAT_URL, mCategory == null ? ""
						: mCategory.getTitle(), mPage);
		mKeyToSave = offlineKey.getUniqueKey();
	}
}