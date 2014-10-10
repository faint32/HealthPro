package com.wyy.myhealth.ui.mapfood;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKGeocoderAddressComponent;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionInfo;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.PreferencesFoodsInfo;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.Foods;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.ui.fooddetails.FoodDetailsActivity;
import com.wyy.myhealth.ui.yaoyingyang.YaoyingyangFragment;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.FoodsUtil;
import com.wyy.myhealth.utils.ImageUtil;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MapFoodsActivity extends ActionBarActivity {

	private static final String TAG = MapFoodsActivity.class.getSimpleName();

	private static final double SCALE = 1000000.0;
	private static final int INIT_LEVEL = 17;
	private static final int SHOW_LEVEL = 15;
	private static final double HALF = 0.5;
	/**
	 * pu MapView 是地图主控件
	 */
	private MapView mMapView = null;
	/**
	 * 用MapController完成地图控制
	 */
	static MyOverlay mOverlay = null;

	// 定位相关
	LocationClient mLocClient;
	LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isRequest = false;// 是否手动触发请求定位
	boolean isFirstLoc = true;// 是否首次定位
	HashMap<OverlayItem, Foods> imemFoods = new HashMap<OverlayItem, Foods>();
	public static ArrayList<Foods> foodsList;
	public static Foods foods;
	// 定位图层
	MyLocationOverlay myLocationOverlay = null;

	/**
	 * MKMapViewListener 用于处理地图事件回调
	 */
	MKMapViewListener mMapListener = null;

	// public static EditText edit_search;
	private MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	/**
	 * 搜索关键字输入窗口
	 */
	private ArrayAdapter<String> sugAdapter = null;
	private String city = null;

	private SearchView searchView;

	private GeoPoint point;

	private boolean isone = false;

	private String foodid = "";

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initActionBar();
		// 防止networkonmainthreadexception异常
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		// initFirst();
		/**
		 * 使用地图sdk前需先初始化BMapManager. BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
		 * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
		 */
		WyyApplication app = (WyyApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			/**
			 * 如果BMapManager没有初始化则初始化BMapManager
			 */
			app.mBMapManager.init(WyyApplication.strKey,
					new MKGeneralListener() {

						@Override
						public void onGetPermissionState(int arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onGetNetworkState(int arg0) {
							// TODO Auto-generated method stub

						}
					});
		}
		/**
		 * 由于MapView在setContentView()中初始化,所以它需要在BMapManager初始化之后
		 */
		setContentView(R.layout.activity_map);

		mMapView = (MapView) findViewById(R.id.foodsmapView);

		if (!FoodsUtil.checkDataBase()) {
			System.out.println("数据库ls.db不存在");
		}

		/**
		 * 显示内置缩放控件
		 */
		mMapView.setBuiltInZoomControls(false);

		mOverlay = new MyOverlay(getResources().getDrawable(
				R.drawable.icon_marka), mMapView, this);
		mMapView.getOverlays().add(mOverlay);
		createMapListener();

		// createSearch();

		/**
		 * 创建一个popupoverlay
		 */
		// createPop();
		// 地图初始化

		/**
		 * 设置地图是否响应点击事件 .
		 */
		mMapView.getController().setZoom(INIT_LEVEL);
		mMapView.getController().enableClick(true);
		// 俯视角度
		mMapView.getController().setOverlooking(-45);

		mMapView.refresh();

		// 定位初始化
		mLocClient = new LocationClient(this);
		locData = new LocationData();

		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		// mLocClient.start();

		// 定位图层初始化
		myLocationOverlay = new MyLocationOverlay(mMapView);
		myLocationOverlay.setLocationMode(LocationMode.NORMAL);
		// 设置定位数据
		myLocationOverlay.setData(locData);
		// 位置样式图片
		Drawable drawable = getResources().getDrawable(R.drawable.zhuye_gprs);
		drawable.setBounds(10, 10, 50, 50);
		myLocationOverlay.setMarker(drawable);

		// 添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);

		if (YaoyingyangFragment.isdingwei) {
			foodid = PreferencesFoodsInfo.getfoodId(MapFoodsActivity.this);
			Intent intent = getIntent();
			double lat = intent.getDoubleExtra("lat", 0);
			double lon = intent.getDoubleExtra("lon", 0);

			Log.i(TAG, "经度:" + lat + "纬度:" + lon);

			point = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
			mMapView.getController().setCenter(point);
			resetOverlay();
			createSearch();
			YaoyingyangFragment.isdingwei = false;
			return;
		}

		mLocClient.start();

		createSearch();

	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub

		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);

				} catch (Exception e) {
				}
			}
		}

		return super.onMenuOpened(featureId, menu);
	}

	private void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_g_bg));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);

		searchView = new SearchView(this);
		settingSearchView();
		actionBar.setCustomView(searchView, new ActionBar.LayoutParams(
				Gravity.RIGHT));
		actionBar.setDisplayShowCustomEnabled(true);
		searchView.setOnQueryTextListener(searchListener);

	}

	private OnQueryTextListener searchListener = new OnQueryTextListener() {

		@Override
		public boolean onQueryTextSubmit(String arg0) {
			// TODO Auto-generated method stub
			mSearch.poiSearchInCity(city, arg0);
			return true;
		}

		@Override
		public boolean onQueryTextChange(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	// 监听地图事件
	public void createMapListener() {
		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
				/**
				 * 在此处理地图移动完成回调 缩放，平移等操作完成后，此回调被触发
				 */
				resetOverlay();
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * 在此处理底图poi点击事件 显示底图poi名称并移动至该点 设置过： 时，此回调才能被触发
				 */
				@SuppressWarnings("unused")
				String title = "";
				// if (mapPoiInfo != null){
				// title = mapPoiInfo.strText;
				// Toast.makeText(FoodsMapActivity.this,title,Toast.LENGTH_SHORT).show();
				// mMapController.animateTo(mapPoiInfo.geoPt);
				// }
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				/**
				 * 当调用过 mMapView.getCurrentMap()后，此回调会被触发 可在此保存截图至存储设备
				 */
			}

			@Override
			public void onMapAnimationFinish() {
				/**
				 * 地图完成带动画的操作（如: animationTo()）后，此回调被触发
				 */
			}

			/**
			 * 在此处理地图载完成事件
			 */
			@Override
			public void onMapLoadFinish() {
				Toast.makeText(MapFoodsActivity.this, "地图加载完成",
						Toast.LENGTH_SHORT).show();
				// requestLocClick();
				// resetOverlay();
			}
		};
		mMapView.regMapViewListener(WyyApplication.getInstance().mBMapManager,
				mMapListener);

	}

	/**
	 * 修改位置图标
	 * 
	 * @param marker
	 */
	public void modifyLocationOverlayIcon(Drawable marker) {
		// 当传入marker为null时，使用默认图标绘制
		myLocationOverlay.setMarker(marker);
		// 修改图层，需要刷新MapView生效
		mMapView.refresh();
	}

	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay() {
		Toast.makeText(MapFoodsActivity.this, "加载美食……", Toast.LENGTH_SHORT)
				.show();
		new Thread() {
			@Override
			public void run() {// 你要执行的方法

				if (mMapView.getZoomLevel() > SHOW_LEVEL) {
					GeoPoint gp = mMapView.getMapCenter();
					// Log.i("resetOverlay", "mMapView.getZoomLevel() ："
					// + mMapView.getZoomLevel());
					// Log.i("resetOverlay", "经度宽 ：" +
					// mMapView.getLongitudeSpan());

					try {
						double minx = (gp.getLongitudeE6() - mMapView
								.getLongitudeSpan() * HALF)
								/ SCALE;
						double miny = (gp.getLatitudeE6() - mMapView
								.getLatitudeSpan() * HALF)
								/ SCALE;
						double maxx = (gp.getLongitudeE6() + mMapView
								.getLongitudeSpan() * HALF)
								/ SCALE;
						double maxy = (gp.getLatitudeE6() + mMapView
								.getLatitudeSpan() * HALF)
								/ SCALE;
						BingLog.i("地图", "minx:" + minx + "miny:" + miny
								+ "maxx:" + maxx + "maxy:" + maxy);
						searchByBox(minx, miny, maxx, maxy);
					} catch (Exception e) {
						// TODO: handle exception
						BingLog.e("地图", "解析错误:" + e.toString());
						newSearch();
					}

				} else {
					newSearch();
				}
			}
		}.start();

	}

	private void newSearch() {
		try {
			double minx = (point.getLongitudeE6() - 1) / SCALE;
			double miny = (point.getLatitudeE6() - 1) / SCALE;
			double maxx = (point.getLongitudeE6() + 1) / SCALE;
			double maxy = (point.getLatitudeE6() + 1) / SCALE;
			BingLog.i("地图", "minx:" + minx + "miny:" + miny + "maxx:" + maxx
					+ "maxy:" + maxy);
			searchByBox(minx, miny, maxx, maxy);
		} catch (Exception e) {
			// TODO: handle exception
			BingLog.e("地图2", "解析错误:" + e.toString());
		}
	}

	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void search4Place() {
		Toast.makeText(MapFoodsActivity.this, "加载美食……", Toast.LENGTH_SHORT)
				.show();
		new Thread() {
			@Override
			public void run() {// 你要执行的方法

				if (mMapView.getZoomLevel() > SHOW_LEVEL) {
					GeoPoint gp = mMapView.getMapCenter();

					try {
						double minx = (gp.getLongitudeE6() - mMapView
								.getLongitudeSpan() * HALF)
								/ SCALE;
						double miny = (gp.getLatitudeE6() - mMapView
								.getLatitudeSpan() * HALF)
								/ SCALE;
						double maxx = (gp.getLongitudeE6() + mMapView
								.getLongitudeSpan() * HALF)
								/ SCALE;
						double maxy = (gp.getLatitudeE6() + mMapView
								.getLatitudeSpan() * HALF)
								/ SCALE;
						Log.i("地图", "minx:" + minx + "miny:" + miny + "maxx:"
								+ maxx + "maxy:" + maxy);
						searchByBox(minx, miny, maxx, maxy);
					} catch (Exception e) {
						// TODO: handle exception
						Log.e("地图", "解析错误:" + e.toString());
					}

				}
			}
		}.start();

	}

	/**
	 * 手动触发一次定位请求
	 */
	public void requestLocClick() {
		isRequest = true;
		mLocClient.requestLocation();
		Toast.makeText(MapFoodsActivity.this, "正在定位……", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	protected void onPause() {
		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		mMapView.onPause();
		super.onPause();
		UmenAnalyticsUtility.onPause(this);
	}

	@Override
	protected void onResume() {
		/**
		 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		 */
		mMapView.onResume();
		super.onResume();
		UmenAnalyticsUtility.onResume(this);
	}

	@Override
	protected void onDestroy() {
		/**
		 * MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		 */
		mMapView.destroy();
		if (mLocClient != null)
			mLocClient.stop();
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * 根据范围查询美食
	 * 
	 * @param minx
	 * @param miny
	 * @param maxx
	 * @param maxy
	 */
	protected void searchByBox(double minx, double miny, double maxx,
			double maxy) {

		FoodsUtil foodsHands = new FoodsUtil();
		AsyncHttpResponseHandler res = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				BingLog.i(TAG, "搜索返回:" + response);
				foodsList = parseFoods(response);
				showFoods(foodsList);

			}

		};
		foodsHands.findByBox(minx, miny, maxx, maxy, res);
	}

	protected void searchByCircle() {
		Toast.makeText(MapFoodsActivity.this, "加载美食……", Toast.LENGTH_SHORT)
				.show();
		new Thread() {
			@Override
			public void run() {// 你要执行的方法
				FoodsUtil foodsHands = new FoodsUtil();
				AsyncHttpResponseHandler res = new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						System.out.println(response);
						foodsList = parseFoods(response);
						showFoods(foodsList);

					}
				};
				foodsHands.findByCircle(locData.longitude, locData.latitude,
						res);
			}
		}.start();
	}

	/**
	 * 根据名称查询美食
	 * 
	 * @param str
	 */
	protected void searchByName(String str) {
		FoodsUtil foodsHands = new FoodsUtil();
		AsyncHttpResponseHandler res = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				System.out.println(response);

				foodsList = parseFoods(response);
				showFoods(foodsList);

			}
		};
		foodsHands.findByText(str, res);

	}

	/**
	 * 解析json格式美食数据
	 * 
	 * @param str
	 * @return
	 */
	public ArrayList<Foods> parseFoods(String str) {
		ArrayList<Foods> list = new ArrayList<Foods>();
		try {

			JSONObject result = new JSONObject(str);
			if (result.isNull("foods"))
				return list;
			JSONArray json = result.getJSONArray("foods");

			int length = json.length();
			for (int i = 0; i < length; i++) {
				// Foods food=new Foods();
				JSONObject obj = json.getJSONObject(i);
				Foods food = JsonUtils.getfoods(obj);
				// food.setCommercialLat(obj.getString("commercialLat"));
				// food.setCommercialLon(obj.getString("commercialLon"));
				// food.setCommercialName(obj.getString("commercialName"));
				// food.setCommercialAddress(obj.getString("commercialAddress"));
				// food.setCommercialPhone(obj.getString("commercialPhone"));
				// food.setTags(obj.getString("tags"));
				// food.setId(obj.getString("id"));
				// food.setTastelevel(obj.getString("tastelevel"));
				// food.setFoodpic(obj.getString("foodpic"));
				// food.setSummary(obj.getString("summary"));
				// food.setEnergy(obj.getString("energy"));
				// food.setType(obj.getString("type"));
				if (!isone) {
					if (foodid.equals(food.getId())) {
						list.add(food);
					}
				} else {
					list.add(food);
				}

			}
			isone = true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 显示美食
	 * 
	 * @param foods
	 */
	protected void showFoods(ArrayList<Foods> foods) {
		mOverlay.clearOutBundItem();
		mOverlay.addFoods(foods);
		System.out.print(mOverlay.size());
		mMapView.refresh();

	}

	public class MyOverlay extends ItemizedOverlay<OverlayItem> {
		private Context context;

		public MyOverlay(Drawable defaultMarker, MapView mapView,
				Context context) {
			super(defaultMarker, mapView);
			this.context = context;
		}

		@Override
		public boolean onTap(int index) {
			OverlayItem item = getItem(index);
			foods = imemFoods.get(item);

			Intent intent = new Intent();
			PreferencesFoodsInfo.setfoodId(MapFoodsActivity.this,
					"" + foods.getId());
			intent.setClass(context, FoodDetailsActivity.class);
			context.startActivity(intent);
			return true;
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mMapView) {
			// if (pop != null) {
			// pop.hidePop();
			// mMapView.removeView(button);
			// }
			return false;
		}

		public void clearOutBundItem() {

			GeoPoint gp = mMapView.getMapCenter();
			double minx = gp.getLongitudeE6() - mMapView.getLongitudeSpan()
					* HALF;
			double miny = gp.getLatitudeE6() - mMapView.getLatitudeSpan()
					* HALF;
			double maxx = gp.getLongitudeE6() + mMapView.getLongitudeSpan()
					* HALF;
			double maxy = gp.getLatitudeE6() + mMapView.getLatitudeSpan()
					* HALF;
			ArrayList<OverlayItem> items = this.getAllItem();

			for (OverlayItem item : items) {
				GeoPoint p = item.getPoint();
				if (p.getLongitudeE6() < minx || p.getLongitudeE6() > maxx
						|| p.getLatitudeE6() < miny || p.getLatitudeE6() > maxy) {
					this.removeItem(item);
					// Log.i("", "清除 ｉｔｅｍ" + item);
				}
			}

		}

		public void addFoods(ArrayList<Foods> foods) {
			/**
			 * 创建自定义overlay
			 */

			for (Foods food : foods) {

				/**
				 * 准备overlay 数据
				 */
				GeoPoint p1 = new GeoPoint(
						(int) (Double.parseDouble(food.getCommercialLat()) * 1E6),
						(int) (Double.parseDouble(food.getCommercialLon()) * 1E6));
				// OverlayItem item = new OverlayItem(p1,
				// food.getCommercialName(), "");
				// item.setMarker(getResources().getDrawable(R.drawable.icon_gcoding));
				// item.setMarker(ImageUtil.loadImageFromUrl("http://tx.bdimg.com/sys/portrait/item/990e6271796a7a6c170c.jpg"));
				boolean flag = false;
				ArrayList<OverlayItem> items = this.getAllItem();
				for (OverlayItem item : items) {
					GeoPoint p = item.getPoint();
					if (p.getLongitudeE6() == p1.getLongitudeE6()
							&& p.getLatitudeE6() == p.getLatitudeE6()) {
						imemFoods.put(item, food);
						flag = true;
						break;
					}
				}

				if (!flag) {

					OverlayItem item = new OverlayItem(p1,
							food.getCommercialName(), "");

					imemFoods.put(item, food);
					item.setMarker(ImageUtil
							.loadImageFromUrl(FoodsUtil.urlMiniImage
									+ food.getId()));
					item.setAnchor(0.5f, 1f);

					/**
					 * 将item 添加到overlay中 注意： 同一个itme只能add一次
					 */
					addItem(item);
				}

			}
		}

	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		double[] oldLocaltion = new double[2];
		final double maxDistance = 100;

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			// 如果不显示定位精度圈，将accuracy赋值为0即可
			locData.accuracy = location.getRadius();
			// 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
			locData.direction = location.getDerect();
			// 更新定位数据
			myLocationOverlay.setData(locData);
			// 更新图层数据执行刷新后生效
			mMapView.refresh();
			// 是手动触发请求或首次定位时，移动到定位点
			if (isRequest || isFirstLoc) {
				// 移动地图到定位点
				// Log.d("LocationOverlay", "receive location, animate to it");
				mMapView.getController().animateTo(
						new GeoPoint((int) (locData.latitude * 1e6),
								(int) (locData.longitude * 1e6)));
				initCity();
				/**
				 * 设置地图缩放级别
				 */
				isRequest = false;

			}

			if (Math.abs(oldLocaltion[0] - locData.longitude) > this.maxDistance
					|| Math.abs(oldLocaltion[1] - locData.latitude) > this.maxDistance) {
				if (!isFirstLoc)
					searchByCircle();
				oldLocaltion[0] = locData.longitude;
				oldLocaltion[1] = locData.latitude;

			}
			if (isFirstLoc)
				// 临近范围定位
				searchByCircle();
			// 首次定位完成
			isFirstLoc = false;

		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	// private View.OnClickListener listener = new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// switch (v.getId()) {
	//
	// case R.id.left_button:
	// EditText keyWorldsView = (EditText) findViewById(R.id.search_input);
	//
	// mSearch.poiSearchInCity(city, keyWorldsView.getText()
	// .toString());
	// break;
	// case R.id.map_right_button:
	//
	// // Toast.makeText(MainActivity.this, "点击主页",
	// // Toast.LENGTH_LONG).show();
	// // Intent intent3 = new Intent(MainActivity.this,
	// // HomeActivity.class);
	// // startActivity(intent3);
	// // finish();
	//
	// break;
	// case R.id.Lie_Layout:
	//
	// /*
	// * Intent intent2 = new
	// * Intent(MainActivity.this,SearchActivity.class);
	// * intent2.putExtra("lon", (int) (locData.longitude * 1e6));
	// * intent2.putExtra("lat", (int) (locData.latitude * 1e6));
	// */
	//
	// Intent intent2 = new Intent(MainActivity.this,
	// LieActivity.class);
	//
	// startActivity(intent2);
	// finish();
	// break;
	// case R.id.Map_Layout:
	//
	// break;
	// default:
	// break;
	// }
	// }
	//
	// };

	public void createSearch() {
		WyyApplication app = (WyyApplication) this.getApplication();
		// 初始化搜索模块，注册搜索事件监听
		mSearch = new MKSearch();
		mSearch.init(app.mBMapManager, new MKSearchListener() {
			// 在此处理详情页结果
			@Override
			public void onGetPoiDetailSearchResult(int type, int error) {
				if (error != 0) {
					Toast.makeText(MapFoodsActivity.this, "抱歉，未找到结果",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MapFoodsActivity.this, "成功，查看详情页面",
							Toast.LENGTH_SHORT).show();
				}
			}

			/**
			 * 在此处理poi搜索结果
			 */
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// 错误号可参考MKEvent中的定义
				if (error != 0 || res == null) {
					Toast.makeText(MapFoodsActivity.this, "抱歉，未找到结果",
							Toast.LENGTH_LONG).show();
					return;
				}
				// 将地图移动到第一个POI中心点
				if (res.getCurrentNumPois() > 0) {
					// 将poi结果显示到地图上
					// MyPoiOverlay poiOverlay = new
					// MyPoiOverlay(SearchActivity.this, mMapView, mSearch);
					// poiOverlay.setData(res.getAllPoi());
					// mMapView.getOverlays().clear();
					// mMapView.getOverlays().add(poiOverlay);
					// mMapView.refresh();
					// 当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
					for (MKPoiInfo info : res.getAllPoi()) {
						if (info.pt != null) {
							// mMapView.getController().animateTo(info.pt);
							// Log.i(MainActivity.class.getName(),
							// info.pt.getLatitudeE6() + "经纬度 "
							// + info.pt.getLongitudeE6());
							// Intent intent = new Intent();
							// intent.setClass(MainActivity.this,
							// MainActivity.class);
							// intent.putExtra("lon", info.pt.getLongitudeE6());
							// intent.putExtra("lat", info.pt.getLatitudeE6());
							// startActivity(intent);
							// new
							// GeoPoint(info.pt.getLatitudeE6(),info.pt.getLongitudeE6());
							mMapView.getController().animateTo(
									new GeoPoint(info.pt.getLatitudeE6(),
											info.pt.getLongitudeE6()));
							return;
						}
					}
				} else if (res.getCityListNum() > 0) {
					// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
					String strInfo = "在";
					for (int i = 0; i < res.getCityListNum(); i++) {
						strInfo += res.getCityListInfo(i).city;
						strInfo += ",";
					}
					strInfo += "找到结果";
					Toast.makeText(MapFoodsActivity.this, strInfo,
							Toast.LENGTH_LONG).show();
				}
			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
			}

			public void onGetAddrResult(MKAddrInfo res, int error) {
				// 错误号可参考MKEvent中的定义
				if (error != 0 || res == null) {
					Toast.makeText(MapFoodsActivity.this, "抱歉，未找到结果",
							Toast.LENGTH_LONG).show();
					return;
				}
				MKGeocoderAddressComponent kk = res.addressComponents;
				city = kk.city;
				Toast.makeText(MapFoodsActivity.this, city, Toast.LENGTH_LONG)
						.show();
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

			/**
			 * 更新建议列表
			 */
			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
				if (res == null || res.getAllSuggestions() == null) {
					return;
				}
				sugAdapter.clear();
				for (MKSuggestionInfo info : res.getAllSuggestions()) {
					if (info.key != null)
						sugAdapter.add(info.key);
				}
				sugAdapter.notifyDataSetChanged();

			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
				// TODO Auto-generated method stub

			}
		});

		// keyWorldsView = (AutoCompleteTextView)
		// findViewById(R.id.search_input);
		// sugAdapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_dropdown_item_1line);
		// keyWorldsView.setAdapter(sugAdapter);
		//
		// /**
		// * 当输入关键字变化时，动态更新建议列表
		// */
		// keyWorldsView.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void afterTextChanged(Editable arg0) {
		//
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence arg0, int arg1,
		// int arg2, int arg3) {
		//
		// }
		//
		// @Override
		// public void onTextChanged(CharSequence cs, int arg1, int arg2,
		// int arg3) {
		// if (cs.length() <= 0) {
		// return;
		// }
		// // String city =
		// // ((EditText)findViewById(R.id.city)).getText().toString();
		// /**
		// * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
		// */
		// mSearch.suggestionSearch(cs.toString(), "");
		// }
		// });
		//
		// keyWorldsView.setOnKeyListener(new OnKeyListener() {//
		// 输入完后按键盘上的搜索键【回车键改为了搜索键】
		//
		// @Override
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		//
		// if (city == null) {
		// Toast.makeText(MapFoodsActivity.this, "没有定位到城市，无法查询",
		// Toast.LENGTH_LONG).show();
		// return false;
		// }
		//
		// // TODO Auto-generated method stub
		// if (keyCode == KeyEvent.KEYCODE_ENTER) {// 修改回车键功能
		// // 先隐藏键盘
		// ((InputMethodManager) keyWorldsView.getContext()
		// .getSystemService(
		// Context.INPUT_METHOD_SERVICE))
		// .hideSoftInputFromWindow(
		// MapFoodsActivity.this.getCurrentFocus()
		// .getWindowToken(),
		// InputMethodManager.HIDE_NOT_ALWAYS);
		//
		// EditText keyWorldsView = (EditText) findViewById(R.id.search_input);
		//
		// mSearch.poiSearchInCity(city, keyWorldsView
		// .getText().toString());
		//
		// return true;
		// }
		// return false;
		// }
		//
		// });

	}

	public void initCity() {

		mSearch.reverseGeocode(new GeoPoint((int) (locData.latitude * 1e6),
				(int) (locData.longitude * 1e6)));
	}

	// private void initFirst() {
	// // TODO Auto-generated method stub
	// if (!LoginActivity.firstYao) {
	// Intent intent = new Intent(MainActivity.this, Prompt.class);
	// intent.putExtra("first_map", "bing");
	// startActivity(intent);
	// LoginActivity.firstYao = true;
	//
	// }
	//
	// }

	private void settingSearchView() {
		ImageView icon = (ImageView) searchView
				.findViewById(android.support.v7.appcompat.R.id.search_button);
		icon.setImageResource(R.drawable.ic_search);
	}

}
