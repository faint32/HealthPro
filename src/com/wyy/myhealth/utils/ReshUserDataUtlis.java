package com.wyy.myhealth.utils;

import com.wyy.myhealth.contants.ConstantS;

import android.content.Context;
import android.content.Intent;

public class ReshUserDataUtlis {

	public static void reshUserRecorder(Context context){
		context.sendBroadcast(new Intent(ConstantS.ACTION_RESH_USER_DATA));
	}
	
}
