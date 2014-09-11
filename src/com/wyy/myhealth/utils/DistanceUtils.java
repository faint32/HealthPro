package com.wyy.myhealth.utils;

import java.math.BigDecimal;

/**
 * 计算距离工具类
 * @author lyl
 *
 */
public class DistanceUtils {
	/**
	 * 计算距离方法
	 * @param lng1 经度0
	 * @param lat1 纬度0
	 * @param lng2 经度1
	 * @param lat2 纬度1
	 * @return
	 */
	public static double getDistance(double lng1, double lat1, double lng2, double lat2)
	{
		double radLat1 = lat1 * 0.0174532925199433D;
	    double radLat2 = lat2 * 0.0174532925199433D;
	    double a = radLat1 - radLat2;
	    double b = (lng1 - lng2) * 0.0174532925199433D;
	    double s = 2.0D * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2.0D), 2.0D) + 
	    		Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2.0D), 2.0D)));
	    s *= 6378137.0D;
	    s = Math.round(s * 10000.0D) / 10000L;
	    return s;
	  }
	
	public static double changep2(double i){
		BigDecimal b = new BigDecimal(i);
		 int saveBitNum = 2;
		 double c = b.setScale(saveBitNum , BigDecimal.ROUND_HALF_UP).doubleValue();
		 return c;
	}
	
	
	
	/**
	 * 计算距离方法
	 * @param lng1 经度0
	 * @param lat1 纬度0
	 * @param lng2 经度1
	 * @param lat2 纬度1
	 * @return
	 */
	public static double getDistance(double lng1, double lat1, String lng2, String lat2)
	{
		
		double lat22=0;
		double lng22=0;
		
		try {
			lng22=Double.valueOf(lng2);
			lat22=Double.valueOf(lat2);
		} catch (Exception e) {
			// TODO: handle exception
			return -1;
		}
		
		double radLat1 = lat1 * 0.0174532925199433D;
	    double radLat2 = lat22 * 0.0174532925199433D;
	    double a = radLat1 - radLat2;
	    double b = (lng1 - lng22) * 0.0174532925199433D;
	    double s = 2.0D * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2.0D), 2.0D) + 
	    		Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2.0D), 2.0D)));
	    s *= 6378137.0D;
	    s = Math.round(s * 10000.0D) / 10000L;
	    
	    s=changep2(s/1000);
	    
	    return s;
	  }
	
	
	
}
