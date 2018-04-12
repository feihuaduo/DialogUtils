package com.feihua.dialogutils.bean;

public class UpdateLog
{
	private String version;
	private String message;

	public void setMessage(String message){
		this.message=message;
	}

	public String getMessage(){
		return message;
	}

	

	public void setVersion(String version){
		this.version=version;
	}

	public String getVersion(){
		return version;
	}
	}
