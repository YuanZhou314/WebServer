package com.webserver.servlet;
/**
 * 处理登录业务
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
/**
 * 所有Servlet的超类
 * @author my
 */
public class LoginServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response){
		System.out.println("LoginServlet:开始登录......");
		
		//1.获取用户信息
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		//登录验证
		try(RandomAccessFile raf = new RandomAccessFile("user.dat","r");
				) {
			//默认表示登录失败
			boolean check = false;
			for(int i=0;i<raf.length()/100;i++){
				raf.seek(i*100);
				byte[] date = new byte[32];
				raf.read(date);
				String name = new String(date,"UTF-8").trim();
				if(name.equals(username)){
					raf.read(date);
					String pwd = new String(date,"UTF-8").trim();
					if(pwd.equals(password)){
						//登录成功
						forwart("myweb/login_success.html", request, response);
						check = true;
					}
					break;
				}
			}
			if(!check){
				//判断是否登录成功
				System.out.println("密码错误！");
				forwart("myweb/login_fail.html", request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("登录完毕！");
	}
}
