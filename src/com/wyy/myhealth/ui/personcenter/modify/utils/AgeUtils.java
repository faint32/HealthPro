package com.wyy.myhealth.ui.personcenter.modify.utils;

public class AgeUtils {

	public static boolean isAge(String content) {
		boolean isage=false;
		if (HwUtlis.isNum(content)) {
			try {
				int age=Integer.valueOf(content);
				if (age>0&&age<100) {
					isage=true;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		return isage;
		
	}
	
}
