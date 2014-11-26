package com.wyy.myhealth.bean;

import java.io.Serializable;

public class MsgBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7130544152584123319L;

	private String content="";
	
	private String createtime="";
	
	private String id;
	
	private String objimg;
	
	private int objtype;
	
	private String objid="";
	
	private String receiveuid;
	
	private String sendheadimage;
	
	private String senduid;
	
	private String sendusername="";
	
	private int type;
	
	private String cn_time="";
	
	public void setObjid(String objid) {
		this.objid = objid;
	}
	
	public String getObjid() {
		return objid;
	}
	
	public void setCn_time(String cn_time) {
		this.cn_time = cn_time;
	}
	
	public String getCn_time() {
		return cn_time;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObjimg() {
		return objimg;
	}

	public void setObjimg(String objimg) {
		this.objimg = objimg;
	}

	public int getObjtype() {
		return objtype;
	}

	public void setObjtype(int objtype) {
		this.objtype = objtype;
	}

	public String getReceiveuid() {
		return receiveuid;
	}

	public void setReceiveuid(String receiveuid) {
		this.receiveuid = receiveuid;
	}

	public String getSendheadimage() {
		return sendheadimage;
	}

	public void setSendheadimage(String sendheadimage) {
		this.sendheadimage = sendheadimage;
	}

	public String getSenduid() {
		return senduid;
	}

	public void setSenduid(String senduid) {
		this.senduid = senduid;
	}

	public String getSendusername() {
		return sendusername;
	}

	public void setSendusername(String sendusername) {
		this.sendusername = sendusername;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	
	
}
