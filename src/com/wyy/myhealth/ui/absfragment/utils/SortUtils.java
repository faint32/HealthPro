package com.wyy.myhealth.ui.absfragment.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
/**
 * 排序工具
 * @author lyl
 *
 */
public class SortUtils {

	/**
	 * 根据时间排序
	 * 
	 * @param list
	 */
	public static void bingSort(List<Map<String, Object>> list) {
		if (!list.isEmpty()) {
			Collections.sort(list, new Comparator<Map<String, Object>>() {

				@Override
				public int compare(Map<String, Object> lhs,
						Map<String, Object> rhs) {
					// TODO Auto-generated method stub
					return rhs.get("time").toString()
							.compareTo(lhs.get("time").toString());
				}
			});
		}
	}

}
