package com.wyy.myhealth.ui.personcenter;

import android.util.Log;

import com.wyy.myhealth.utils.DistanceUtils;

public class PersonUtils {

	public static double getBmi(String height,String weight){
		double bmi=0;
		double dheight=0;
		double dweight=0;
		Log.i("管理中心", "高:"+height+"体重:"+weight);
		try {
			dheight=Double.valueOf(height);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
		
		try {
			dweight=Double.valueOf(weight);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
		
		Log.i("管理中心", "高:"+dheight+"体重:"+dweight);
		
		try {
			bmi=(dweight/((dheight/100)*(dheight/100)));
			bmi=DistanceUtils.changep2(bmi);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return bmi;
	}
	
}
