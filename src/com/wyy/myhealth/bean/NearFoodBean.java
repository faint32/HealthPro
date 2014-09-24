package com.wyy.myhealth.bean;

public class NearFoodBean extends Foods{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String distance;

	private String visitcount;
	
	private String createtime;
	
	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	private boolean iscollect;
	
	public boolean isIscollect() {
		return iscollect;
	}

	public void setIscollect(boolean iscollect) {
		this.iscollect = iscollect;
	}

	public String getVisitcount() {
		return ""+visitcount;
	}

	public void setVisitcount(String visitcount) {
		this.visitcount = visitcount;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	
}
