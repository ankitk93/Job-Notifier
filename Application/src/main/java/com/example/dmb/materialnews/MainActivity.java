package com.example.dmb.materialnews;

import com.dmbteam.wordpress.fetcher.cmn.Category;
import com.dmbteam.wordpress.fetcher.cmn.Post;
import com.dmbteam.wordpress.fetcher.worker.WordpressFetcher;
import com.example.dmb.adapter.AdapterSlidingMenu;
import com.example.dmb.callback.AllCategoriesCallback;
import com.example.dmb.data.MagazineAppDataHolder;
import com.example.dmb.fragment.DialogFragmentLoading;
import com.example.dmb.settings.AppConstants;
import com.example.dmb.util.ScaleUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * The Class MainActivity
 */
public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private GridView mGridView;
    private GridAdapter mAdapter;

    /** The is loading more. */
    private boolean isLoadingMore;

    /** The Constant FONT_SIZE. */
    public static final String FONT_SIZE = "fontsize";

    /** The Categoty list view. */
    private ListView mCategotyListView;

    /** The Drawer toggle. */
    private ActionBarDrawerToggle mDrawerToggle;

    /** The Left drawer. */
    private DrawerLayout mLeftDrawer;

    /** The Sliding about container. */
    private View mSlidingAboutContainer;

    /** The currently loaded category. */
    private Category currentlyLoadedCategory;

    /** The Wordpress fetcher call back. */
    private AllCategoriesCallback mWordpressFetcherCallBack;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        setUpActionbarSlidingMenu();

        giveMeAllCategories();

        initCategoryViews();

        initTextSize();

        // Setup the GridView and set the adapter
        mGridView = (GridView) findViewById(R.id.grid);
        mGridView.setOnItemClickListener(this);
        mAdapter = new GridAdapter();
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Called when an item in the {@link android.widget.GridView} is clicked. Here will launch the
     * {@link DetailActivity}, using the Scene Transition animation functionality.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Post post = (Post) adapterView.getItemAtPosition(position);

        // Construct an Intent as normal
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_PARAM_ID, post);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            // BEGIN_INCLUDE(start_activity)
            /**
             * Now create an {@link android.app.ActivityOptions} instance using the
             * {@link ActivityOptionsCompat#makeSceneTransitionAnimation(Activity, Pair[])} factory
             * method.
             */
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,

                    // Now we provide a list of Pair items which contain the view we can transitioning
                    // from, and the name of the view it is transitioning to, in the launched activity
                    new Pair<View, String>(view.findViewById(R.id.imageview_item),
                            DetailActivity.VIEW_NAME_HEADER_IMAGE),
                    new Pair<View, String>(view.findViewById(R.id.textview_name),
                            DetailActivity.VIEW_NAME_HEADER_TITLE));

            // Now we can start the Activity, providing the activity options as a bundle
            ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
            // END_INCLUDE(start_activity)
        } else {
            startActivity(intent);
        }

    }

    /**
     * {@link android.widget.BaseAdapter} which displays items.
     */
    private class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return MagazineAppDataHolder.getInstance()
                    .getAllPosts().size();
        }

        @Override
        public Post getItem(int position) {
            return MagazineAppDataHolder.getInstance()
                    .getAllPosts().get(position);
        }

        @Override
        public long getItemId(int position) {
            return Integer.parseInt(getItem(position).getId());
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.grid_item, viewGroup, false);
            }

            final Post item = getItem(position);

            if (getCount() >= 10 && getCount() % 10 == 0 && (position == (getCount() - 1)) && !isLoadingMore) {
                isLoadingMore = true;

                loadMore(getCount() / 10 + 1);

                Log.i("Magazine_Load_More", "" + (getCount() / 10 + 1));
            }

            ImageView image = (ImageView) view.findViewById(R.id.imageview_item);

            // Load the thumbnail image
            if (item.getAttachments().size() > 0) {
                Picasso.with(image.getContext()).load(item.giveMeBiggestAttachment()).into(image);
            } else {
                Picasso.with(image.getContext()).load(R.drawable.splash).into(image);
            }

            // Set the TextView's contents
            TextView name = (TextView) view.findViewById(R.id.textview_name);
            name.setText(item.getTitle());

            return view;
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPostCreate(android.os.Bundle)
     */
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_about) {
            openAboutScreen();
            return true;
        }

        if (item.getItemId() == R.id.action_rate) {
            openMarket();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Open market.
     */
    private void openMarket() {
        Uri uri = Uri.parse("market://details?id="
                + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.cannot_launch_market,
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Perform sliding menu clicked.
     *
     * @param category the category
     * @param showWithTransition the show with transition
     * @param page the page
     */
    public void performSlidingMenuClicked(Category category,
                                          boolean showWithTransition, int page) {

        currentlyLoadedCategory = category;

        if (mWordpressFetcherCallBack == null) {
            mWordpressFetcherCallBack = new AllCategoriesCallback(
                    MainActivity.this, showWithTransition);
        }

        WordpressFetcher.getInstance(this).fetchPostsForCategoryWithCallback(
                mWordpressFetcherCallBack, category, page);

        closeLeftSlidingMenu();
    }

    /**
     * Sets the up category list view.
     *
     * @param categories the new up category list view
     */
    public void setUpCategoryListView(List<Category> categories) {
        AdapterSlidingMenu adapter = new AdapterSlidingMenu(this, categories);

        mCategotyListView.setAdapter(adapter);

        mCategotyListView.setClickable(true);
    }

    /**
     * Re-fresh the grid
     */
    public void refreshGrid() {

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

        isLoadingMore = false;

    }

    /**
     * Gets the user web url.
     *
     * @return the user web url
     */
    public String getUserWebUrl() {
        return AppConstants.USER_WEBSITE_URL;
    }

    /**
     * Gets the user mail.
     *
     * @return the user mail
     */
    public String getUserMail() {
        return AppConstants.USER_MAIL;
    }

    /**
     * Gets the user blog title.
     *
     * @return the user blog title
     */
    public String getUserBlogTitle() {
        return AppConstants.USER_BLOG_TITLE;
    }

    /**
     * Load more.
     *
     * @param page the page
     */
    public void loadMore(int page) {
        if (mWordpressFetcherCallBack == null) {
            mWordpressFetcherCallBack = new AllCategoriesCallback(
                    MainActivity.this, false);

        }

        mWordpressFetcherCallBack.setIsLoadMore(true);

        performSlidingMenuClicked(currentlyLoadedCategory, false, page);
    }

    /**
     * Open about screen.
     */
    public void openAboutScreen() {

        closeLeftSlidingMenu();

        // Construct an Intent as normal
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);

    }

    /**
     * Show loading dialog.
     */
    public void showLoadingDialog() {

        DialogFragmentLoading fragment = DialogFragmentLoading.newInstance();

        fragment.show(getSupportFragmentManager(), DialogFragmentLoading.TAG);
    }

    /**
     * Hide loading dialog.
     */
    public void hideLoadingDialog() {
        Fragment dialog = getSupportFragmentManager().findFragmentByTag(
                DialogFragmentLoading.TAG);

        if (dialog != null) {
            ((DialogFragmentLoading) dialog).endAnimation();
        }
    }

    /**
     * Inits the text size.
     */
    private void initTextSize() {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

        float size = prefs.getFloat(FONT_SIZE, 0);

        if (size == 0) {
            SharedPreferences.Editor ed = prefs.edit();
            ed.putFloat(FONT_SIZE,
                    ScaleUtil.getScaledInitialTextSize(this));
            ed.commit();

        }
    }

    /**
     * Sets the up actionbar sliding menu.
     */
    private void setUpActionbarSlidingMenu() {

        mLeftDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, ((DrawerLayout)findViewById(R.id.drawer_layout)),
                mToolbar, R.string.open_dr_desc, R.string.app_name) {


            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(getString(R.string.app_name));
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(getString(R.string.open_dr_desc));
            }

        };
        // Set the drawer toggle as the DrawerListener
        mLeftDrawer.setDrawerListener(mDrawerToggle);

    }

    /**
     * Inits the category views.
     */
    private void initCategoryViews() {
        mLeftDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mCategotyListView = (ListView) findViewById(R.id.sliding_menu_categories_lv);

        mSlidingAboutContainer = findViewById(R.id.sliding_menu_about_container);

        mSlidingAboutContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openAboutScreen();
            }
        });

        TextView blogTitle = (TextView) findViewById(R.id.sliding_menu_blog_title);
        blogTitle.setText(getUserBlogTitle());

        TextView blogUrl = (TextView) findViewById(R.id.sliding_menu_blog_url);
        blogUrl.setText(getUserWebUrl());

        TextView blogMail = (TextView) findViewById(R.id.sliding_menu_blog_mail);
        blogMail.setText(getUserMail());
    }

    /**
     * Give me all categories.
     */
    private void giveMeAllCategories() {
        WordpressFetcher.getInstance(this).fetchAllCategoriesWithCallback(
                new AllCategoriesCallback(this, true));
    }

    /**
     * Close left sliding menu.
     */
    private void closeLeftSlidingMenu() {
        mLeftDrawer.closeDrawer(Gravity.LEFT);
    }

    public void openMenuItem(Category category) {
        performSlidingMenuClicked(category, false, 0);

        showLoadingDialog();

        mWordpressFetcherCallBack.setIsLoadMore(false);
    }

}
