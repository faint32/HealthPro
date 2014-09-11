package com.wyy.myhealth.ui.icebox.utils;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;

public class FoodTypeUtils {

	private static final String FRUIT = WyyApplication.getInstance().getString(
			R.string.fruit);

	private static final String VEGETABLES = WyyApplication.getInstance()
			.getString(R.string.vgetablis);

	private static final String MEATE = WyyApplication.getInstance().getString(
			R.string.meat);

	private static final String STABLE_FOOD = WyyApplication.getInstance()
			.getString(R.string.stable_food);

	private static final String OTHER = WyyApplication.getInstance().getString(
			R.string.other);

	public static String getfoodtype(int type) {
		String foodtype = "";
		switch (type) {
		case 2:
			foodtype = FRUIT;
			break;

		case 1:
			foodtype = VEGETABLES;
			break;

		case 3:
			foodtype = MEATE;
			break;

		case 4:
			foodtype = STABLE_FOOD;
			break;

		case 5:
			foodtype = OTHER;
			break;

		default:
			foodtype = OTHER;
			break;
		}

		return foodtype;

	}

}
