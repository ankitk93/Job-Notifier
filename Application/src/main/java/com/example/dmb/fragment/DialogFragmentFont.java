package com.example.dmb.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.dmb.materialnews.DetailActivity;
import com.example.dmb.materialnews.MainActivity;
import com.example.dmb.materialnews.R;
import com.example.dmb.util.ScaleUtil;


/**
 * The Class DialogFragmentFont.
 */
public class DialogFragmentFont extends DialogFragment {

	/** The Constant TAG. */
	public static final String TAG = DialogFragmentFont.class.getSimpleName();

	/** The parent view. */
	private View parentView;

	/** The Seek bar. */
	private SeekBar mSeekBar;

	/** The Text content. */
	private TextView mTextContent;

	/** The prefs. */
	private SharedPreferences prefs;

	/** The Btn done. */
	private TextView mBtnDone;

	/**
	 * New instance.
	 *
	 * @return the dialog fragment font
	 */
	public static DialogFragmentFont newInstance() {
		DialogFragmentFont fragment = new DialogFragmentFont();

		return fragment;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);

		parentView = inflater.inflate(R.layout.fragment_font, container, false);

		mSeekBar = (SeekBar) parentView.findViewById(R.id.fra_font_seekbark);

		prefs = getActivity().getPreferences(Context.MODE_PRIVATE);

		mTextContent = (TextView) parentView
				.findViewById(R.id.fra_font_example_text);

		float size = prefs.getFloat(MainActivity.FONT_SIZE, 0);

		mTextContent.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				size + ScaleUtil.dipsToPixels(getActivity(), 10));

		Log.i("Magazine_Text", "DialogFragmetn onCreateView size = " + size);

		mBtnDone = (TextView) parentView.findViewById(R.id.fra_font_done_btn);

		initSeekBarListener();

		initBtnDoneListener();

		return parentView;

	}

	/**
	 * Inits the btn done listener.
	 */
	private void initBtnDoneListener() {
		mBtnDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();

				((DetailActivity) getActivity()).updateTextSize();
			}
		});
	}

	/**
	 * Inits the seek bar listener.
	 */
	private void initSeekBarListener() {

		float fs = prefs.getFloat(
				MainActivity.FONT_SIZE, 0);

		mSeekBar.setMax(ScaleUtil.dipsToPixels(getActivity(), 12));
		mSeekBar.setProgress((int) fs);
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
				SharedPreferences.Editor ed = prefs.edit();

				ed.putFloat(MainActivity.FONT_SIZE, mSeekBar.getProgress());

				ed.commit();
				Log.i("Magazine_Text",
						"comited size size = " + mSeekBar.getProgress());
				Log.i("Magazine_Text", "comited size size progress = "
						+ progress);

				if (mTextContent != null) {

					float size = prefs.getFloat(MainActivity.FONT_SIZE, 0);

					mTextContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, size
							+ ScaleUtil.dipsToPixels(getActivity(), 10));

					Log.i("Magazine_Text", "onProgressChanged size = " + size);
				}
			}
		});
	}
}
