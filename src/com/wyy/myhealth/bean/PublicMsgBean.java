package com.wyy.myhealth.bean;

import java.io.Serializable;

public class PublicMsgBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5981192065919191979L;
	
	private long realtime;

	private String time;
	
	private String content;
	
	private String chatname;
	
	private int msgtype;
	
	private int _id;
	
	

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(int msgtype) {
		this.msgtype = msgtype;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getChatname() {
		return chatname;
	}

	public void setChatname(String chatname) {
		this.chatname = chatname;
	}


	public long getRealtime() {
		return realtime;
	}

	public void setRealtime(long realtime) {
		this.realtime = realtime;
	}
	
}
