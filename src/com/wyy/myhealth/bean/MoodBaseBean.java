package com.wyy.myhealth.bean;

import java.io.Serializable;

public class MoodBaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5501627686568122980L;

	private String collectcount;

	private String commentcount;

	private String context;

	private String createtime;

	private String id;

	private String laudcount;

	private int moodindex;

	private String userid;

	public String getCollectcount() {
		return collectcount;
	}

	public void setCollectcount(String collectcount) {
		this.collectcount = collectcount;
	}

	public String getCommentcount() {
		return commentcount;
	}

	public void setCommentcount(String commentcount) {
		this.commentcount = commentcount;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLaudcount() {
		return laudcount;
	}

	public void setLaudcount(String laudcount) {
		this.laudcount = laudcount;
	}

	public int getMoodindex() {
		return moodindex;
	}

	public void setMoodindex(int moodindex) {
		this.moodindex = moodindex;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
