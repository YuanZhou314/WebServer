package com.webserver.core;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * ���ඨ���йط����������Ϣ
 * @author my
 *
 */
public class ServerContext {
	/**
	 * servlet ��Ӧ��ϵ
	 * key:��Ӧ·��
	 * value :��Ӧ��Servlet�����ȫ�޶���
	 */
	private static final Map<String,String> SERVLET_MAPPING = new HashMap<String,String>();
	/**
	 * ��ʼ������
	 */
	static{
//		SERVLET_MAPPING.put("/myweb/reg", "com.webserver.servlet.RegServlet");
//		SERVLET_MAPPING.put("/myweb/login", "com.webserver.servlet.LoginServlet");
		/*
		 * ����conf/servlets.xml
		 * ����Ŀ¼<servlets>�µ�����<servlet>Ԫ��ȡ��
		 * ����ÿ��<servlet>Ԫ���е����ԣ�
		 * url��ֵ��Ϊkey,classNmae��ֵ��Ϊvalue
		 * ���浽SERVLET_MAPPING���Map����ɳ�ʼ��
		 */
		try {
			SAXReader sax = new SAXReader();
			Document servlets = sax.read(new File("./conf/Servlets.xml"));
			Element root = servlets.getRootElement();
			List<Element> list = root.elements("servlet");
			for(Element serEle :list){
				String url = serEle.attributeValue("url");
				String className = serEle.attributeValue("className");
				SERVLET_MAPPING.put(url, className);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * ���������ȡ��Ӧ��ҵ�����������
	 */
	public static String getServletName(String url){
		return SERVLET_MAPPING.get(url);
	}
	
	public static void main(String[] args) {
		ServerContext server = new ServerContext();
		
	}
}
