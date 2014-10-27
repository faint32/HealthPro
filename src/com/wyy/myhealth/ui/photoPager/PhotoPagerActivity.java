package com.wyy.myhealth.ui.photoPager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.photoview.PhotoView;
import com.wyy.myhealth.ui.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.wyy.myhealth.ui.photoview.ZoomOutPageTransformer;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PhotoPagerActivity extends BaseActivity implements
		OnPageChangeListener {

	private ViewPager pager;
	private DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	private PhotoPagerAdapter mAdapter;

	private List<String> imgurList = new ArrayList<String>();
	private TextView postionTextView;
	private TextView sumTextView;
	private HashSet<ViewGroup> unRecycledViews = new HashSet<ViewGroup>();
	// private static final int STATUS_BAR_HEIGHT_DP_UNIT = 25;
	// private static final int NAVIGATION_BAR_HEIGHT_DP_UNIT = 48;
	private ProgressBar loadingBar;

	private boolean loads2f = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_pager);
		initSetting();
		initUI();

	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.ablum);
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources()
				.getColor(R.color.themetranscolor)));
		actionBar.setSplitBackgroundDrawable(new ColorDrawable(getResources()
				.getColor(R.color.deepskyblue)));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UmenAnalyticsUtility.onResume(context);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		UmenAnalyticsUtility.onPause(context);
	}

	private void initUI() {
		imgurList = getIntent().getStringArrayListExtra("imgurls");
		pager = (ViewPager) findViewById(R.id.picpager);
		sumTextView = (TextView) findViewById(R.id.sum_textV);
		postionTextView = (TextView) findViewById(R.id.index_textV);
		loadingBar = (ProgressBar) findViewById(R.id.pic_progressBar);

		if (imgurList == null) {
			return;
		}

		sumTextView.setText("" + imgurList.size());
		int postion = getIntent().getIntExtra("postion", 0) + 1;
		postionTextView.setText("" + postion);

		mAdapter = new PhotoPagerAdapter(imgurList);
		pager.setAdapter(mAdapter);

		pager.setOnPageChangeListener(this);

		pager.setCurrentItem(getIntent().getIntExtra("postion", 0));

		// pager.setOffscreenPageLimit(1);
		pager.setPageTransformer(true, new ZoomOutPageTransformer());
		// pager.setPadding(0, Utility.dip2px(STATUS_BAR_HEIGHT_DP_UNIT), 0, 0);

	}

	private void initSetting() {
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.pic_empty)
				.showImageOnFail(R.drawable.pic_failure).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	public class PhotoPagerAdapter extends PagerAdapter {

		private List<String> list;
		private LayoutInflater inflater;

		public PhotoPagerAdapter(List<String> list) {
			this.list = list;
			this.inflater = getLayoutInflater();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0.equals(arg1);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			if (!loads2f) {
				if (object instanceof ViewGroup) {
					try {
						((ViewPager) container).removeView((View) object);
						unRecycledViews.remove(object);
					} catch (Exception e) {
						// TODO: handle exception
					}

					// ViewGroup viewGroup = (ViewGroup) object;
					// try {
					// Utility.recycleViewGroupAndChildViews(viewGroup, true);
					// } catch (Exception e) {
					// // TODO: handle exception
					// }

				}
			}

		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			View imageLayout = inflater.inflate(R.layout.photo_pager_,
					container, false);
			assert imageLayout != null;

			handelImg(imageLayout, list, position);
			// container.addView(imageLayout, position);
			((ViewPager) container).addView(imageLayout, 0);
			unRecycledViews.add((ViewGroup) imageLayout);
			return imageLayout;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			// TODO Auto-generated method stub
			super.setPrimaryItem(container, position, object);
			View imageLayout = (View) object;
			if (imageLayout == null) {
				return;
			}

			ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.photoView1);

			if (imageView.getDrawable() != null) {
				return;
			}

			handelImg(imageLayout, list, position);
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return super.saveState();
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		postionTextView.setText("" + (arg0 + 1));
	}

	private void handelImg(View imageLayout, List<String> list, int position) {
		final PhotoView photoView = (PhotoView) imageLayout
				.findViewById(R.id.photoView1);

		photoView.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View view, float x, float y) {
				// TODO Auto-generated method stub
				if (photoView == null
						|| (!(photoView.getDrawable() instanceof BitmapDrawable))) {
					PhotoPagerActivity.this.finish();
					return;
				}
			}
		});

		imageLoader.displayImage(
				HealthHttpClient.IMAGE_URL + list.get(position), photoView,
				options, new SimpleImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						super.onLoadingStarted(imageUri, view);
						loadingBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub
						super.onLoadingFailed(imageUri, view, failReason);
						loadingBar.setVisibility(View.GONE);
						// photoView.setImageResource(R.drawable.pic_failure);
						loads2f = true;
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub
						super.onLoadingComplete(imageUri, view, loadedImage);
						loadingBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub
						super.onLoadingCancelled(imageUri, view);
						loadingBar.setVisibility(View.GONE);
						// photoView.setImageResource(R.drawable.pic_failure);
					}

				});

		// imageLoader.displayImage(list.get(position), animationView);
		//
		// if (Utility.doThisDeviceOwnNavigationBar(PhotoPagerActivity.this)) {
		// photoView.setPadding(0, 0, 0,
		// Utility.dip2px(NAVIGATION_BAR_HEIGHT_DP_UNIT));
		// }

	}

}
