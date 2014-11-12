package com.wyy.myhealth.bean;

import java.io.Serializable;

public class LevelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 313091660396512096L;

	private String createtime;
	
	private String img;
	
	private String name="";
	
	private int level;

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	
	
}
