package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * ���ڴ���ʵ��ҵ��
 * @author my
 *
 */
public class RegServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response){
		/*
		 * ע�����̣�
		 * 1.ͨ��request��ȡ�û��ύ��ע����Ϣ
		 * 2.���û���Ϣд��user.dat��
		 * 3.����responsse��Ӧ��ע����ҳ��
		 */
		System.out.println("RegServlet:��ʼע��......");
		//1.��ȡ�û���Ϣ
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		int age = Integer.parseInt(request.getParameter("age"));
		System.out.println(username+","+password+","+nickname+","+age);
		
		/*
		 * 2.�����û��������롢�ǳ�Ϊ�ַ���������Ϊ����
		 * �������ÿ����¼ռ100�ֽ�
		 * �û������룬�ǳƸ�ռ32�ֽڣ�����̶�4�ֽ�
		 */
		try {
			RandomAccessFile raf = new RandomAccessFile("user.dat","rw");
			raf.seek(raf.length());
			//д�û���
			byte[] date = username.getBytes("UTF-8");
			date = Arrays.copyOf(date, 32);
			raf.write(date);
			
			//д����
			date = password.getBytes("UTF-8");
			date = Arrays.copyOf(date, 32);
			raf.write(date);
			
			//д�ǳ�
			date = nickname.getBytes("UTF-8");
			date = Arrays.copyOf(date, 32);
			raf.write(date);
			
			//д����
			raf.writeInt(age);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//3.��Ӧ�û�ע��ɹ�
		forwart("myweb/reg_success.html", request, response);
		System.out.println("RegServlet:ע����ϣ�");
	}
}
