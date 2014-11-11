package com.wyy.myhealth.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.text.TextUtils;

public class Comment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3851538581029233160L;
	String content;
	String id;
	String foodsuserid;
	String username;
	String mineral;
	String foodsid;
	String protein;
	String fat;
	String userid;
	String userheadimage;
	String commenttime;
	String vitamin;
	String reasonable;
	String sugar;
	String energy;
	String createtime;
	String cn_time="";
	String headimage;
	
	int commentid;
	
	public List<Comment> comment=new ArrayList<>();
	
	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}

	public int getCommentid() {
		return commentid;
	}

	public void setCommentid(int commentid) {
		this.commentid = commentid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFoodsuserid() {
		return foodsuserid;
	}

	public void setFoodsuserid(String foodsuserid) {
		this.foodsuserid = foodsuserid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMineral() {
		return mineral;
	}

	public void setMineral(String mineral) {
		this.mineral = mineral;
	}

	public String getFoodsid() {
		return foodsid;
	}

	public void setFoodsid(String foodsid) {
		this.foodsid = foodsid;
	}

	public String getProtein() {
		return protein;
	}

	public void setProtein(String protein) {
		this.protein = protein;
	}

	public String getFat() {
		return fat;
	}

	public void setFat(String fat) {
		this.fat = fat;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserheadimage() {
		return userheadimage;
	}

	public void setUserheadimage(String userheadimage) {
		this.userheadimage = userheadimage;
	}

	public String getCommenttime() {
		return commenttime;
	}

	public void setCommenttime(String commenttime) {
		this.commenttime = commenttime;
	}

	public String getVitamin() {
		return vitamin;
	}

	public void setVitamin(String vitamin) {
		this.vitamin = vitamin;
	}

	public String getReasonable() {
		return reasonable;
	}

	public void setReasonable(String reasonable) {
		this.reasonable = reasonable;
	}

	public String getSugar() {
		return sugar;
	}

	public void setSugar(String sugar) {
		this.sugar = sugar;
	}

	public String getEnergy() {
		return energy;
	}

	public void setEnergy(String energy) {
		this.energy = energy;
	}

	
	public void setCn_time(String cn_time) {
		this.cn_time = cn_time;
	}
	
	public String getCn_time() {
		return cn_time;
	}
	
	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}
	
	public String getHeadimage() {
		return headimage;
	}
	
	@Override
	public String toString() {
		return "Comment [content=" + content + ", id=" + id + ", foodsuserid="
				+ foodsuserid + ", username=" + username + ", mineral="
				+ mineral + ", foodsid=" + foodsid + ", protein=" + protein
				+ ", fat=" + fat + ", userid=" + userid + ", userheadimage="
				+ userheadimage + ", commenttime=" + commenttime + ", vitamin="
				+ vitamin + ", reasonable=" + reasonable + ", sugar=" + sugar
				+ ", energy=" + energy + "]";
	}

}
