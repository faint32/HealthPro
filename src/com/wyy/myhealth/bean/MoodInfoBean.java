package com.wyy.myhealth.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MoodInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1011966203465358661L;

	private int type;

	private List<String> img;

	private PersonalInfo user;

	private List<Comment> comments=new ArrayList<>();

	private String cn_time;

	private boolean isCollect;

	private boolean isLaud;

	private List<PersonalInfo> laudUser;

	private MoodBaseBean mood;
	
	public void setMood(MoodBaseBean mood) {
		this.mood = mood;
	}
	
	public MoodBaseBean getMood() {
		return mood;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<String> getImg() {
		return img;
	}

	public void setImg(List<String> img) {
		this.img = img;
	}

	public PersonalInfo getUser() {
		return user;
	}

	public void setUser(PersonalInfo user) {
		this.user = user;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String getCn_time() {
		return cn_time;
	}

	public void setCn_time(String cn_time) {
		this.cn_time = cn_time;
	}

	public boolean isCollect() {
		return isCollect;
	}

	public void setCollect(boolean isCollect) {
		this.isCollect = isCollect;
	}

	public boolean isLaud() {
		return isLaud;
	}

	public void setLaud(boolean isLaud) {
		this.isLaud = isLaud;
	}

	public List<PersonalInfo> getLaudUser() {
		return laudUser;
	}

	public void setLaudUser(List<PersonalInfo> laudUser) {
		this.laudUser = laudUser;
	}

}
