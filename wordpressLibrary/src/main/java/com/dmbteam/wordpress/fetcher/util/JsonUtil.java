package com.dmbteam.wordpress.fetcher.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * This utility class contains all JSON operations
 * 
 * @author |dmb TEAM|
 *
 */
public class JsonUtil {
	
	/**
	 * Parse a server response into a JSON Object. This is a basic
	 * implementation using org.json.JSONObject representation.
	 * 
	 * @param response
	 *            - string representation of the response
	 * @return the response as a JSON Object
	 * @throws JSONException
	 *             - if the response is not valid JSON
	 * @throws ReadError
	 *             - if an error condition is set
	 */
	public static JSONObject parseJson(String response) throws JSONException {
		Log.e("THE RESPONSE:", response);
		Log.e("length", response.length() + "");
		Log.e("1.First character is:", response.charAt(0) + "<----- Is this");
		int lenght = response.length();
		if (response.charAt(0) != '{') {
			response = response.substring(1, lenght);
			Log.e("2.First character is :", response.charAt(0)
					+ "<----- Is this");
		}

		if (response.equals("true")) {
			response = "{value : true}";
		}
		JSONObject json = new JSONObject(response);

		return json;
	}
}
