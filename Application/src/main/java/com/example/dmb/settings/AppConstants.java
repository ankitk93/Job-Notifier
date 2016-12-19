package com.example.dmb.settings;

import static com.example.dmb.settings.ThemeEnum.*;

public class AppConstants {

	/**
	 * URL of your WordPress server
	 */
	public static final String WORDPRESS_SERVER_URL = "http://www.dmb-team.com/";

	/**
	 * Themes of the app. Default is BLUE. You can change it to GREEN, PINK,
	 * DARK, RED and ORANGE
	 * 
	 * See the values of the theme in ThemesEnum.java located in the current
	 * package
	 */
	public static final ThemeEnum APP_THEME = GREEN;
	
	/**
	 * Enable or disable admob ads.
	 */
	public static boolean isAdmobEnabled = true;

	/**
	 * Sliding menu, the title at the top; About page title.
	 */
	public static final String USER_BLOG_TITLE = "Material News";

	/**
	 * Sliding menu, the website at the top; About page website.
	 */
	public static final String USER_WEBSITE_URL = "http://www.dmb-team.com";

	/**
	 * Sliding menu, the mail at the top; About page mail.
	 */
	public static final String USER_MAIL = "support@dmb-team.com";

	/**
	 * About page phone
	 */
	public static final String USER_PHONE = "+44 888 55 42 42";
	
	/**
	 * Link for facebook icon in the about page
	 */
	public static final String FACEBOOK_LINK = "https://www.facebook.com/dmbteam?ref=bookmarks";
	
	/**
	 * Link for twitter icon in the about page
	 */
	public static final String TWITTER_LINK = "https://twitter.com/";
}
