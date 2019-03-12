package com.webserver.http;

import java.awt.Transparency;
import java.awt.TrayIcon;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.rule.Mode;

/**
 * �й�HTTPЭ��涨������
 * @author my
 *
 */
public class HttpContext {
	/**
	 * ״̬�����������Ķ�Ӧ��ϵ
	 * kry:״̬����
	 * value:��Ӧ��״̬����
	 */
	private static final Map<Integer,String> STATUS_MAPPING =new HashMap<Integer,String>();
	
	/**
	 * ��Դ��׺��Content-Typeֵ�Ķ�Ӧ��ϵ
	 * key:��׺�����磺png
	 * value:Content-Type��Ӧֵ���磺image/png
	 */
	private static final Map<String,String> MIME_MAPPING = new HashMap<String,String>();
	
	static{
		//��ʼ��
		
		//��ʼ����״̬�������Ӧ����
		initStatusMapping();
		//��ʼ������Դ��׺���Ӧ��Content-Type��ֵ
		initMimeMapping();
	}
	
	public static void initMimeMapping(){
//		MIME_MAPPING.put("png", "image/png");
//		MIME_MAPPING.put("gif", "image/gif");
//		MIME_MAPPING.put("jpg", "image/jpg");
//		MIME_MAPPING.put("css", "text/css");
//		MIME_MAPPING.put("html", "text/html");
//		MIME_MAPPING.put("js", "application/javascript");
		
		/*
		 * ͨ������web.xml�ļ���ʼ��MIME_MAPING
		 * 
		 * 1.����SAXReader
		 * 2.ʹ��SAXReader��ȡconfĿ¼��web.xml�ļ�
		 * 3.��ȡ��Ԫ������������<mime-mapping>����Ԫ��
		 * 4.����ÿ��<mime-mapping>Ԫ�أ���������Ԫ�أ�
		 * 	<extension>�м��ı���Ϊkey
		 * 	<mime-type>�м���ı���Ϊvalue
		 * 	���浽MIME_MAPPING���Map�м���
		 */
		try {
			SAXReader sax = new SAXReader();
			Document doc = sax.read(new File("./conf/web.xml"));
			Element root = doc.getRootElement();
			List<Element> list = root.elements("mime-mapping");
			for(Element docEle : list){
				String extension = docEle.elementTextTrim("extension");
				String mime = docEle.elementTextTrim("mime-type");
				MIME_MAPPING.put(extension, mime);
			}
//			System.out.println("������ɣ���"+MIME_MAPPING.size()+"��");
//			System.out.println(MIME_MAPPING);
//			System.out.println("zip�����ͣ�"+MIME_MAPPING.get("zip"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * ��ʼ��״̬�������Ӧ������
	 */
	public static void initStatusMapping(){
		STATUS_MAPPING.put(200, "OK");
		STATUS_MAPPING.put(201, "Created");
		STATUS_MAPPING.put(202, "Accepted");
		STATUS_MAPPING.put(204, "No Content");
		STATUS_MAPPING.put(301, "Moved Permanently");
		STATUS_MAPPING.put(302, "Moved Temporarily");
		STATUS_MAPPING.put(304, "Not Modified");
		STATUS_MAPPING.put(400, "Bad Request");
		STATUS_MAPPING.put(401, "Unauthorized");
		STATUS_MAPPING.put(403, "Forbidden");
		STATUS_MAPPING.put(404, "Not Found");
		STATUS_MAPPING.put(500, "Internal Server Error");
		STATUS_MAPPING.put(501, "Not Implemented");
		STATUS_MAPPING.put(502, "Bad Gateway");
		STATUS_MAPPING.put(503, "Service Unavailable");
	}
	
	/**
	 * ����״̬�����ȡ��Ӧ��״̬����Ĭ��ֵ
	 * @param statusCode
	 * @return
	 */
	public static String getStatusReason(int statusCode){
		return STATUS_MAPPING.get(statusCode);
	}
	
	/**
	 * ������Դ��׺����ȡ��Ӧ��Content-Type��ֵ
	 * @param ext
	 * @return
	 */
	public static String getMimeType(String ext){
		return MIME_MAPPING.get(ext);
	}
	
//	public static void main(String[] args) {
//		String reason = getStatusReason(501);
//		System.out.println(reason);
//		
//		String fileName = "xxx.jpg";
//		int index = fileName.lastIndexOf(".")+1;
//		String ext = fileName.substring(index);
//		System.out.println(ext);
//		String type = getMimeType("ico");
//		System.out.println(type);
	}
