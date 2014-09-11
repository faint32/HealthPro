package com.wyy.myhealth.bean;

import java.io.Serializable;

public class ScanMoodBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5172495505244877673L;
	
	private String content;
	
	private String img;

	public String getContent() {
		return ""+content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImg() {
		return ""+img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	

}
