package com.wyy.myhealth.receiver;

import java.util.Calendar;

import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.stepcount.StepDetector;
import com.wyy.myhealth.stepcount.StepService;
import com.wyy.myhealth.utils.BingLog;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PostFootReceiver extends BroadcastReceiver {
	private boolean postflag = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (StepService.total_step != 0) {
			if (null != WyyApplication.getInfo()) {
				HealthHttpClient.doHttpPostFoot( WyyApplication.getInfo()
						.getId(), "" + StepService.total_step,
						postFootResponseHandler);
			} else if (StepService.userID != null) {
				HealthHttpClient.doHttpPostFoot(StepService.userID, ""
						+ StepService.total_step, postFootResponseHandler);
			}

		}
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.HOUR_OF_DAY) == 7
				|| calendar.get(Calendar.HOUR_OF_DAY) == 11
				|| calendar.get(Calendar.HOUR_OF_DAY) == 20) {
			if (postflag==false) {
				
			}else {
				postflag=false;
				new Thread(postRunnable).start();
			}
			
		}
	}

	private AsyncHttpResponseHandler postFootResponseHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			StepService.total_step = 0;
			StepDetector.CURRENT_SETP = 0;
			postflag=true;
			// Log.i("PostFootReceiver", content);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			BingLog.i("PostFootReceiver", content);
		}

	};

	private Runnable postRunnable = new Runnable() {

		
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (!postflag) {
//				bing=true;
				if (StepService.total_step != 0) {
					if (null !=  WyyApplication.getInfo()) {
						HealthHttpClient.doHttpPostFoot( WyyApplication.getInfo()
								.getId(), "" + StepService.total_step,
								postFootResponseHandler);
					} else if (StepService.userID != null) {
						HealthHttpClient.doHttpPostFoot(StepService.userID, ""
								+ StepService.total_step, postFootResponseHandler);
					}

				}
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	};
	
}
