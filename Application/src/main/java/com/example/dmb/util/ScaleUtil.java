package com.example.dmb.util;

import android.content.Context;
import android.util.TypedValue;


/**
 * The Class ScaleUtil.
 */
public class ScaleUtil {
	
	/** The Constant INITIAL_SIZE_TEXT. */
	public static final int INITIAL_SIZE_TEXT = 6;


	/**
	 * Dips to pixels.
	 *
	 * @param context the context
	 * @param dips the dips
	 * @return the int
	 */
	public static int dipsToPixels(Context context, float dips) {
		if (context != null) {
			return Math.round(TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, dips, context.getResources()
							.getDisplayMetrics()));
		} else {
			return Math.round(dips);
		}
	}
	
	/**
	 * Gets the scaled initial text size.
	 *
	 * @param c the c
	 * @return the scaled initial text size
	 */
	public static int getScaledInitialTextSize(Context c){
		return dipsToPixels(c, INITIAL_SIZE_TEXT);
	}
	
}
