package com.wyy.myhealth;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	/**
	 * Per the design guidelines, you should show the drawer on launch until the
	 * user manually expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	// private ListView mDrawerListView;
	private View mFragmentContainerView;

	private int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;

	private static final int TAB_LENGTH = 7;
	private LinearLayout[] recorderLay = new LinearLayout[TAB_LENGTH];

	private ImageView[] recorderimg = new ImageView[TAB_LENGTH];

	private TextView[] recordertxt = new TextView[TAB_LENGTH];
	private static final int[][] imageBg = new int[2][TAB_LENGTH];

	static {
		imageBg[0][0] = R.drawable.yinshiheli_icon;
		imageBg[1][0] = R.drawable.yinshiheli_icon_sec;
		imageBg[0][1] = R.drawable.yundongrel_icon;
		imageBg[1][1] = R.drawable.yundongrel_icon_sec;
		imageBg[0][2] = R.drawable.zhifang_icon;
		imageBg[1][2] = R.drawable.zhifang_icon_sec;
		imageBg[0][3] = R.drawable.tanglei_icon;
		imageBg[1][3] = R.drawable.tanglei_icon_sec;
		imageBg[0][4] = R.drawable.danbaizhi_icon;
		imageBg[1][4] = R.drawable.danbaizhi_icon_sec;
		imageBg[0][5] = R.drawable.weishengsu_icon;
		imageBg[1][5] = R.drawable.weishengsu_icon_sec;
		imageBg[0][6] = R.drawable.kuangwuzhi_icon;
		imageBg[1][6] = R.drawable.kuangwuzhi_icon_sec;
	}

	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Read in the flag indicating whether or not the user has demonstrated
		// awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState
					.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		}

		// Select either the default item (0) or the last selected item.
		selectItem(mCurrentSelectedPosition);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_navi_drawer,
				container, false);

		initView(rootView);

		return rootView;
	}

	private void initView(View rootView) {
		recorderLay[0] = (LinearLayout) rootView
				.findViewById(R.id.home0_yinshiheli_layout);
		recorderLay[1] = (LinearLayout) rootView
				.findViewById(R.id.home1_yundongreliang_layout);
		recorderLay[2] = (LinearLayout) rootView
				.findViewById(R.id.home2_zhifang_layout);
		recorderLay[3] = (LinearLayout) rootView
				.findViewById(R.id.home3_tanglei_layout);
		recorderLay[4] = (LinearLayout) rootView
				.findViewById(R.id.home4_danbaizhi_layout);
		recorderLay[5] = (LinearLayout) rootView
				.findViewById(R.id.home5_vshengsu_layout);
		recorderLay[6] = (LinearLayout) rootView
				.findViewById(R.id.home6_kuangwuzhi_layout);

		recorderimg[0] = (ImageView) rootView
				.findViewById(R.id.home0_yinshiheli_img);
		recorderimg[1] = (ImageView) rootView
				.findViewById(R.id.home1_yundongreliang_img);
		recorderimg[2] = (ImageView) rootView
				.findViewById(R.id.home2_zhifang_img);
		recorderimg[3] = (ImageView) rootView
				.findViewById(R.id.home3_tanglei_img);
		recorderimg[4] = (ImageView) rootView
				.findViewById(R.id.home4_danbaizhi_img);
		recorderimg[5] = (ImageView) rootView
				.findViewById(R.id.home5_vshengsu_img);
		recorderimg[6] = (ImageView) rootView
				.findViewById(R.id.home6_kuangwuzhi_img);

		recordertxt[0] = (TextView) rootView
				.findViewById(R.id.home0_yinshiheli_txt);
		recordertxt[1] = (TextView) rootView
				.findViewById(R.id.home1_yundongreliang_txt);
		recordertxt[2] = (TextView) rootView
				.findViewById(R.id.home2_zhifang_txt);
		recordertxt[3] = (TextView) rootView
				.findViewById(R.id.home3_tanglei_txt);
		recordertxt[4] = (TextView) rootView
				.findViewById(R.id.home4_danbaizhi_txt);
		recordertxt[5] = (TextView) rootView
				.findViewById(R.id.home5_vshengsu_txt);
		recordertxt[6] = (TextView) rootView
				.findViewById(R.id.home6_kuangwuzhi_txt);
		for (int i = 0; i < TAB_LENGTH; i++) {
			recorderLay[i].setOnClickListener(listener);
		}
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.navigation_drawer_open, /*
										 * "open drawer" description for
										 * accessibility
										 */
		R.string.navigation_drawer_close /*
										 * "close drawer" description for
										 * accessibility
										 */
		) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}

				getActivity().supportInvalidateOptionsMenu(); // calls
																// onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}

				if (!mUserLearnedDrawer) {
					// The user manually opened the drawer; store this flag to
					// prevent auto-showing
					// the navigation drawer automatically in the future.
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
							.commit();
				}

				getActivity().supportInvalidateOptionsMenu(); // calls
																// onPrepareOptionsMenu()
			}
		};

		// If the user hasn't 'learned' about the drawer, open it to introduce
		// them to the drawer,
		// per the navigation drawer design guidelines.
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void selectItem(int position) {
		mCurrentSelectedPosition = position;
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar.
		// See also
		// showGlobalContextActionBar, which controls the top-left area of the
		// action bar.
		if (mDrawerLayout != null && isDrawerOpen()) {
//			inflater.inflate(R.menu.global, menu);
			showGlobalContextActionBar();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}


		return super.onOptionsItemSelected(item);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to
	 * show the global app 'context', rather than just what's in the current
	 * screen.
	 */
	private void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(R.string.health_recoder);
	}

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.home0_yinshiheli_layout:
				showRecorderFragment(0);
				break;

			case R.id.home1_yundongreliang_layout:
				showRecorderFragment(1);
				break;

			case R.id.home2_zhifang_layout:
				showRecorderFragment(2);
				break;

			case R.id.home3_tanglei_layout:
				showRecorderFragment(3);
				break;

			case R.id.home4_danbaizhi_layout:
				showRecorderFragment(4);
				break;

			case R.id.home5_vshengsu_layout:
				showRecorderFragment(5);
				break;

			case R.id.home6_kuangwuzhi_layout:
				showRecorderFragment(6);
				break;

			default:
				break;
			}
		}
	};

	private void showRecorderFragment(int id) {

		for (int i = 0; i < TAB_LENGTH; i++) {
			if (i == id) {
				recorderLay[i].setBackgroundColor(getResources().getColor(
						R.color.themecolor));
				recordertxt[i].setTextColor(Color.WHITE);
				recorderimg[i].setImageResource(imageBg[1][i]);
			} else {
				recorderLay[i].setBackgroundColor(Color.WHITE);
				recordertxt[i].setTextColor(Color.BLACK);
				recorderimg[i].setImageResource(imageBg[0][i]);
			}
		}

		selectItem(id);

	}

}
