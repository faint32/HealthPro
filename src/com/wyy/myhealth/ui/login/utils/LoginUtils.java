package com.wyy.myhealth.ui.login.utils;

import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.utils.SavePersonInfoUtlis;

public class LoginUtils {

	public static void login(final Context context,String idcode){
		 AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				parseJson(content, context);
			}

			public void onFailure(Throwable error, String content) {
				Toast.makeText(context, R.string.loginfailure, Toast.LENGTH_LONG)
						.show();
			};

		};
		
		HealthHttpClient.doHttpLogin(idcode, handler);
		
	}
	
	
	private static void parseJson(String content,Context context){
		try {
			JSONObject jsonObject=new JSONObject(content);
			if (JsonUtils.isSuccess(jsonObject)) {
				JSONObject object = jsonObject
						.getJSONObject("user");
				PersonalInfo info = JsonUtils
						.getInfo(object);
				if (null!=info) {
					WyyApplication.setInfo(info);
					SavePersonInfoUtlis.setPersonInfo(info, context);
				}
				
			}else {
				Toast.makeText(context, R.string.loginfailure, Toast.LENGTH_LONG)
				.show();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(context, R.string.loginfailure, Toast.LENGTH_LONG)
			.show();
		}
	}
	
}
