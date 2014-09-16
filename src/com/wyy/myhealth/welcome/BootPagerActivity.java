package com.wyy.myhealth.welcome;

import java.util.ArrayList;
import java.util.List;

import com.wyy.myhealth.R;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.login.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BootPagerActivity extends Activity implements ActivityInterface {

	private ViewPager pager;
	private View view0, view1, view2, view3;
	private List<View> picList = new ArrayList<View>();
	private ViewGroup group;
	private ImageView[] imageViews;

	private ImageView imageView;

	private ImageView p0;

	private ImageView p1;

	private ImageView p2;

	private ImageView p3;

	private TextView oneTextView;

	private TextView oneTextView_content;

	private TextView twoTextView;

	private TextView twoTextView_content;

	private TextView thrTextView;

	private TextView thrTextView_content;

	private TextView fouTextView;

	private TextView fouTextView_content;

	private TextView enter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_boot_pager);
		initView();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		pager = (ViewPager) findViewById(R.id.boot_Pager);
		LayoutInflater layoutInflater = getLayoutInflater();
		view0 = layoutInflater.inflate(R.layout.pager0, null, false);
		view1 = layoutInflater.inflate(R.layout.pager0, null, false);
		view2 = layoutInflater.inflate(R.layout.pager0, null, false);
		view3 = layoutInflater.inflate(R.layout.pager0, null, false);

		p0 = (ImageView) view0.findViewById(R.id.boot_img_content);
		p1 = (ImageView) view1.findViewById(R.id.boot_img_content);
		p2 = (ImageView) view2.findViewById(R.id.boot_img_content);
		p3 = (ImageView) view3.findViewById(R.id.boot_img_content);

		oneTextView = (TextView) view0.findViewById(R.id.boot_txt_title);
		oneTextView_content = (TextView) view0
				.findViewById(R.id.boot_txt_content);
		twoTextView = (TextView) view1.findViewById(R.id.boot_txt_title);
		twoTextView_content = (TextView) view1
				.findViewById(R.id.boot_txt_content);
		thrTextView = (TextView) view2.findViewById(R.id.boot_txt_title);
		thrTextView_content = (TextView) view2
				.findViewById(R.id.boot_txt_content);
		fouTextView = (TextView) view3.findViewById(R.id.boot_txt_title);
		fouTextView_content = (TextView) view3
				.findViewById(R.id.boot_txt_content);

		oneTextView.setText(R.string.boot_title1);
		oneTextView_content.setText(R.string.boot_title1_content);
		twoTextView.setText(R.string.boot_title2);
		twoTextView_content.setText(R.string.boot_title2_content);
		thrTextView.setText(R.string.boot_title3);
		thrTextView_content.setText(R.string.boot_title3_content);
		fouTextView.setText(R.string.boot_title4);
		fouTextView_content.setText(R.string.boot_title4_content);

		p0.setImageResource(R.drawable.boot1);
		p1.setImageResource(R.drawable.boot2);
		p2.setImageResource(R.drawable.boot3);
		p3.setImageResource(R.drawable.boot4);

		enter = (TextView) view3.findViewById(R.id.enter_btn);
		enter.setVisibility(View.VISIBLE);

		picList.add(view0);
		picList.add(view1);
		picList.add(view2);
		picList.add(view3);

		pager.setAdapter(new BingPagerAdapter(picList));

		pager.setCurrentItem(0);
		pointslid();

		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		pager.setOnPageChangeListener(listener);
		enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(BootPagerActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	/**
	 * 初始化小点
	 */
	public void pointslid() {

		group = (ViewGroup) findViewById(R.id.point_line);
		imageViews = new ImageView[picList.size()];
		for (int i = 0; i < picList.size(); i++) {
			LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			margin.setMargins(10, 0, 0, 0);
			imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(15, 15));
			imageViews[i] = imageView;
			if (i == 0) {
				imageViews[i].setBackgroundResource(R.drawable.icon_done);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.icon_next);
			}
			group.addView(imageViews[i], margin);
		}
	}

	private OnPageChangeListener listener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0].setBackgroundResource(R.drawable.icon_done);
				if (arg0 != i) {
					imageViews[i].setBackgroundResource(R.drawable.icon_next);
				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};

}
