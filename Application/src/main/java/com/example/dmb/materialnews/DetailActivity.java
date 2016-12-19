package com.example.dmb.materialnews;

import com.dmbteam.wordpress.fetcher.cmn.Post;
import com.dmbteam.wordpress.fetcher.util.DateUtil;
import com.example.dmb.fragment.DialogFragmentFont;
import com.example.dmb.util.ScaleUtil;
import com.example.dmb.util.URLImageParser;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.transition.Transition;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * The Class DetailActivity.
 */
public class DetailActivity extends BaseActivity implements ScrollViewMovable {

    // Extra name for the ID parameter
    public static final String EXTRA_PARAM_ID = "detail:_id";

    // View name of the header image. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

    // View name of the header title. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";

    private ImageView mHeaderImageView;
    private TextView mHeaderTitle;
    private TextView mContent;
    private TextView mDate;
    private FloatingActionsMenu actionsMenu;
    private FloatingActionButton button;
    private CustomScrollView scrollView;

    private Post mPost;

    // Keep reference to the ShareActionProvider from the menu
    private ShareActionProvider mShareActionProvider;

    @Override
    protected int getLayoutId() {
        return R.layout.details;
    }

    @Override
    protected void initViews() {
        // Retrieve the correct Item instance, using the ID provided in the Intent
        mPost = (Post) getIntent().getSerializableExtra(EXTRA_PARAM_ID);

        mHeaderImageView = (ImageView) findViewById(R.id.imageview_header);
        mHeaderTitle = (TextView) findViewById(R.id.textview_title);
        scrollView = (CustomScrollView) findViewById(R.id.scroll);

        scrollView.setMovable(this);
        mContent = (TextView) findViewById(R.id.textview_content);
        mDate = (TextView) findViewById(R.id.textview_date);
        actionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        button = (FloatingActionButton) findViewById(R.id.button);
        String action = Intent.ACTION_SEND;

        // Get list of handler apps that can send
        Intent intent = new Intent(action);
        intent.setType("text/plain");
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resInfos = pm.queryIntentActivities(intent, 0);

        int count = 0;
        for (ResolveInfo resInfo : resInfos) {
            String context = resInfo.activityInfo.packageName;
            String packageClassName = resInfo.activityInfo.name;
            CharSequence label = resInfo.loadLabel(pm);
            Drawable icon = resInfo.loadIcon(pm);
            if (packageClassName.contains("facebook")
                    || packageClassName.contains("twitter")
                    || packageClassName.contains("android.gm")
                    || packageClassName.contains("email")) {
                actionsMenu.addButton(getButton(label.toString(), icon, context,
                        packageClassName));
                count++;
            }
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionsMenu.toggle();
            }
        });

        if (count == 0) {
            actionsMenu.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
        } else {
            actionsMenu.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
        }






        /**
         * Set the name of the view's which will be transition to, using the static values above.
         * This could be done in the layout XML, but exposing it via static variables allows easy
         * querying from other Activities
         */
        ViewCompat.setTransitionName(mHeaderImageView, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(mHeaderTitle, VIEW_NAME_HEADER_TITLE);

        loadItem();
    }

    /**
     * This is called in response to an internal scroll in this view (i.e., the
     * view scrolled its own contents). This is typically as a result of
     * scrollBy(int, int) or scrollTo(int, int) having been called.
     *
     * @param currentHorizontal
     *            Current horizontal scroll origin.
     * @param currentVertical
     *            Current vertical scroll origin.
     * @param previourHorizontal
     *            Previous horizontal scroll origin.
     * @param previousVertical
     *            Previous vertical scroll origin.
     */
    @Override
    public void onScrollChanged(int currentHorizontal, int currentVertical,
                                int previourHorizontal, int previousVertical) {
        // We take the last son in the scrollview
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
        // Scroll down
        if (currentVertical > previousVertical && diff != 0) {
            actionsMenu.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
        } else {
            actionsMenu.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);;
        }

    }

    private FloatingActionButton getButton(String s, Drawable icon, final String context, final String packageClassName) {
        FloatingActionButton fb = new FloatingActionButton(this);
        fb.setIconDrawable(icon);
        fb.setColorNormal(button.getColorNormal());
        fb.setColorPressed(button.getColorPressed());
        fb.setColorDisabled(button.getColorDisabled());
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the selected activity sending it the URLs of the resized images
                Intent intent;
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setClassName(context, packageClassName);
                Post post = mPost;
                if (packageClassName.contains("twitter")) {
                    String message;
                    if (post.getTitle().length() > 70) {
                        message = post.getTitle().substring(0, 71) + "... Read more: " + post.getUrl();
                    } else {
                        message = post.getTitle()  + "... Read more: " + post.getUrl();
                    }
                    intent.putExtra(Intent.EXTRA_TEXT, message);
                } else if (packageClassName.contains("facebook")) {
                    intent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
                    intent.putExtra(Intent.EXTRA_TEXT, post.getUrl());
                } else {
                    intent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
                    intent.putExtra(Intent.EXTRA_TEXT, post.getContent());
                }
                actionsMenu.toggle();
                startActivity(intent);
            }
        });
        return fb;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        if (item.getItemId() == R.id.action_font) {
            showFontDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Loads a single item
     */
    private void loadItem() {
        // Set the title TextView to the item's name and author
        mHeaderTitle.setText(mPost.getTitle());
        URLImageParser p = new URLImageParser(mContent, this);
        mContent.setText(Html.fromHtml(
                mPost.getContentString().replace((char) 160, (char) 32)
                        .replace((char) 65532, (char) 32).trim(), p, null));
        mContent.setMovementMethod(LinkMovementMethod.getInstance());
        mDate.setText(DateUtil.postsDatePublishedFormatter1.format(mPost.getPublishedDate()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {
            // If we're running on Lollipop and we have added a listener to the shared element
            // transition, load the thumbnail. The listener will load the full-size image when
            // the transition is complete.
            loadThumbnail();
        } else {
            // If all other cases we should just load the full-size image now
            loadFullSizeImage();
        }
    }

    /**
     * Load the item's thumbnail image into our {@link ImageView}.
     */
    private void loadThumbnail() {

        if (mPost.getAttachments().size() > 0) {
            Picasso.with(mHeaderImageView.getContext())
                    .load(mPost.giveMeBiggestAttachment()).into(mHeaderImageView);
        } else {
            Picasso.with(mHeaderImageView.getContext())
                    .load(R.drawable.splash).into(mHeaderImageView);
        }
    }

    /**
     * Load the item's full-size image into our {@link ImageView}.
     */
    private void loadFullSizeImage() {

        if (mPost.getAttachments().size() > 0) {

            Picasso.with(mHeaderImageView.getContext())
                    .load(mPost.giveMeBiggestAttachment()).into(mHeaderImageView);
        } else {
            Picasso.with(mHeaderImageView.getContext())
                    .load(R.drawable.splash).into(mHeaderImageView);
        }

    }

    /**
     * Try and add a {@link Transition.TransitionListener} to the entering shared element
     * {@link Transition}. We do this so that we can load the full-size image after the transition
     * has completed.
     *
     * @return true if we were successful in adding a listener to the enter transition
     */
    private boolean addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();

        if (transition != null) {
            // There is an entering shared element transition so add a listener to it
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    // As the transition has ended, we can now load the full-size image
                    loadFullSizeImage();

                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    // No-op
                }
            });
            return true;
        }

        // If we reach here then we have not added a listener
        return false;
    }

    public void updateTextSize() {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        float size = prefs.getFloat(
                MainActivity.FONT_SIZE, 0);

        mContent.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                size + ScaleUtil.dipsToPixels(this, 10));
    }

    public void showFontDialog() {
        DialogFragmentFont fraFont = DialogFragmentFont.newInstance();

        fraFont.show(getSupportFragmentManager(), DialogFragmentFont.TAG);
    }

    /**
     * Inits the list view.
     */
    /*private void initListView() {
        String action = Intent.ACTION_SEND;

        // Get list of handler apps that can send
        Intent intent = new Intent(action);
        intent.setType("text/plain");
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resInfos = pm.queryIntentActivities(intent, 0);


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
                        Post post = ((MainActivity) getActivity()).getCurrentPost();
                        if (items.get(which).packageClassName.contains("twitter")) {
                            String message;
                            if (post.getTitle().length() > 70) {
                                message = post.getTitle().substring(0, 71) + "... Read more: " + post.getUrl();
                            } else {
                                message = post.getTitle()  + "... Read more: " + post.getUrl();
                            }
                            intent.putExtra(Intent.EXTRA_TEXT, message);
                        } else if (items.get(which).packageClassName.contains("facebook")) {
                            intent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
                            intent.putExtra(Intent.EXTRA_TEXT, post.getUrl());
                        } else {
                            intent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
                            intent.putExtra(Intent.EXTRA_TEXT, post.getContent());
                        }
                        startActivity(intent);
                    }
                });
                return v;
            }
        };

    }*/

}