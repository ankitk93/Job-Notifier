package com.example.dmb.adapter;

import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.dmbteam.wordpress.fetcher.cmn.Category;
import com.example.dmb.materialnews.MainActivity;
import com.example.dmb.materialnews.R;

/**
 * The Class AdapterSlidingMenu.
 */
public class AdapterSlidingMenu extends ArrayAdapter<Category> {

    private MainActivity context;

	/**
	 * Instantiates a new adapter sliding menu.
	 *
	 * @param context the context
	 * @param cagerories the cagerories
	 */
	public AdapterSlidingMenu(MainActivity context, List<Category> cagerories) {
		super(context, 0, cagerories);
        this.context = context;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = getInflater().inflate(R.layout.list_item_category,
					null);
		}

		if (position == 0) {
			convertView.findViewById(R.id.list_item_category_divider)
					.setVisibility(View.VISIBLE);
		} else {
			convertView.findViewById(R.id.list_item_category_divider)
					.setVisibility(View.GONE);
		}

		TextView categoryText = (TextView) convertView
				.findViewById(R.id.list_item_category_text);
		categoryText.setText(getItem(position).getTitle());

		convertView.setTag(getItem(position));
        RippleView rv = (RippleView) convertView.findViewById(R.id.more);
        rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = position;
                RippleHandler handler = new RippleHandler();
                handler.sendMessageDelayed(message, 1000);

            }
        });

		return convertView;
	}

	/**
	 * Gets the inflater.
	 *
	 * @return the inflater
	 */
	private LayoutInflater getInflater() {
		return LayoutInflater.from(getContext());
	}

    /**
     * The Class SplashHandler.
     */
    private class RippleHandler extends Handler {

        /* (non-Javadoc)
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {

                    super.handleMessage(msg);
                    context.openMenuItem(getItem(msg.what));


        }
    }
}
