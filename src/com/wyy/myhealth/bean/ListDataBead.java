package com.wyy.myhealth.bean;

import java.io.Serializable;

public class ListDataBead implements Serializable{

	/**
	 * °æ±¾ºÅ
	 */
	private static final long serialVersionUID = 1L;

	private int postion;
	
	private String jsondata;
	
	private String time;
	
	private int _id;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getPostion() {
		return postion;
	}

	public void setPostion(int postion) {
		this.postion = postion;
	}

	public String getJsondata() {
		return jsondata;
	}

	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}
	
	
	
}
