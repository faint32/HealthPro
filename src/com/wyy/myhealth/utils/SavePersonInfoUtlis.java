package com.wyy.myhealth.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

import com.wyy.myhealth.baidu.utlis.TagUtils;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.contants.ConstantS;

/**
 * 保存个人信息
 * 
 * @author lyl
 * 
 */
public class SavePersonInfoUtlis {
	/**
	 * 保存信息
	 * 
	 * @param info
	 * @param context
	 */
	public static void setPersonInfo(PersonalInfo info, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(ConstantS.USER_DATA,
				Context.MODE_PRIVATE);

		Editor editor = preferences.edit();
		// 存入数据
		editor.putBoolean("isLogin", true);
		editor.putString("id", info.getId());
		editor.putString("idcode", info.getIdcode());
		editor.putString("foodpic", info.getHeadimage());
		TagUtils.setTag(info.getId(), context);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream infoStream = new ObjectOutputStream(baos);
			infoStream.writeObject(info);
			String infobase64 = Base64.encodeToString(baos.toByteArray(),
					Base64.DEFAULT);
			editor.putString("personinfo", infobase64);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 提交修改
		editor.commit();
	
		persondataBackup(info, context);
		
	}
	
	
	public static void persondataBackup(PersonalInfo info,Context context){
		SharedPreferences preferences = context.getSharedPreferences(ConstantS.USER_BACK_UP,
				Context.MODE_PRIVATE);

		Editor editor = preferences.edit();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream infoStream = new ObjectOutputStream(baos);
			infoStream.writeObject(info);
			String infobase64 = Base64.encodeToString(baos.toByteArray(),
					Base64.DEFAULT);
			editor.putString("personinfo", infobase64);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 提交修改
		editor.commit();
	}

	
	/**
	 * 获取备份信息
	 */
	public static PersonalInfo getPersonInfo(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConstantS.USER_BACK_UP, Context.MODE_PRIVATE);
		PersonalInfo personalInfo =null;
		String personstr = sharedPreferences.getString("personinfo", "");
		if (!TextUtils.isEmpty(personstr)) {
			byte[] bytepersonbase64 = Base64.decode(personstr.getBytes(),
					Base64.DEFAULT);
			ByteArrayInputStream bis = new ByteArrayInputStream(
					bytepersonbase64);
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(bis);
				try {
					personalInfo = (PersonalInfo) ois.readObject();
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		return personalInfo;
		
	}
	
	
}
