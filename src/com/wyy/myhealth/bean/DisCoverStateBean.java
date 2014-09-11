package com.wyy.myhealth.bean;

import java.io.Serializable;

public class DisCoverStateBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4903020938982159292L;

	private boolean hasNewShai;
	
	private boolean hasNewHps;
	
	private String imgUrl;

	public boolean isHasNewShai() {
		return hasNewShai;
	}

	public void setHasNewShai(boolean hasNewShai) {
		this.hasNewShai = hasNewShai;
	}

	public boolean isHasNewHps() {
		return hasNewHps;
	}

	public void setHasNewHps(boolean hasNewHps) {
		this.hasNewHps = hasNewHps;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	
	
	
	
}
