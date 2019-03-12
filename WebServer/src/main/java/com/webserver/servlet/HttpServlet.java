package com.webserver.servlet;

import java.io.File;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public abstract class HttpServlet {
	/**
	 * ҵ������
	 * @param request
	 */
	public abstract void service(HttpRequest request,HttpResponse response);
	
	/**
	 * ��תָ��·����Ӧ����Դ
	 * ʵ�ʸ÷�������(TomCat)��ת�����У�����ͨ��request��ȡ
	 * @param path
	 */
	public void forwart(String path,HttpRequest request,HttpResponse response){
		File file = new File("webapps/"+path);
		response.setEntity(file);
	}
}
