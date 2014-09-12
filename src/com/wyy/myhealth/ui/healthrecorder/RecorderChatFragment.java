package com.wyy.myhealth.ui.healthrecorder;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.wyy.myhealth.HealthReActivity;
import com.wyy.myhealth.R;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.imag.utils.SavePic;
import com.wyy.myhealth.service.MainService;
import com.wyy.myhealth.utils.BingDateUtils;
import com.wyy.myhealth.utils.BingLog;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class RecorderChatFragment extends Fragment {

	private static final String TAG = RecorderChatFragment.class
			.getSimpleName();

	private FrameLayout wrapper;

	private static final int TYPE_SIZE = 2;

	private int re_type = ConstantS.YINSHI;

	public static RecorderChatFragment newInstance(int id) {
		RecorderChatFragment recorderChatFragment = new RecorderChatFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(ConstantS.RECEODER, id);
		recorderChatFragment.setArguments(bundle);
		return recorderChatFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		re_type = getArguments().getInt(ConstantS.RECEODER, ConstantS.YINSHI);
		if (savedInstanceState != null) {
			re_type = savedInstanceState.getInt(ConstantS.RECEODER);
		}

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		((HealthReActivity) activity).onSectionAttached(getArguments().getInt(
				ConstantS.RECEODER));
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		BingLog.i(TAG, "============onDetach============");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.wrapper, container, false);
		wrapper = (FrameLayout) rootView.findViewById(R.id.wrapper);
		wrapper.addView(getChatView());
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			re_type = savedInstanceState.getInt(ConstantS.RECEODER);
			BingLog.i(TAG, "============onActivityCreated========re_type===="
					+ re_type);
		}

		wrapper.addView(getChatView());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt(ConstantS.RECEODER, re_type);
	}

	private XYMultipleSeriesDataset getBarDemoDataset() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int nr = MainService.getThHealthRecoderBeans().size();
		if (re_type == ConstantS.YUNDONG) {
			nr = MainService.getSports().size();
		}
		for (int i = 0; i < TYPE_SIZE; i++) {
			CategorySeries series = new CategorySeries("Demo series " + (i + 1));
			// TimeSeries series2 = new TimeSeries(TAG);
			if (i == 0) {
				series = new CategorySeries(getString(R.string.shijiajilu));
				// series2 = new TimeSeries(TAG);
			} else {
				series = new CategorySeries(getString(R.string.jianyijilu));
				// series2 = new TimeSeries(TAG + "jjjjjjj");
			}
			for (int k = 0; k < nr; k++) {
				try {
					if (i == 0) {
						series.add(getThReceoder(k));
						// series2.add(
						// new Date(BingDateUtils.getTime(MainService
						// .getThHealthRecoderBeans().get(i)
						// .getCreatetime())), getThReceoder(k));
					} else {
						series.add(getNeReceoder(k));
						// series2.add(
						// new Date(BingDateUtils.getTime(MainService
						// .getNextHealthRecoderBeans().get(i)
						// .getCreatetime())), getNeReceoder(k));
					}

				} catch (Exception e) {
					// TODO: handle exception
					series.add(0);
					// series2.add(
					// new Date(BingDateUtils.getTime(MainService
					// .getThHealthRecoderBeans().get(i)
					// .getCreatetime())), 5);
				}

			}
			dataset.addSeries(series.toXYSeries());
			// dataset.addSeries(series2);
		}
		return dataset;
	}

	private void setChartSettings(XYMultipleSeriesRenderer renderer) {
		// renderer.setChartTitle(getString(R.string.health_recoder));
		// renderer.setChartTitleTextSize(50f);
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.GRAY);
		renderer.setMarginsColor(Color.GRAY);
		renderer.setXTitle(getString(R.string.x_date));
		renderer.setYTitle(getString(R.string.y_values));
		renderer.setPanEnabled(true, false);

		if (re_type == ConstantS.YUNDONG) {
			try {
				renderer.setXAxisMin(MainService.getSports().size() - 10);
			} catch (Exception e) {
				// TODO: handle exception
				renderer.setXAxisMin(0.5);
			}

			renderer.setXAxisMax(MainService.getSports().size());

		} else {
			try {
				renderer.setXAxisMin(MainService.getThHealthRecoderBeans()
						.size() - 10);
			} catch (Exception e) {
				// TODO: handle exception
				renderer.setXAxisMin(0.5);
			}

			renderer.setXAxisMax(MainService.getThHealthRecoderBeans().size());
		}
		try {
			int length = MainService.getThHealthRecoderBeans().size();
			for (int i = 0; i < length; i = i + 5) {
				renderer.addXTextLabel(
						i,
						BingDateUtils.changeDate(MainService
								.getThHealthRecoderBeans().get(i)
								.getCreatetime()));
			}
			renderer.setXLabels(0);

		} catch (Exception e) {
			// TODO: handle exception
		}
		renderer.setXLabelsPadding(60f);
		// BingLog.i(TAG, "最大数:"+series.getMaxX());
		// renderer.setXAxisMin(series.getMaxX() - 100000);
		// renderer.setXAxisMax(series.getMaxX());
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(5);
	}

	public XYMultipleSeriesRenderer getBarDemoRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(20);
		renderer.setLegendTextSize(20);
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		r.setColor(Color.BLUE);
		renderer.addSeriesRenderer(r);
		r = new SimpleSeriesRenderer();
		r.setColor(Color.GREEN);
		renderer.addSeriesRenderer(r);
		return renderer;
	}

	private XYMultipleSeriesRenderer getDemoRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(20);
		renderer.setLegendTextSize(20);
		renderer.setPointSize(10f);
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.WHITE);
		r.setPointStyle(PointStyle.TRIANGLE);
		// r.setFillBelowLine(true);
		// r.setFillBelowLineColor(Color.WHITE);
		r.setFillPoints(true);
		renderer.addSeriesRenderer(r);
		r = new XYSeriesRenderer();
		r.setPointStyle(PointStyle.CIRCLE);
		r.setColor(Color.GREEN);
		r.setFillPoints(true);
		renderer.addSeriesRenderer(r);
		renderer.setAxesColor(Color.WHITE);
		renderer.setLabelsColor(Color.WHITE);
		return renderer;
	}

	private View getChatView() {
		XYMultipleSeriesRenderer renderer = getDemoRenderer();
		setChartSettings(renderer);
		// return ChartFactory.getBarChartView(getActivity(),
		// getBarDemoDataset(),
		// renderer, Type.DEFAULT);
		GraphicalView graphicalView = ChartFactory.getLineChartView(
				getActivity(), getBarDemoDataset(), renderer);
		// GraphicalView graphicalView = ChartFactory.getTimeChartView(
		// getActivity(), getBarDemoDataset(), renderer, null);

		try {
			graphicalView.repaint();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return graphicalView;

	}

	private double getThReceoder(int index) {
		double v = 0;
		switch (re_type) {
		case ConstantS.YINSHI:
			try {
				v = Double.valueOf(MainService.getThHealthRecoderBeans()
						.get(index).getReasonable());
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;

		case ConstantS.YUNDONG:
			try {
				v = Double.valueOf(MainService.getSports().get(index));
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;

		case ConstantS.ZHIFANG:
			try {
				v = Double.valueOf(MainService.getThHealthRecoderBeans()
						.get(index).getFat());
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;

		case ConstantS.TANGLEI:

			try {
				v = Double.valueOf(MainService.getThHealthRecoderBeans()
						.get(index).getSugar());
			} catch (Exception e) {
				// TODO: handle exception
			}

			break;

		case ConstantS.DANGBAIZHI:

			try {
				v = Double.valueOf(MainService.getThHealthRecoderBeans()
						.get(index).getProtein());
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;

		case ConstantS.WEISHENGSU:

			try {
				v = Double.valueOf(MainService.getThHealthRecoderBeans()
						.get(index).getVitamin());
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;

		case ConstantS.KUANGWUZHI:

			try {
				v = Double.valueOf(MainService.getThHealthRecoderBeans()
						.get(index).getMineral());
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;

		default:
			break;
		}

		return v;

	}

	private double getNeReceoder(int index) {
		double v = 0;
		switch (re_type) {
		case ConstantS.YINSHI:
			try {
				v = Double.valueOf(MainService.getNextHealthRecoderBeans()
						.get(index).getReasonable());
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;

		case ConstantS.YUNDONG:

			break;

		case ConstantS.ZHIFANG:
			try {
				v = Double.valueOf(MainService.getNextHealthRecoderBeans()
						.get(index).getFat());
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;

		case ConstantS.TANGLEI:

			try {
				v = Double.valueOf(MainService.getNextHealthRecoderBeans()
						.get(index).getSugar());
			} catch (Exception e) {
				// TODO: handle exception
			}

			break;

		case ConstantS.DANGBAIZHI:

			try {
				v = Double.valueOf(MainService.getNextHealthRecoderBeans()
						.get(index).getProtein());
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;

		case ConstantS.WEISHENGSU:

			try {
				v = Double.valueOf(MainService.getNextHealthRecoderBeans()
						.get(index).getVitamin());
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;

		case ConstantS.KUANGWUZHI:

			try {
				v = Double.valueOf(MainService.getNextHealthRecoderBeans()
						.get(index).getMineral());
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;

		default:
			break;
		}

		return v;

	}

	private void saveScreen(final View graphicalView) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					graphicalView.setDrawingCacheEnabled(true);
					SavePic.saveRecoredPic2Example(graphicalView
							.getDrawingCache());
					graphicalView.setDrawingCacheEnabled(false);
				} catch (Exception e) {
					// TODO: handle exception
					BingLog.e(TAG, "保存错误:" + e.getMessage());
				}

			}

		}.start();
	}

}
