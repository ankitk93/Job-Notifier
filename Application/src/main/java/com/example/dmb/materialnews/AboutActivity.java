package com.example.dmb.materialnews;

import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dmb.settings.AppConstants;

/**
 * The Class AboutActivity.
 */
public class AboutActivity extends BaseActivity {

    TextView blogTitle;
    TextView blogUrl;
    TextView blogMail;
    TextView blogPhone;
    ImageView facebookUrl;
    ImageView twitterUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initViews() {
        blogTitle = (TextView) findViewById(
                R.id.about_fragment_blog_title);
        blogTitle.setText(AppConstants.USER_BLOG_TITLE);

        blogUrl = (TextView) findViewById(
                R.id.about_fragment_blog_url);
        blogUrl.setText(AppConstants.USER_WEBSITE_URL);
        blogUrl.setTag(blogUrl.getText());
        blogUrl.setOnClickListener(new AboutPageActions(1));

        blogMail = (TextView) findViewById(
                R.id.about_fragment_blog_mail);
        blogMail.setText(AppConstants.USER_MAIL);
        blogMail.setTag(blogMail.getText());
        blogMail.setOnClickListener(new AboutPageActions(2));

        blogPhone = (TextView) findViewById(
                R.id.about_fragment_blog_phone);
        blogPhone.setText(AppConstants.USER_PHONE);
        blogPhone.setTag(blogPhone.getText());
        blogPhone.setOnClickListener(new AboutPageActions(3));


        facebookUrl = (ImageView)findViewById(
                R.id.about_fragment_facebook);
        facebookUrl.setTag(AppConstants.FACEBOOK_LINK);
        facebookUrl.setOnClickListener(new AboutPageActions(1));

        twitterUrl = (ImageView) findViewById(
                R.id.about_fragment_twitter);
        twitterUrl.setTag(AppConstants.TWITTER_LINK);
        twitterUrl.setOnClickListener(new AboutPageActions(1));
    }

    /* (non-Javadoc)
    * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * The Class AboutPageActions.
     */
    private class AboutPageActions implements View.OnClickListener {

        /** The Specific case. */
        int mSpecificCase;

        /**
         * Instantiates a new about page actions.
         *
         * @param specCase the spec case
         */
        public AboutPageActions(int specCase) {
            this.mSpecificCase = specCase;
        }

        /* (non-Javadoc)
         * @see android.view.View.OnClickListener#onClick(android.view.View)
         */
        @Override
        public void onClick(View v) {

            String value = (String) v.getTag();

            switch (mSpecificCase) {
                case 1:
                    openWebSiteWithIntent(value);
                    break;
                case 2:
                    openMailWithRecipient(value);
                    break;
                case 3:
                    openDialerWithPhone(value);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Open web site with intent.
     *
     * @param url the url
     */
    private void openWebSiteWithIntent(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    /**
     * Open mail with recipient.
     *
     * @param recipientMail the recipient mail
     */
    private void openMailWithRecipient(String recipientMail) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("vnd.android.cursor.item/email");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,
                new String[] { recipientMail });
        startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
    }

    /**
     * Open dialer with phone.
     *
     * @param phone the phone
     */
    private void openDialerWithPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

}