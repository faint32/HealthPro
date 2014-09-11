package com.wyy.myhealth.ui.personcenter.modify;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.JsonHttpResponseHandler;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.NoticeUtils;
import com.wyy.myhealth.utils.SavePersonInfoUtlis;

public class ModifyHandler extends JsonHttpResponseHandler {

	private Activity context;
	
	public ModifyHandler (Activity context){
		this.context=context;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		NoticeUtils.notice(context,
				context.getResources().getString(R.string.send),
				ConstantS.PUBLISH_COMMENT);
		context.finish();
	}


	@Override
	public void onSuccess(JSONObject response) {
		// TODO Auto-generated method stub
		try {

			BingLog.i(ModifyHandler.class.getSimpleName(), "·µ»Ø:"+response);
			
			if (response.has("user")) {
				JSONObject object = response.getJSONObject("user");
				PersonalInfo info = JsonUtils.getInfo(object);
				if (info != null) {
					// setForName(info);
					WyyApplication.setInfo(info);
					
					SavePersonInfoUtlis.setPersonInfo(info,
							context);

					NoticeUtils.removeNotice(ConstantS.PUBLISH_COMMENT, context);
					NoticeUtils.showSuccessfulNotification(context);
					sendModifyBrocast(context);
				}
			} else {
				NoticeUtils.removeNotice(ConstantS.PUBLISH_COMMENT, context);
				NoticeUtils.showFailePublish(context);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onSuccess(response);
	}

	@Override
	public void onFinish() {
		super.onFinish();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onFailure(Throwable error) {
		// TODO Auto-generated method stub
		super.onFailure(error);
		NoticeUtils.removeNotice(ConstantS.PUBLISH_COMMENT, context);
		NoticeUtils.showFailePublish(context);
	}


	private final void sendModifyBrocast(Context context){
		Intent intent=new Intent();
		intent.setAction(ConstantS.ACTION_BASE_INFO_CHANGE);
		context.sendBroadcast(intent);
	}
	
}
