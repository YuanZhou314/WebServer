package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 响应对象
 * 该类的每个实例表示服务端发送给客户端的一个具体的HTTP响应内容
 * 一个HTTP响应包含三部分：
 * 状态行，响应头，响应正文
 * 
 * @author my
 *
 */
public class HttpResponse {
	//状态行相关信息的定义
	//状态代码，默认为：200
	private int statusCode = 200;
	//状态描述，默认为："OK"
	private String statusReason = "OK";
	
	
	//响应头相关信息的定义
	private Map<String,String> headers = new HashMap<String,String>();
	
	
	
	//响应正文相关信息的定义
	private File entity;//响应的实体文件
	
	//和连接相关信息
	private Socket socket;
	private OutputStream out;
	
	public HttpResponse(Socket socket){
		try {
			this.socket = socket;
			this.out = socket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 将当前对象响应内容按照标准HTTP相应格式发送给客户端。
	 */
	public void flush(){
		/*
		 * 1.发送状态行
		 * 2.发送响应头
		 * 3.发送响应正文
		 */
		sendStatusLine();
		sendHeaders();
		sendContent();
		}
	public void sendStatusLine(){
		System.out.println("HttpResponse:开始发送状态行....");
		try {
			String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);//write CR
			out.write(10);//write LF
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpResponse:发送响应行完毕！");
	}
	public void sendHeaders(){
		System.out.println("HttpResponse:开始发送响应头....");
		try {
			Set<Entry<String,String>> entrySet = headers.entrySet();
			for(Entry<String,String> header :entrySet){
				String key = header.getKey();
				String value = header.getValue();
				String line = key+": "+value;
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
			} 
			//单独发送一个CRLF表示响应头发送完毕
			out.write(13);
			out.write(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpResponse:发送响应头完毕！");
	}
	public void sendContent(){
		System.out.println("HttpResponse:开始发送响应正文....");
		if(entity==null){	
			return;
		}
		try (FileInputStream fis = new FileInputStream(entity);
				){
			int len = -1;
			byte[] date = new byte[1024*10];
			while((len = fis.read(date))!=-1){
				out.write(date,0,len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpResponse:发送响应正文完毕！");
	}

	/**
	 * 设置响应正文的实体文件
	 * 在设置该文件的同时，会自动根据该文件添加
	 * 两个响应头：Content-Type和Content-Length
	 * @return
	 */
	public File getEntity() {
		/*
		 * 添加Content-Type
		 * 根据给定的文件的名字的后缀，从HttpContext中获取对应的Content-Type值
		 */
		String fileName = entity.getName();
		int index = fileName.lastIndexOf(".")+1;
		String ext = fileName.substring(index);
		String contentType = HttpContext.getMimeType(ext);
		this.headers.put("Content-Type",contentType);
		//添加Content-Length
		this.headers.put("Content-Length",entity.length()+" ");//加空字符串转换为字符串类型
		return entity;
	}

	public void setEntity(File entity) {
		this.entity = entity;
	}

	public int getStatusCode() {
		return statusCode;
	}
	
	/**
	 * 设置状态代码
	 * 在设置的同时，内部会根据状态代码取HttpContext中获取该代码对应的状态描述值并进行自动设置
	 * 这样做就省去了外界每次设置状态代码后还要单独进行状态描述的设置
	 * 除非需要给该代码额外设置不同的状态描述值，否则就不用再调用setStatusReason方法了
	 * @param statusCode
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		//自动设置对应的状态描述
		this.statusReason = HttpContext.getStatusReason(statusCode);
	}

	public String getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}
	
	/**
	 * 添加指定的响应头
	 */
	public void putHeader(String name,String value){
		this.headers.put(name, value);
	}
	
	public String getHeader(String name){
		return this.headers.get(name);
	}
}




















