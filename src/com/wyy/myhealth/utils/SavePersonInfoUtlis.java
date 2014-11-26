package com.wyy.myhealth.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.contants.ConstantS;

/**
 * ���������Ϣ
 * 
 * @author lyl
 * 
 */
public class SavePersonInfoUtlis {
	/**
	 * ������Ϣ
	 * 
	 * @param info
	 * @param context
	 */
	public static void setPersonInfo(PersonalInfo info, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(ConstantS.USER_DATA,
				Context.MODE_PRIVATE);

		Editor editor = preferences.edit();
		// ��������
		editor.putBoolean("isLogin", true);
		editor.putString("id", info.getId());
		editor.putString("idcode", info.getIdcode());
		editor.putString("foodpic", info.getHeadimage());
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

		// �ύ�޸�
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

		// �ύ�޸�
		editor.commit();
	}

	
	/**
	 * ��ȡ������Ϣ
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
