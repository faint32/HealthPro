package com.wyy.myhealth.utils;



import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.ui.healthbar.HealthPassActivity;
import com.wyy.myhealth.ui.message.MessageTListActivity;
import com.wyy.myhealth.welcome.WelcomeActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

public class NoticeUtils {

	private static NotificationManager notificationManager;

	private static Handler handler = new Handler();

	/**
	 * 新通知
	 * 
	 * @param context
	 *            上下文
	 * @param message
	 *            消息
	 */
	@SuppressWarnings("deprecation")
	public static void notice(Context context, String message, int id) {
		if (null == notificationManager) {
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}

		PendingIntent pendingIntent = null;

		Notification notification = null;

		switch (id) {
		case ConstantS.NEW_LOGIN_ACTION_ID:

			if (null == WyyApplication.getInfo()) {
				pendingIntent = PendingIntent.getActivity(context, 0,
						new Intent(context, WelcomeActivity.class),
						Intent.FLAG_ACTIVITY_NEW_TASK);
			} else {

				pendingIntent = PendingIntent.getActivity(context, 0,
						new Intent(context, MessageTListActivity.class),
						Intent.FLAG_ACTIVITY_NEW_TASK);
			}

			notification = new Notification(R.drawable.logo, "" + message,
					System.currentTimeMillis());
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.defaults = Notification.DEFAULT_SOUND;
			notification.setLatestEventInfo(context, "微营养", message,
					pendingIntent);
			notificationManager.notify(ConstantS.NEW_LOGIN_ACTION, id,
					notification);
			break;

		case ConstantS.NEW_FOOD_COMMENT_ID:
			if (null == WyyApplication.getInfo()) {
				pendingIntent = PendingIntent.getActivity(context, 0,
						new Intent(context, WelcomeActivity.class),
						Intent.FLAG_ACTIVITY_NEW_TASK);
			} else {
				pendingIntent = PendingIntent.getActivity(context, 0,
						new Intent(context, HealthPassActivity.class),
						Intent.FLAG_ACTIVITY_NEW_TASK);
			}

			notification = new Notification(R.drawable.logo, "" + message,
					System.currentTimeMillis());
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.defaults = Notification.DEFAULT_SOUND;
			notification.setLatestEventInfo(context, context.getString(R.string.app_name), message,
					pendingIntent);
			notificationManager.notify(ConstantS.NEW_FOOD_COMMENT, id,
					notification);
			context.sendBroadcast(new Intent(ConstantS.NEW_FOOD_COMMENT));
			break;

		case ConstantS.PUBLISH_MOOD_ID:

			notification = new NotificationCompat.Builder(context)

			.setTicker(context.getString(R.string.sending))
					.setContentTitle(context.getString(R.string.sending))
					.setContentText(message).setContentIntent(pendingIntent)
					.setOnlyAlertOnce(true).setOngoing(true)
					.setSmallIcon(R.drawable.upload_white).build();
			notificationManager.notify(id, notification);
			break;

		case ConstantS.PUBLISH_SHAI_ID:

			notification = new NotificationCompat.Builder(context)

			.setTicker(context.getString(R.string.sending))
					.setContentTitle(context.getString(R.string.sending))
					.setContentText(message).setContentIntent(pendingIntent)
					.setOnlyAlertOnce(true).setOngoing(true)
					.setSmallIcon(R.drawable.upload_white).build();
			notificationManager.notify(id, notification);
			break;
			
		case ConstantS.PUBLISH_FOOD_ID:

			notification = new NotificationCompat.Builder(context)

			.setTicker(context.getString(R.string.sending))
					.setContentTitle(context.getString(R.string.sending))
					.setContentText(message).setContentIntent(pendingIntent)
					.setOnlyAlertOnce(true).setOngoing(true)
					.setSmallIcon(R.drawable.upload_white).build();
			notificationManager.notify(id, notification);
			break;
			
			
		case ConstantS.PUBLISH_COMMENT:
			notification = new NotificationCompat.Builder(context)

			.setTicker(context.getString(R.string.sending))
					.setContentTitle(context.getString(R.string.sending))
					.setContentText(message).setContentIntent(pendingIntent)
					.setOnlyAlertOnce(true).setOngoing(true)
					.setSmallIcon(R.drawable.upload_white).build();
			notificationManager.notify(id, notification);
			break;
			
		default:
			break;
		}


	}

	public static void showSuccessfulNotification(Context context) {
		if (null == notificationManager) {
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
				.setTicker(context.getString(R.string.send_successfully))
				.setContentTitle(context.getString(R.string.send_successfully))
				.setOnlyAlertOnce(true).setAutoCancel(true)
				.setSmallIcon(R.drawable.send_successfully).setOngoing(false);
		Notification notification = builder.build();
		notificationManager.notify(100, notification);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				notificationManager.cancel(100);
			}
		}, 3000);
	}

	
	public static void showProgressPublish(Context context,int progress,int max,int id){
		if (null == notificationManager) {
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
        .setTicker(context.getString(R.string.send_photo))
        .setContentTitle(context.getString(R.string.send_photo))
        .setOnlyAlertOnce(true)
        .setAutoCancel(true)
        .setSmallIcon(R.drawable.upload_white)
        .setProgress(max, progress, false)
        .setOngoing(false);
		 Notification notification = builder.build();
		notificationManager.notify(id, notification);
		
		
	}
	
	public static void showFailePublish(Context context){
		
		if (null == notificationManager) {
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
         .setTicker(context.getString(R.string.send_failed))
         .setContentTitle(context.getString(R.string.send_failed))
         .setOnlyAlertOnce(true)
         .setAutoCancel(true)
         .setSmallIcon(R.drawable.send_failed)
         .setOngoing(false);
		 
		 Notification notification = builder.build();
			notificationManager.notify(100, notification);
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					notificationManager.cancel(100);
				}
			}, 3000);
		 
	}
	
	
	public static void removeNotice(int id,Context context){
		if (null == notificationManager) {
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		
		notificationManager.cancel(id);
	}
	
}
