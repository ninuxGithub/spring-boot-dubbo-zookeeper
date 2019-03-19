package com.example.provider.utils;

import java.io.Serializable;


/**
 * 接口的返回对象
 * 
 * status: 200 代表成功， >=400: 失败
 * body:消息体
 * 
 *
 */
public class HttpResult implements Serializable {
	
	public static final HttpResult successResult = new HttpResult(200, "成功");
	public static final HttpResult failedResult = new HttpResult(400, "失败");

	private static final long serialVersionUID = 6452199670035589676L;
	private int status;
	private String body;

	public HttpResult() {
	}

	public HttpResult(int status, String body) {
		this.status = status;
		this.body = body;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "HttpResult [status=" + status + ", body=" + body + "]";
	}

}
