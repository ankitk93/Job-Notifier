package com.dmbteam.wordpress.fetcher.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * The Class PreferencesUtil.
 */
public class PreferencesUtil {
	/* Application preference parameters */
	/** The Constant PREFERENCES_NAME. */
	public static final String PREFERENCES_NAME = "magazine.app.preferences";

	/**
	 * Gets the shared preferences.
	 *
	 * @param context the context
	 * @return the shared preferences
	 */
	public static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	/**
	 * Save json string for category.
	 *
	 * @param context the context
	 * @param jsontResponse the jsont response
	 * @param categoryEnum the category enum
	 */
	public static void saveJsonStringWithKey(Context context,
			String jsontResponse, String key) {
		SharedPreferences prefs = getSharedPreferences(context);

		saveWithKeyAndValue(key, jsontResponse,
				prefs.edit());
	}
	
//	/**
//	 * Save json string categories.
//	 *
//	 * @param context the context
//	 * @param jsontResponse the jsont response
//	 * @param categoriesJson the categories json
//	 */
//	public static void saveJsonStringCategories(Context context,
//			String jsontResponse, String categoriesJson) {
//		SharedPreferences prefs = getSharedPreferences(context);
//
//		saveWithKeyAndValue(categoriesJson, jsontResponse,
//				prefs.edit());
//	}


	/**
	 * Save with key and value.
	 *
	 * @param key the key
	 * @param value the value
	 * @param editor the editor
	 */
	private static void saveWithKeyAndValue(String key, String value,
			Editor editor) {
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * Gets the with key.
	 *
	 * @param key the key
	 * @param context the context
	 * @return the with key
	 */
	public static String getWithKey(String key, Context context) {
		SharedPreferences prefs = getSharedPreferences(context);

		return prefs.getString(key, null);
	}
}
