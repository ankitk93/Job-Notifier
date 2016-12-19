package com.example.dmb.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.example.dmb.materialnews.R;


/**
 * The Class DialogFragmentLoading.
 */
public class DialogFragmentLoading extends DialogFragment {

	/** The Constant TAG. */
	public static final String TAG = DialogFragmentLoading.class
			.getSimpleName();


    private ImageView img;

    private ImageView imgEnd;
	/**
	 * New instance.
	 *
	 * @return the dialog fragment loading
	 */
	public static DialogFragmentLoading newInstance() {
		DialogFragmentLoading fragment = new DialogFragmentLoading();

		return fragment;
	}

	/** The parent view. */
	private View parentView;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);

		parentView = inflater.inflate(R.layout.dialog_fragment_loading,
				container, false);

        // Load the ImageView that will host the animation and
        // set its background to our AnimationDrawable XML resource.
        img = (ImageView)parentView.findViewById(R.id.loading);
        img.setBackgroundResource(R.drawable.loading_animation);

        imgEnd = (ImageView)parentView.findViewById(R.id.loadingEnd);
        imgEnd.setBackgroundResource(R.drawable.loading_animation_end);
        imgEnd.setVisibility(View.INVISIBLE);

        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

        // Start the animation (looped playback by default).
        frameAnimation.start();


        return parentView;
	}

    public void endAnimation() {
        Handler mAnimationHandler;
        imgEnd.setVisibility(View.VISIBLE);
        img.setVisibility(View.INVISIBLE);
        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimationEnd = (AnimationDrawable) imgEnd.getBackground();
        frameAnimationEnd.setOneShot(true);
        int duration  = getTotalDuration(frameAnimationEnd);
        mAnimationHandler = new Handler();
        mAnimationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, duration + 200);
        frameAnimationEnd.start();

    }

    public int getTotalDuration(AnimationDrawable drawable) {

        int iDuration = 0;

        for (int i = 0; i < drawable.getNumberOfFrames(); i++) {
            iDuration += drawable.getDuration(i);
        }

        return iDuration;
    }

}
