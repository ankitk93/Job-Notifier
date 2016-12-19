package com.example.dmb.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.widget.TextView;

import com.example.dmb.materialnews.R;
import com.example.dmb.cache.CacheLoader;


/**
 * The Class URLImageParser.
 */
public class URLImageParser implements ImageGetter {
    
    /** The c. */
    Context c;
    
    /** The container. */
    TextView container;

    /**
     * Instantiates a new URL image parser.
     *
     * @param t the t
     * @param c the c
     */
    public URLImageParser(TextView t, Context c) {
        this.c = c;
        this.container = t;
    }

    /* (non-Javadoc)
     * @see android.text.Html.ImageGetter#getDrawable(java.lang.String)
     */
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = c.getResources().getDrawable(R.drawable.ic_launcher);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage().execute(source.replace("-150x150", ""), d);

        return d;
    }

    /**
     * The Class LoadImage.
     */
    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        /** The Drawable. */
        private LevelListDrawable mDrawable;

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            try {
            	Bitmap cacheBitmap = CacheLoader.getInstance().getBitmapFromMemCache(source);
            	if (cacheBitmap != null) {
            		return cacheBitmap;
            	} else {
            		InputStream is = new URL(source).openStream();
            		Bitmap b = BitmapFactory.decodeStream(is);
            		CacheLoader.getInstance().addBitmapToMemoryCache(source, b);
                    return b;
            	}
                
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
            	int screenWidth = ((Activity) c).getWindowManager().getDefaultDisplay().getWidth() - 100; 
            	Double newHeight = screenWidth/1.77;
            	if (bitmap.getHeight() > bitmap.getWidth()) {
            		bitmap = Bitmap.createScaledBitmap(bitmap,
                			newHeight.intValue(), screenWidth, false);
            	} else {
            		bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth,
                			newHeight.intValue(), false);
            	}
            	
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                CharSequence text = container.getText();
                container.setText(text);
            }
        }
    }
}
