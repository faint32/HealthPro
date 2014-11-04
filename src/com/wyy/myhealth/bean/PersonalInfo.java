package com.wyy.myhealth.bean;

import java.io.Serializable;

import org.json.JSONArray;

import android.text.TextUtils;

public class PersonalInfo implements Serializable {
	// "tags": "",
	// "summary": "",
	// "birthday": "",
	// "weight": "",
	// "status": 0,
	// "roleid": 4,
	// "nickname": "",
	// "mobilenum": "",
	// "job": "",
	// "bodyindex": "0",
	// "piccode": "",
	// "id": 1,
	// "headimage": "/default/userHeader.png",
	// "authkey": "",
	// "username": "admin",
	// "height": "",
	// "idcode": "",
	// "email": "",
	// "gender": 1,
	// "sportindex": 0,
	// "pinyin": "admin",
	// "salt": "123"

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	String tags;// ±Í«©
	String summary;// √Ë ˆ
	String birthday;
	String weight;
	String status;
	String roleid;
	String nickname;
	String mobilenum;
	String authkey;
	String job;
	String piccode;
	String username;
	String id;
	String gender;
	String idcode;
	String email;
	String height;
	String headimage;
	String bodyindex;
	String pinyin;
	String salt;
	String sportindex;
	String age;
	String money;

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSportindex() {
		return sportindex;
	}

	public void setSportindex(String sportindex) {
		this.sportindex = sportindex;
	}

	public String getAuthkey() {
		return authkey;
	}

	public void setAuthkey(String authkey) {
		this.authkey = authkey;
	}

	public String getPiccode() {
		return piccode;
	}

	public void setPiccode(String piccode) {
		this.piccode = piccode;
	}

	public String getUsername() {
		if (TextUtils.isEmpty(username)) {
			username = "";
		}
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIdcode() {
		return idcode;
	}

	public void setIdcode(String idcode) {
		this.idcode = idcode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getSummary() {
		if (TextUtils.isEmpty(summary)) {
			summary = "";
		}
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMobilenum() {
		return mobilenum;
	}

	public void setMobilenum(String mobilenum) {
		this.mobilenum = mobilenum;
	}

	public String getJob() {
		if (TextUtils.isEmpty(job)) {
			job = "";
		}
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getBodyindex() {
		if (TextUtils.isEmpty(bodyindex)) {
			bodyindex = "";
		}
		return bodyindex;
	}

	public void setBodyindex(String bodyindex) {
		this.bodyindex = bodyindex;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Override
	public String toString() {
		return "PersonalInfo [tags=" + tags + ", summary=" + summary
				+ ", birthday=" + birthday + ", weight=" + weight + ", status="
				+ status + ", roleid=" + roleid + ", nickname=" + nickname
				+ ", mobilenum=" + mobilenum + ", authkey=" + authkey
				+ ", job=" + job + ", piccode=" + piccode + ", username="
				+ username + ", id=" + id + ", gender=" + gender + ", idcode="
				+ idcode + ", email=" + email + ", height=" + height
				+ ", headimage=" + headimage + ", bodyindex=" + bodyindex
				+ ", pinyin=" + pinyin + ", salt=" + salt + ", sportindex="
				+ sportindex + ", age=" + age + "]";
	}

}
