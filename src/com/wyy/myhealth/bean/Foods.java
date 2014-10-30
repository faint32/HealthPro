package com.wyy.myhealth.bean;

import java.io.Serializable;

public class Foods implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String belongToFoodsLib;
	String commentcount;
	String commercialAddress;
	String commercialLat;
	String commercialLon;
	String commercialName;
	String commercialPhone;

	String foodpic;
	String id;
	String summary;
	String tags;
	String tastelevel;
	String title;
	String type;
	String userid;
	String time;
	String month;
	String day;
	String energy;
	String reasonable;
	
	String collectcount;
	String laudcount;

	public String getCollectcount() {
		return collectcount;
	}

	public void setCollectcount(String collectcount) {
		this.collectcount = collectcount;
	}

	public String getLaudcount() {
		return ""+laudcount;
	}

	public void setLaudcount(String laudcount) {
		this.laudcount = laudcount;
	}

	public String getReasonable() {
		return reasonable;
	}

	public void setReasonable(String reasonable) {
		this.reasonable = reasonable;
	}

	public String getEnergy() {
		return energy;
	}

	public void setEnergy(String energy) {
		this.energy = energy;
	}

	String samedate="";
	
	public String isSamedate() {
		return samedate;
	}

	public void setSamedate(String samedate) {
		this.samedate = samedate;
	}

	public String getSamedate() {
		return samedate;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getBelongToFoodsLib() {
		return belongToFoodsLib;
	}

	public void setBelongToFoodsLib(String belongToFoodsLib) {
		this.belongToFoodsLib = belongToFoodsLib;
	}

	public String getCommentcount() {
		return ""+commentcount;
	}

	public void setCommentcount(String commentcount) {
		this.commentcount = commentcount;
	}

	public String getCommercialAddress() {
		return commercialAddress;
	}

	public void setCommercialAddress(String commercialAddress) {
		this.commercialAddress = commercialAddress;
	}

	public String getCommercialLat() {
		return commercialLat;
	}

	public void setCommercialLat(String commercialLat) {
		this.commercialLat = commercialLat;
	}

	public String getCommercialLon() {
		return commercialLon;
	}

	public void setCommercialLon(String commercialLon) {
		this.commercialLon = commercialLon;
	}

	public String getCommercialName() {
		return commercialName;
	}

	public void setCommercialName(String commercialName) {
		this.commercialName = commercialName;
	}

	public String getCommercialPhone() {
		return commercialPhone;
	}

	public void setCommercialPhone(String commercialPhone) {
		this.commercialPhone = commercialPhone;
	}

	public String getFoodpic() {
		return foodpic;
	}

	public void setFoodpic(String foodpic) {
		this.foodpic = foodpic;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getTastelevel() {
		return tastelevel;
	}

	public void setTastelevel(String tastelevel) {
		this.tastelevel = tastelevel;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "Foods [belongToFoodsLib=" + belongToFoodsLib
				+ ", commentcount=" + commentcount + ", commercialAddress="
				+ commercialAddress + ", commercialLat=" + commercialLat
				+ ", commercialLon=" + commercialLon + ", commercialName="
				+ commercialName + ", commercialPhone=" + commercialPhone
				+ ", foodpic=" + foodpic + ", id=" + id + ", summary="
				+ summary + ", tags=" + tags + ", tastelevel=" + tastelevel
				+ ", title=" + title + ", type=" + type + ", userid=" + userid
				+ ", time=" + time + ", month=" + month + ", day=" + day + "]";
	}
}
