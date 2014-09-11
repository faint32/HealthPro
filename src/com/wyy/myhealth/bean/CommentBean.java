package com.wyy.myhealth.bean;

import java.io.Serializable;

public class CommentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6744334774183207609L;

	private String id;
	
	private String content;
	
	private String createtime;
	
	private String commentid;
	
	private PersonalInfo user;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getCommentid() {
		return commentid;
	}

	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}

	public PersonalInfo getUser() {
		return user;
	}

	public void setUser(PersonalInfo user) {
		this.user = user;
	}
	
	
	
	
}
