package com.webserver.servlet;
/**
 * �����¼ҵ��
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
/**
 * ����Servlet�ĳ���
 * @author my
 */
public class LoginServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response){
		System.out.println("LoginServlet:��ʼ��¼......");
		
		//1.��ȡ�û���Ϣ
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		//��¼��֤
		try(RandomAccessFile raf = new RandomAccessFile("user.dat","r");
				) {
			//Ĭ�ϱ�ʾ��¼ʧ��
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
						//��¼�ɹ�
						forwart("myweb/login_success.html", request, response);
						check = true;
					}
					break;
				}
			}
			if(!check){
				//�ж��Ƿ��¼�ɹ�
				System.out.println("�������");
				forwart("myweb/login_fail.html", request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("��¼��ϣ�");
	}
}
