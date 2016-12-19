package com.dmbteam.wordpress.fetcher.cmn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dmbteam.wordpress.fetcher.util.DateUtil;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

/**
 * This class represents a Post from Wordpress
 * 
 * @author |dmb TEAM|
 * 
 */
public class Post implements Serializable {

	/**
	 * UID version for serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * List of Post Attachments
	 */
	private List<Attachment> mAttachments;

	/**
	 * List of Post Categories
	 */
	private List<Category> mCategories;

	/**
	 * Content of the Post
	 */
	private String mContent;

	/**
	 * Identifier of the Post
	 */
	private String mid;

	/**
	 * The date that the Post was published
	 */
	private Date mPublishedDate;

	/**
	 * Status of the Post
	 */
	private String mStatus;

	/**
	 * Title of the Post
	 */
	private String mTitle;

	/**
	 * URL of the Post
	 */
	private String mUrl;

	private int mVIdeoCounter;

	private int mRecentPostsCounter = -1;

	/**
	 * Class constructor
	 * 
	 * @param jsonObject
	 *            JSON Object
	 * @throws Exception
	 */
	public Post(JSONObject jsonObject) throws Exception {
		this.mid = jsonObject.getString("id");
		this.mTitle = Html.fromHtml(jsonObject.getString("title_plain"))
				.toString();
		String text = embedYoutubeVideos(jsonObject.getString("content"));
		this.mContent = text;

		removeAdsense();
		this.mStatus = jsonObject.getString("status");
		this.mUrl = jsonObject.getString("url");
		this.mPublishedDate = DateUtil.postsDatePublishedFormatter
				.parse(jsonObject.getString("date"));

		JSONArray categoriesJson = jsonObject.getJSONArray("categories");

		mCategories = new ArrayList<Category>();

		for (int i = 0; i < categoriesJson.length(); i++) {
			mCategories.add(new Category(categoriesJson.getJSONObject(i)));
		}

		// JSONArray attachmentsJson = jsonObject.getJSONArray("attachments");

		mAttachments = new ArrayList<Attachment>();

		/*
		 * for (int i = 0; i < attachmentsJson.length(); i++) {
		 * 
		 * mAttachments.add(new Attachment(attachmentsJson.getJSONObject(i))); }
		 */

		addAttachmentFromThumbnailImages(jsonObject);
		addAttachmentFromAttachments(jsonObject);

		for (Attachment attachment : mAttachments) {
			if (attachment.getFullSize() == null
					&& attachment.getLargeSize() == null
					&& attachment.getMediumSize() == null
					&& attachment.getNotSizedAttachment() == null) {
				mAttachments.remove(attachment);
			}
		}

		Log.d("Adding Post", "Title - " + mTitle);

	}

	public Post(JSONObject jsonObject, Category category) throws Exception {
		this(jsonObject);

		this.mCategories = new ArrayList<Category>(0);
		this.mCategories.add(category);

	}

	/**
	 * Getter of the Post Attachments
	 * 
	 * @return Post Attachments
	 */
	public List<Attachment> getAttachments() {
		return mAttachments;
	}

	/**
	 * Getter of the Post Categories
	 * 
	 * @return Post Categories
	 */
	public List<Category> getCategories() {
		return mCategories;
	}

	/**
	 * Getter of the Post content
	 * 
	 * @return Post content
	 */
	public Spanned getContent() {
		return Html.fromHtml(mContent.replace((char) 160, (char) 32)
				.replace((char) 65532, (char) 32).trim());
	}

	public String getContentString() {
		return mContent;
	}

	/**
	 * Getter of the Post identifier
	 * 
	 * @return Post identifier
	 */
	public String getId() {
		return mid;
	}

	/**
	 * Getter of date when the Post was published
	 * 
	 * @return date when the Post was published
	 */
	public Date getPublishedDate() {
		return mPublishedDate;
	}

	/**
	 * Getter of the Post Status
	 * 
	 * @return Post Status
	 */
	public String getStatus() {
		return mStatus;
	}

	/**
	 * Getter of the Post Title
	 * 
	 * @return Post Title
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Getter of the Post URL
	 * 
	 * @return Post URL
	 */
	public String getUrl() {
		return mUrl;
	}

