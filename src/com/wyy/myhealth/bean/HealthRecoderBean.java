package com.wyy.myhealth.bean;

import java.io.Serializable;

public class HealthRecoderBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String mineral;
	
	private String protein;
	
	private String fat;
	
	private String vitamin;
	
	private String reasonable;
	
	private String energy;
	
	private String sugar;
	
	private String createtime;
	
	private String log;

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getMineral() {
		return mineral;
	}

	public void setMineral(String mineral) {
		this.mineral = mineral;
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

	public String getEnergy() {
		return energy;
	}

	public void setEnergy(String energy) {
		this.energy = energy;
	}

	public String getSugar() {
		return sugar;
	}

	public void setSugar(String sugar) {
		this.sugar = sugar;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	

}
