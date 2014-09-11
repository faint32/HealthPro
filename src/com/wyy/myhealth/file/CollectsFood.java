package com.wyy.myhealth.file;

/**
 * 
 * @author lyl
 * 
 */
public class CollectsFood {
	// 收藏时间
	private String time;
	// 收藏月份
	private String month;
	// 收藏日子
	private String day;
	// 收藏食物id
	private String foodid;
	// 收藏id
	private String collect_id;
	// 图片地址
	private String foodpic;
	//食物标签
	private String tag;
	//食物口味级别
	private String tastelevel;
	//能量
	private String energy;
	//收藏次数
	private String collectcount;
	//赞次数
	private String laudcount;
	
	private String commentcount;
	private String summary;
	
	public String getCommentcount() {
		return commentcount;
	}

	public void setCommentcount(String commentcount) {
		this.commentcount = commentcount;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getCollectcount() {
		return collectcount;
	}

	public void setCollectcount(String collectcount) {
		this.collectcount = collectcount;
	}

	public String getLaudcount() {
		return laudcount;
	}

	public void setLaudcount(String laudcount) {
		this.laudcount = laudcount;
	}

	public String getEnergy() {
		return energy;
	}

	public void setEnergy(String energy) {
		this.energy = energy;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTastelevel() {
		return tastelevel;
	}

	public void setTastelevel(String tastelevel) {
		this.tastelevel = tastelevel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	//食物烈性
	private String type;

	public String getFoodpic() {
		return foodpic;
	}

	public void setFoodpic(String foodpic) {
		this.foodpic = foodpic;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	public String getFoodid() {
		return foodid;
	}

	public void setFoodid(String foodid) {
		this.foodid = foodid;
	}

	public String getCollect_id() {
		return collect_id;
	}

	public void setCollect_id(String collect_id) {
		this.collect_id = collect_id;
	}
}
