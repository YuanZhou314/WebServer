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
 * ��Ӧ����
 * �����ÿ��ʵ����ʾ����˷��͸��ͻ��˵�һ�������HTTP��Ӧ����
 * һ��HTTP��Ӧ���������֣�
 * ״̬�У���Ӧͷ����Ӧ����
 * 
 * @author my
 *
 */
public class HttpResponse {
	//״̬�������Ϣ�Ķ���
	//״̬���룬Ĭ��Ϊ��200
	private int statusCode = 200;
	//״̬������Ĭ��Ϊ��"OK"
	private String statusReason = "OK";
	
	
	//��Ӧͷ�����Ϣ�Ķ���
	private Map<String,String> headers = new HashMap<String,String>();
	
	
	
	//��Ӧ���������Ϣ�Ķ���
	private File entity;//��Ӧ��ʵ���ļ�
	
	//�����������Ϣ
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
	 * ����ǰ������Ӧ���ݰ��ձ�׼HTTP��Ӧ��ʽ���͸��ͻ��ˡ�
	 */
	public void flush(){
		/*
		 * 1.����״̬��
		 * 2.������Ӧͷ
		 * 3.������Ӧ����
		 */
		sendStatusLine();
		sendHeaders();
		sendContent();
		}
	public void sendStatusLine(){
		System.out.println("HttpResponse:��ʼ����״̬��....");
		try {
			String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);//write CR
			out.write(10);//write LF
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpResponse:������Ӧ����ϣ�");
	}
	public void sendHeaders(){
		System.out.println("HttpResponse:��ʼ������Ӧͷ....");
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
			//��������һ��CRLF��ʾ��Ӧͷ�������
			out.write(13);
			out.write(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpResponse:������Ӧͷ��ϣ�");
	}
	public void sendContent(){
		System.out.println("HttpResponse:��ʼ������Ӧ����....");
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
		System.out.println("HttpResponse:������Ӧ������ϣ�");
	}

	/**
	 * ������Ӧ���ĵ�ʵ���ļ�
	 * �����ø��ļ���ͬʱ�����Զ����ݸ��ļ����
	 * ������Ӧͷ��Content-Type��Content-Length
	 * @return
	 */
	public File getEntity() {
		/*
		 * ���Content-Type
		 * ���ݸ������ļ������ֵĺ�׺����HttpContext�л�ȡ��Ӧ��Content-Typeֵ
		 */
		String fileName = entity.getName();
		int index = fileName.lastIndexOf(".")+1;
		String ext = fileName.substring(index);
		String contentType = HttpContext.getMimeType(ext);
		this.headers.put("Content-Type",contentType);
		//���Content-Length
		this.headers.put("Content-Length",entity.length()+" ");//�ӿ��ַ���ת��Ϊ�ַ�������
		return entity;
	}

	public void setEntity(File entity) {
		this.entity = entity;
	}

	public int getStatusCode() {
		return statusCode;
	}
	
	/**
	 * ����״̬����
	 * �����õ�ͬʱ���ڲ������״̬����ȡHttpContext�л�ȡ�ô����Ӧ��״̬����ֵ�������Զ�����
	 * ��������ʡȥ�����ÿ������״̬�����Ҫ��������״̬����������
	 * ������Ҫ���ô���������ò�ͬ��״̬����ֵ������Ͳ����ٵ���setStatusReason������
	 * @param statusCode
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		//�Զ����ö�Ӧ��״̬����
		this.statusReason = HttpContext.getStatusReason(statusCode);
	}

	public String getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}
	
	/**
	 * ���ָ������Ӧͷ
	 */
	public void putHeader(String name,String value){
		this.headers.put(name, value);
	}
	
	public String getHeader(String name){
		return this.headers.get(name);
	}
}




















