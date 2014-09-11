package com.wyy.myhealth.ui.shaiyishai.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.wyy.myhealth.bean.ListDataBead;
import com.wyy.myhealth.bean.MoodaFoodBean;
import com.wyy.myhealth.db.utils.ShaiDatebaseUtils;
import com.wyy.myhealth.http.utils.JsonUtils;

public class ShaiUtility {

	@SuppressWarnings("unchecked")
	public static String getShaiJsonTime(Context context) {
		List<ListDataBead> lastDataBeads = new ArrayList<ListDataBead>();
		lastDataBeads = (List<ListDataBead>) new ShaiDatebaseUtils(context)
				.queryData();
		String json = "";
		if (null != lastDataBeads) {
			if (lastDataBeads.size() > 0) {
				json = lastDataBeads.get(0).getJsondata();
			}

		}
		String time = "";
		try {
			time = reshParseJson(new JSONObject(json));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;

	}

	private static String reshParseJson(JSONObject response) {
		if (JsonUtils.isSuccess(response)) {
			try {
				JSONArray array = response.getJSONArray("foods");
				if (array.length() > 0) {
					MoodaFoodBean moodaFoodBean = JsonUtils
							.getMoodaFoodBean(array.getJSONObject(0));
					return moodaFoodBean.getCreatetime();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "";

	}

	public static MoodaFoodBean getParseInfo(JSONObject response) {
		if (JsonUtils.isSuccess(response)) {
			try {
				JSONArray array = response.getJSONArray("foods");
				if (array.length() > 0) {
					MoodaFoodBean moodaFoodBean = JsonUtils
							.getMoodaFoodBean(array.getJSONObject(0));
					return moodaFoodBean;
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;

	}

}
