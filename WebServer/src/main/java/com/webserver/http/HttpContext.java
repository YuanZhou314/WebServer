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
 * 有关HTTP协议规定的内容
 * @author my
 *
 */
public class HttpContext {
	/**
	 * 状态代码与描述的对应关系
	 * kry:状态代码
	 * value:对应的状态描述
	 */
	private static final Map<Integer,String> STATUS_MAPPING =new HashMap<Integer,String>();
	
	/**
	 * 资源后缀与Content-Type值的对应关系
	 * key:后缀名，如：png
	 * value:Content-Type对应值，如：image/png
	 */
	private static final Map<String,String> MIME_MAPPING = new HashMap<String,String>();
	
	static{
		//初始化
		
		//初始化了状态代码与对应描述
		initStatusMapping();
		//初始化了资源后缀与对应的Content-Type的值
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
		 * 通过解析web.xml文件初始化MIME_MAPING
		 * 
		 * 1.创建SAXReader
		 * 2.使用SAXReader读取conf目录中web.xml文件
		 * 3.获取根元素下所有名字<mime-mapping>的子元素
		 * 4.遍历每个<mime-mapping>元素，并将其子元素：
		 * 	<extension>中间文本作为key
		 * 	<mime-type>中间的文本作为value
		 * 	保存到MIME_MAPPING这个Map中即可
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
//			System.out.println("解析完成，共"+MIME_MAPPING.size()+"个");
//			System.out.println(MIME_MAPPING);
//			System.out.println("zip的类型："+MIME_MAPPING.get("zip"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 初始化状态代码与对应的描述
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
	 * 根据状态代码获取对应的状态描述默认值
	 * @param statusCode
	 * @return
	 */
	public static String getStatusReason(int statusCode){
		return STATUS_MAPPING.get(statusCode);
	}
	
	/**
	 * 根据资源后缀名获取对应的Content-Type的值
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
