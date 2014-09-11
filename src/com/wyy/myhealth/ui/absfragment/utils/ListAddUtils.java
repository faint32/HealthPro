package com.wyy.myhealth.ui.absfragment.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wyy.myhealth.bean.MoodaFoodBean;
import com.wyy.myhealth.bean.NearFoodBean;
import com.wyy.myhealth.utils.BingLog;

public class ListAddUtils {

	private static final String TAG = "ListAddUtils";

	/**
	 * 合并list
	 * 
	 * @param tempshaiList
	 * @param shaiList
	 */
	public static void compleAMearge(List<Map<String, Object>> tempshaiList,
			List<Map<String, Object>> shaiList) {

		int maxindex = 0;
		int locationdex = 0;
		for (int i = 0; i < tempshaiList.size(); i++) {
			for (int j = 0; j < shaiList.size(); j++) {
				if (maxindex == 0) {
					BingLog.i(TAG,
							"不同" + "新时间:" + tempshaiList.get(i).get("time")
									+ ":旧时间:" + shaiList.get(j).get("time"));
				}

				if (tempshaiList.get(i).get("time")
						.equals(shaiList.get(j).get("time"))) {
					maxindex = i;
					locationdex = j;
					// break;
					i = tempshaiList.size();
					j = shaiList.size();
					BingLog.i(TAG, "不同处:" + maxindex);
				}

			}
		}

		// Log.i(TAG, "不同处:"+maxindex);

		if (maxindex > 0) {
			for (int i = maxindex; i < tempshaiList.size()
					&& locationdex < shaiList.size(); i++) {
				shaiList.remove(locationdex);
				shaiList.add(locationdex, tempshaiList.get(i));
				locationdex++;
			}
		}

		if (maxindex > 0) {
			for (int i = 0; i < maxindex; i++) {
				shaiList.add(tempshaiList.get(i));
			}
		} else {
			for (int i = maxindex; i < tempshaiList.size()
					&& locationdex < shaiList.size(); i++) {
				shaiList.remove(locationdex);
				shaiList.add(locationdex, tempshaiList.get(i));
				locationdex++;
			}
		}

	}

	/**
	 * 合并list
	 * 
	 * @param tempshaiList
	 * @param shaiList
	 */
	public synchronized static void compleAMearge2(List<MoodaFoodBean> tempshaiList,
			List<MoodaFoodBean> thList) {

		MoodaFoodBean mBean = thList.get(0);
		int length = tempshaiList.size();

		List<MoodaFoodBean> list = new ArrayList<>();

		for (int i = 0; i < length; i++) {
			if (mBean.getCreatetime().equals(
					tempshaiList.get(i).getCreatetime())) {
				thList.remove(0);
				thList.add(0, tempshaiList.get(i));
				break;
			} else {
				list.add(tempshaiList.get(i));
			}
		}

		thList.addAll(0, list);

		// int maxindex = 0;
		// int locationdex = 0;
		// for (int i = 0; i < tempshaiList.size(); i++) {
		// for (int j = 0; j < thList.size(); j++) {
		// if (maxindex == 0) {
		// BingLog.i(TAG, "不同" + "新时间:"
		// + tempshaiList.get(i).getCreatetime() + ":旧时间:"
		// + thList.get(j).getCreatetime());
		// }
		//
		// if (tempshaiList.get(i).getCreatetime()
		// .equals(thList.get(j).getCreatetime())) {
		// maxindex = i;
		// locationdex = j;
		// i = tempshaiList.size();
		// j = thList.size();
		// BingLog.i(TAG, "不同处:" + maxindex);
		// break;
		//
		// }
		//
		// }
		// }
		//
		// // Log.i(TAG, "不同处:"+maxindex);
		//
		// if (maxindex > 0) {
		// for (int i = maxindex; i < tempshaiList.size()
		// && locationdex < thList.size(); i++) {
		// thList.remove(locationdex);
		// thList.add(locationdex, tempshaiList.get(i));
		// locationdex++;
		// }
		// }
		//
		// if (maxindex > 0) {
		// for (int i = 0; i < maxindex; i++) {
		// thList.add(tempshaiList.get(i));
		// }
		// } else {
		// for (int i = maxindex; i < tempshaiList.size()
		// && locationdex < thList.size(); i++) {
		// thList.remove(locationdex);
		// thList.add(locationdex, tempshaiList.get(i));
		// locationdex++;
		// }
		// }

	}

	/**
	 * 合并list
	 * 
	 * @param tempshaiList
	 * @param thList
	 */
	public static void compleANearMearge(List<NearFoodBean> tempshaiList,
			List<NearFoodBean> shaiList) {

		int maxindex = 0;
		int locationdex = 0;
		for (int i = 0; i < tempshaiList.size(); i++) {
			for (int j = 0; j < shaiList.size(); j++) {
				if (maxindex == 0) {
					BingLog.i(TAG, "不同" + "新时间:" + tempshaiList.get(i).getId()
							+ ":旧时间:" + shaiList.get(j).getId());
				}

				if (tempshaiList.get(i).getId().equals(shaiList.get(j).getId())) {
					maxindex = i;
					locationdex = j;
					// break;
					i = tempshaiList.size();
					j = shaiList.size();
					BingLog.i(TAG, "不同处:" + maxindex);
				}

			}
		}

		// Log.i(TAG, "不同处:"+maxindex);

		if (maxindex > 0) {
			for (int i = maxindex; i < tempshaiList.size()
					&& locationdex < shaiList.size(); i++) {
				shaiList.remove(locationdex);
				shaiList.add(locationdex, tempshaiList.get(i));
				locationdex++;
			}
		}

		if (maxindex > 0) {
			for (int i = 0; i < maxindex; i++) {
				shaiList.add(tempshaiList.get(i));
			}
		} else {
			for (int i = maxindex; i < tempshaiList.size()
					&& locationdex < shaiList.size(); i++) {
				shaiList.remove(locationdex);
				shaiList.add(locationdex, tempshaiList.get(i));
				locationdex++;
			}
		}

	}

}
