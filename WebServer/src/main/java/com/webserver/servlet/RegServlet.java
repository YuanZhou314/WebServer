package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * 用于处理实际业务
 * @author my
 *
 */
public class RegServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response){
		/*
		 * 注册流程：
		 * 1.通过request获取用户提交的注册信息
		 * 2.将用户信息写入user.dat中
		 * 3.设置responsse对应的注册结果页面
		 */
		System.out.println("RegServlet:开始注册......");
		//1.获取用户信息
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		int age = Integer.parseInt(request.getParameter("age"));
		System.out.println(username+","+password+","+nickname+","+age);
		
		/*
		 * 2.其中用户名、密码、昵称为字符串，年龄为整数
		 * 因此我们每条记录占100字节
		 * 用户，密码，昵称各占32字节，年龄固定4字节
		 */
		try {
			RandomAccessFile raf = new RandomAccessFile("user.dat","rw");
			raf.seek(raf.length());
			//写用户名
			byte[] date = username.getBytes("UTF-8");
			date = Arrays.copyOf(date, 32);
			raf.write(date);
			
			//写密码
			date = password.getBytes("UTF-8");
			date = Arrays.copyOf(date, 32);
			raf.write(date);
			
			//写昵称
			date = nickname.getBytes("UTF-8");
			date = Arrays.copyOf(date, 32);
			raf.write(date);
			
			//写年龄
			raf.writeInt(age);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//3.响应用户注册成功
		forwart("myweb/reg_success.html", request, response);
		System.out.println("RegServlet:注册完毕！");
	}
}
