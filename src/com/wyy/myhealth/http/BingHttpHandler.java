package com.wyy.myhealth.http;

import org.json.JSONObject;

public abstract class BingHttpHandler extends JsonHttpResponseHandler {

	@Override
	public void onSuccess(JSONObject response) {
		// TODO Auto-generated method stub
		super.onSuccess(response);
		onGetSuccess(response);
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		super.onFinish();
		onGetFinish();
	}

	protected abstract void onGetSuccess(JSONObject response);

	protected abstract void onGetFinish();

}
