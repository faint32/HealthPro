package com.wyy.myhealth.bean;

import java.util.List;

public class ShaiMoods extends Mood {

	// 收藏数量
	private String collectcount;
	// 评论数量
	private String commentcount;
	// 赞
	private String laudcount;
	// 用户ID
	private String userid;
	// 用户头像
	private String headimage;
	// 评论内
	private String comment_content;
	// 用户名
	private String username;
	//图片地址
	private List<String> imgs; 

	// 评论者名字
	private String comment_user_name;

	public String getComment_user_name() {
		return comment_user_name;
	}

	public void setComment_user_name(String comment_user_name) {
		this.comment_user_name = comment_user_name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLaudcount() {
		return laudcount;
	}

	public void setLaudcount(String laudcount) {
		this.laudcount = laudcount;
	}

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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

	public List<String> getImgs() {
		return imgs;
	}

	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}


	
	
}
