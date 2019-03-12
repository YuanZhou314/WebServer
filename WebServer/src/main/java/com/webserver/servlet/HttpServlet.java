package com.webserver.servlet;

import java.io.File;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public abstract class HttpServlet {
	/**
	 * 业务处理方法
	 * @param request
	 */
	public abstract void service(HttpRequest request,HttpResponse response);
	
	/**
	 * 跳转指定路径对应的资源
	 * 实际该方法将来(TomCat)在转发器中，可以通过request获取
	 * @param path
	 */
	public void forwart(String path,HttpRequest request,HttpResponse response){
		File file = new File("webapps/"+path);
		response.setEntity(file);
	}
}
