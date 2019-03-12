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
 * 该类定义有关服务端配置信息
 * @author my
 *
 */
public class ServerContext {
	/**
	 * servlet 对应关系
	 * key:对应路径
	 * value :对应的Servlet类的完全限定名
	 */
	private static final Map<String,String> SERVLET_MAPPING = new HashMap<String,String>();
	/**
	 * 初始化请求
	 */
	static{
//		SERVLET_MAPPING.put("/myweb/reg", "com.webserver.servlet.RegServlet");
//		SERVLET_MAPPING.put("/myweb/login", "com.webserver.servlet.LoginServlet");
		/*
		 * 解析conf/servlets.xml
		 * 将根目录<servlets>下的所有<servlet>元素取出
		 * 并将每个<servlet>元素中的属性：
		 * url的值作为key,classNmae的值作为value
		 * 保存到SERVLET_MAPPING这个Map中完成初始化
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
	 * 根据请求获取对应的业务处理类的类名
	 */
	public static String getServletName(String url){
		return SERVLET_MAPPING.get(url);
	}
	
	public static void main(String[] args) {
		ServerContext server = new ServerContext();
		
	}
}
