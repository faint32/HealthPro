package com.wyy.myhealth.bean;

import java.io.Serializable;

public class RecorderInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -557636039847298243L;

	private int rank;
	
	private int reasonableNum;
	
	private int noSnapDays;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getReasonableNum() {
		return reasonableNum;
	}

	public void setReasonableNum(int reasonableNum) {
		this.reasonableNum = reasonableNum;
	}

	public int getNoSnapDays() {
		return noSnapDays;
	}

	public void setNoSnapDays(int noSnapDays) {
		this.noSnapDays = noSnapDays;
	}
	
	
	
}
