package com.wyy.myhealth.ui.icebox;

import android.app.Activity;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.IceBoxFoodBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.db.utils.IceDadabaseUtils;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.NoticeUtils;

public class IceHttpHander extends AsyncHttpResponseHandler {

	private Activity context;

	public IceHttpHander(Activity context) {
		this.context = context;
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
	public void onFinish() {
		// TODO Auto-generated method stub
		super.onFinish();
	}

	@Override
	public void onSuccess(String content) {
		// TODO Auto-generated method stub
		super.onSuccess(content);
		BingLog.i(IceHttpHander.class.getSimpleName(), "上传返回:" + content);
		IceBoxFoodBean iceBoxFoodBean = JsonUtils.getIceBox4ProFoodBean(content);
		BingLog.i(IceHttpHander.class.getSimpleName(), "名称:" + iceBoxFoodBean.getName());
		new IceDadabaseUtils(context).insert(iceBoxFoodBean);
		NoticeUtils.removeNotice(ConstantS.PUBLISH_COMMENT, context);
		NoticeUtils.showSuccessfulNotification(context);
	}

	@Override
	public void onFailure(Throwable error, String content) {
		// TODO Auto-generated method stub
		super.onFailure(error, content);
		NoticeUtils.removeNotice(ConstantS.PUBLISH_COMMENT, context);
		NoticeUtils.showFailePublish(context);
	}

}
