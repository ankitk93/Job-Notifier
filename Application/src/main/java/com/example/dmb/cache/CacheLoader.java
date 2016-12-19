package com.example.dmb.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * The Class CacheLoader.
 */
public class CacheLoader {

	/** The Instance. */
	private static CacheLoader mInstance;

	/**
	 * Gets the single instance of CacheLoader.
	 *
	 * @return single instance of CacheLoader
	 */
	public static CacheLoader getInstance() {
		if (mInstance == null) {
			synchronized (CacheLoader.class) {
				if (mInstance == null) {
					mInstance = new CacheLoader();
				}
			}
		}
		return mInstance;
	}

	/** The Memory cache. */
	private LruCache<String, Bitmap> mMemoryCache;

	/**
	 * Instantiates a new cache loader.
	 */
	private CacheLoader() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory/8;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize);
	}

	/**
	 * Adds the bitmap to memory cache.
	 *
	 * @param key the key
	 * @param bitmap the bitmap
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		
		getInstance().mMemoryCache.evictionCount();
		getInstance().mMemoryCache.size();
		
		if (getBitmapFromMemCache(key) == null) {
			getInstance().mMemoryCache.put(key, bitmap);
		}
	}

	/**
	 * Gets the bitmap from mem cache.
	 *
	 * @param key the key
	 * @return the bitmap from mem cache
	 */
	public Bitmap getBitmapFromMemCache(String key) {

		getInstance().mMemoryCache.evictionCount();
		getInstance().mMemoryCache.size();

		
		if(getInstance().mMemoryCache.get(key) != null){
			System.out.println("");
		}
		
		return getInstance().mMemoryCache.get(key);
	}
}