	public boolean isSliderPost() {
		return getAttachments().size() > 0 ? true : false;
	}

	public String getPostCategoryAsString() {

		if (mCategories == null && mCategories.size() == 0) {
			return "N/A";
		}

		String result = "";

		for (int i = 0; i < mCategories.size(); i++) {

			result += mCategories.get(i).getTitle();

			if (i != mCategories.size() - 1) {
				result += ", ";
			}

		}

		return result;
	}

	public String giveMeBiggestAttachment() {

		String result = "";

		int maxValue = 0;

		if (mAttachments != null && mAttachments.size() > 0) {
			for (int i = 0; i < mAttachments.size(); i++) {
				if (mAttachments.get(i).getFullSize().getWidth() > maxValue) {
					maxValue = mAttachments.get(i).getFullSize().getWidth();
					result = mAttachments.get(i).getFullSize().getUrl();
				}
			}
		}

		return result;
	}

	public int getVIdeoCounter() {
		return mVIdeoCounter;
	}

	public int getRecentPostsCounter() {
		return mRecentPostsCounter;
	}

	public void setRecentPostsCounter(int recentPostsCounter) {
		this.mRecentPostsCounter = recentPostsCounter;
	}

	private void addAttachmentFromThumbnailImages(JSONObject jsonObject)
			throws JSONException {
		if (jsonObject.has("thumbnail_images")) {
			JSONArray jsonArray = jsonObject.optJSONArray("thumbnail_images");
			if (jsonArray != null) {
				if (jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						mAttachments.add(new Attachment(jsonArray
								.getJSONObject(i)));
					}
				}
			} else {
				JSONObject image = jsonObject.optJSONObject("thumbnail_images");
				if (image != null) {
					mAttachments.add(new Attachment(image));
				}
			}
		}
	}

	private void addAttachmentFromAttachments(JSONObject jsonObject)
			throws JSONException {
		if (jsonObject.has("attachments")) {
			JSONArray attachmentsJson = jsonObject.getJSONArray("attachments");
			for (int i = 0; i < attachmentsJson.length(); i++) {
				JSONObject attachment = attachmentsJson.getJSONObject(i);
				if (attachment.has("images")
						&& attachment.optJSONArray("images") != null
						&& attachment.optJSONArray("images").length() > 0) {
					mAttachments.add(new Attachment(attachmentsJson
							.getJSONObject(i)));
				} else if (attachment.has("url") || attachment.has("full")) {
					mAttachments.add(new Attachment(attachmentsJson
							.getJSONObject(i)));
				}
			}
		}
	}

	private void removeAdsense() throws Exception {
		String adSensePrefix = "(adsbygoogle";
		String adSenseSuffix = ".push({});";
		if (mContent.indexOf(adSensePrefix) > -1) {
			int startSuffixPosition = mContent.indexOf(adSensePrefix);
			int endSuffixPosition = mContent.indexOf(adSenseSuffix,
					startSuffixPosition);

			mContent = mContent.replace(
					mContent.substring(startSuffixPosition, endSuffixPosition
							+ adSenseSuffix.length()), "");
			;

		}
	}

	private String embedYoutubeVideos(String text) throws Exception {
		String adSensePrefix = "<iframe";
		String adSenseSuffix = "</iframe>";
		String sourceStart = "src=\"";
		String sourceEnd = "\"";
		String source = "";

		if (text.indexOf(adSensePrefix) > -1) {
			int startSuffixPosition = text.indexOf(adSensePrefix);
			int endSuffixPosition = text.indexOf(adSenseSuffix,
					startSuffixPosition);
			if (text.indexOf(sourceStart) > -1) {
				int sourceStartPosition = text.indexOf(sourceStart)
						+ sourceStart.length();
				int sourceEndPosition = text.indexOf(sourceEnd,
						sourceStartPosition) + sourceEnd.length();
				source = text.substring(sourceStartPosition,
						sourceEndPosition - 1);
				source = source.replaceFirst("//", "");
				source = "<a href=\"http://" + source + "\">" + source + "</a>";
				++mVIdeoCounter;
			}

			text = text.replace(
					text.substring(startSuffixPosition, endSuffixPosition
							+ adSenseSuffix.length()), source);
			;
		}
		return text;
	}

}
