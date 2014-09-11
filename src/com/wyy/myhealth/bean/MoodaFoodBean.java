package com.wyy.myhealth.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author lyl
 * @see Foods
 */
public class MoodaFoodBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6353390569961094933L;

	private int type;

	private String id;

	private String context;

	private String summary;

	private String tags;

	private int tastelevel;

	private int moodindex;

	private List<String> img;

	private String createtime;

	private PersonalInfo user;

	private List<CommentBean> comment;

	private String cn_time;

	private String day;

	private String month;
	
	private boolean isAdv;

	public boolean isAdv() {
		return isAdv;
	}

	public void setAdv(boolean isAdv) {
		this.isAdv = isAdv;
	}

	public String getDay() {
		return ""+day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return ""+month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getCn_time() {
		return ""+cn_time;
	}

	public void setCn_time(String cn_time) {
		this.cn_time = cn_time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTags() {
		return ""+tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public int getTastelevel() {
		return tastelevel;
	}

	public void setTastelevel(int tastelevel) {
		this.tastelevel = tastelevel;
	}

	public int getMoodindex() {
		return moodindex;
	}

	public void setMoodindex(int moodindex) {
		this.moodindex = moodindex;
	}

	public List<String> getImg() {
		return img;
	}

	public void setImg(List<String> img) {
		this.img = img;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public PersonalInfo getUser() {
		return user;
	}

	public void setUser(PersonalInfo user) {
		this.user = user;
	}

	public List<CommentBean> getComment() {
		return comment;
	}

	public void setComment(List<CommentBean> comment) {
		this.comment = comment;
	}

}
