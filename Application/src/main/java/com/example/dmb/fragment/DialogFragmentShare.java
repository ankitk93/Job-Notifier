package com.example.dmb.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dmbteam.wordpress.fetcher.cmn.Post;
import com.example.dmb.materialnews.R;

/**
 * The Class DialogFragmentShare.
 */
@Deprecated
public class DialogFragmentShare extends DialogFragment {

	/** The Constant TAG. */
	public static final String TAG = DialogFragmentShare.class.getSimpleName();

	/** The parent view. */
	private View parentView;
	
	/** The List view. */
	private ListView mListView;

    private Post mPost;

	/**
	 * New instance.
	 *
	 * @return the dialog fragment share
	 */
	public static DialogFragmentShare newInstance(Post post) {
		DialogFragmentShare fragment = new DialogFragmentShare();
        fragment.mPost = post;
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

		parentView = inflater
				.inflate(R.layout.fragment_share, container, false);
		
		mListView = (ListView) parentView.findViewById(R.id.fra_share_list);

		initListView();

		return parentView;

	}

	/**
	 * Inits the list view.
	 */
	private void initListView() {
		String action = Intent.ACTION_SEND;

		// Get list of handler apps that can send
		Intent intent = new Intent(action);
		intent.setType("text/plain");
		PackageManager pm = getActivity().getPackageManager();
		List<ResolveInfo> resInfos = pm.queryIntentActivities(intent, 0);

		// Form those activities into an array for the list adapter
		final List<ListItem> items = new ArrayList<ListItem>();
		for (ResolveInfo resInfo : resInfos) {
			String context = resInfo.activityInfo.packageName;
			String packageClassName = resInfo.activityInfo.name;
			CharSequence label = resInfo.loadLabel(pm);
			Drawable icon = resInfo.loadIcon(pm);
			if (packageClassName.contains("facebook")
					|| packageClassName.contains("twitter")
					|| packageClassName.contains("android.gm")
					|| packageClassName.contains("email")) {
				items.add(new ListItem(label.toString(), icon, context,
						packageClassName));
			}
		}
		ArrayAdapter<ListItem> adapter = new ArrayAdapter<ListItem>(
				getActivity(), android.R.layout.select_dialog_item,
				android.R.id.text1, items) {

			public View getView(int position, View convertView, ViewGroup parent) {
				final int which = position;
				// User super class to create the View
				View v = super.getView(position, convertView, parent);
				TextView tv = (TextView) v.findViewById(android.R.id.text1);

				// Put the icon drawable on the TextView (support various screen
				// densities)
				int dpS = (int) (32 * getResources().getDisplayMetrics().density * 0.5f);
				items.get(position).icon.setBounds(0, 0, dpS, dpS);
				tv.setCompoundDrawables(items.get(position).icon, null, null, null);

				// Add margin between image and name (support various screen
				// densities)
				int dp5 = (int) (5 * getResources().getDisplayMetrics().density * 0.5f);
				tv.setCompoundDrawablePadding(dp5);
				v.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// Start the selected activity sending it the URLs of the resized images
					    Intent intent;
					    intent = new Intent(Intent.ACTION_SEND);
					    intent.setType("text/plain");
					    intent.setClassName(items.get(which).context, items.get(which).packageClassName);

					    if (items.get(which).packageClassName.contains("twitter")) {
					    	String message;
					    	if (mPost.getTitle().length() > 70) {
				            	message = mPost.getTitle().substring(0, 71) + "... Read more: " + mPost.getUrl();
				            } else {
				            	message = mPost.getTitle()  + "... Read more: " + mPost.getUrl();
				            }
							intent.putExtra(Intent.EXTRA_TEXT, message);
					    } else if (items.get(which).packageClassName.contains("facebook")) {
					    	intent.putExtra(Intent.EXTRA_SUBJECT, mPost.getTitle());
							intent.putExtra(Intent.EXTRA_TEXT, mPost.getUrl());
					    } else {
					    	intent.putExtra(Intent.EXTRA_SUBJECT, mPost.getTitle());
							intent.putExtra(Intent.EXTRA_TEXT, mPost.getContent());
					    }
						startActivity(intent);
					}
				});
				return v;
			}
		};
		
		mListView.setAdapter(adapter);
	}

	// Class for a singular activity item on the list of apps to send to
	/**
	 * The Class ListItem.
	 */
	class ListItem {
		
		/** The name. */
		public final String name;
		
		/** The icon. */
		public final Drawable icon;
		
		/** The context. */
		public final String context;
		
		/** The package class name. */
		public final String packageClassName;

		/**
		 * Instantiates a new list item.
		 *
		 * @param text the text
		 * @param icon the icon
		 * @param context the context
		 * @param packageClassName the package class name
		 */
		public ListItem(String text, Drawable icon, String context,
				String packageClassName) {
			this.name = text;
			this.icon = icon;
			this.context = context;
			this.packageClassName = packageClassName;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return name;
		}
	}

}
