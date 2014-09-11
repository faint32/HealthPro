package com.wyy.myhealth.bean;

import java.io.Serializable;

public class IceBoxFoodBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -811812954692307477L;

	
	private String createtime;
	
	private int energy;
	
	private int fat;
	
	private int mineral;
	
	private int protein;
	
	private int vitamin;
	
	private int sugar;
	
	private String foodpic;
	
	private String id;
	
	private String isexpire;
	
	private String name;
	
	private String numday;
	
	private String source;
	
	
	
	public int getSugar() {
		return sugar;
	}

	public void setSugar(int sugar) {
		this.sugar = sugar;
	}

	public int getVitamin() {
		return vitamin;
	}

	public void setVitamin(int vitamin) {
		this.vitamin = vitamin;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private int type;
	
	private String userid;

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
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

	public String getIsexpire() {
		return isexpire;
	}

	public void setIsexpire(String isexpire) {
		this.isexpire = isexpire;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumday() {
		return numday;
	}

	public void setNumday(String numday) {
		this.numday = numday;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getFat() {
		return fat;
	}

	public void setFat(int fat) {
		this.fat = fat;
	}

	public int getMineral() {
		return mineral;
	}

	public void setMineral(int mineral) {
		this.mineral = mineral;
	}

	public int getProtein() {
		return protein;
	}

	public void setProtein(int protein) {
		this.protein = protein;
	}
	
	
	
}
