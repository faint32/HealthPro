package com.wyy.myhealth.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class UserAccountBean implements Parcelable {

	private String username;

	private String password;

	private int _id;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(username);
		dest.writeString(password);
		dest.writeInt(_id);
	}

	public static final Parcelable.Creator<UserAccountBean> CREATOR = new Parcelable.Creator<UserAccountBean>() {
		public UserAccountBean createFromParcel(Parcel in) {
			UserAccountBean userAccountBean = new UserAccountBean();
			userAccountBean._id = in.readInt();
			userAccountBean.username = in.readString();
			userAccountBean.password = in.readString();

			return userAccountBean;
		}

		public UserAccountBean[] newArray(int size) {
			return new UserAccountBean[size];
		}
	};

}
