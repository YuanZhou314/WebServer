package com.webserver.http;
/**
 * 空请求异常
 * 当实例化HttpRequest时若为空请求时会抛出该异常
 * 1.定义异常名2.继承Exception3.添加默认序列号4.重写方法
 * @author my
 *
 */
public class EmptyRequestException extends Exception{
	private static final long serialVersionUID = 1L;

	public EmptyRequestException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmptyRequestException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public EmptyRequestException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public EmptyRequestException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public EmptyRequestException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
