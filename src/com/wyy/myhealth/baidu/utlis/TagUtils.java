package com.wyy.myhealth.baidu.utlis;

import java.util.List;

import android.content.Context;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

/**
 * 百度标签设置类
 * 
 * @author lyl
 * 
 */
public class TagUtils {

	/**
	 * 设置标签
	 * 
	 * @param tag
	 */
	public static void setTag(String tag, Context context) {
		if (!Utils.hasBind(context)) {
			PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY,
					Utils.getMetaValue(context, "api_key"));
			// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
			PushManager.enableLbs(context);
		}
		List<String> tags = Utils.getTagsList(tag);
		PushManager.setTags(context, tags);

	}

}
