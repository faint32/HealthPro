package com.wyy.myhealth.ui.personcenter.modify.utils;

/**
 * 身高体重工具
 * 
 * @author lyl
 * 
 */
public class HwUtlis {

	/**
	 * 判断体重是否正常
	 * 
	 * @param content
	 *            体重
	 * @return 返回true 表示正常，否则不正常
	 */
	public static boolean isWeight(String content) {
		boolean isweight = false;

		if (isNum(content)) {
			double weight = Double.valueOf(content);
			if (weight > 0 && weight < 200) {
				isweight = true;
			}
		}

		return isweight;
	}

	/**
	 * 判断身高是否正常
	 * 
	 * @param content
	 *            身高
	 * @return 返回true 表示正常，否则不正常
	 */
	public static boolean isHeight(String content) {
		boolean isheight = false;

		if (isNum(content)) {
			double height = Double.valueOf(content);
			if (height > 0 && height < 300) {
				isheight = true;
			}
		}

		return isheight;
	}

	/**
	 * 判断字符串是否位数字
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isNum(String content) {

		boolean isnum = false;
		try {
			Double.valueOf(content);
			isnum = true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return isnum;
	}

}
