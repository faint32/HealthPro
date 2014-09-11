package com.wyy.myhealth.bean;

public class Mood {

	String id;
	String context;
	String moodindex;
	String time;
	String month;
	String day;
	String moodpic;

	String samedate="";
	
	public String getSamedate() {
		return samedate;
	}

	public void setSamedate(String samedate) {
		this.samedate = samedate;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getMoodindex() {
		return moodindex;
	}

	public void setMoodindex(String moodindex) {
		this.moodindex = moodindex;
	}
	
	
	public String getMoodpic() {
		return moodpic;
	}

	public void setMoodpic(String moodpic) {
		this.moodpic = moodpic;
	}

	@Override
	public String toString() {
		return "Mood [id=" + id + ", context=" + context + ", moodindex="
				+ moodindex + ", time=" + time + ", month=" + month + ", day="
				+ day + "smadate"+samedate+"]";
	}

}
