package com.webserver.servlet;

import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public class UpdateServlet extends HttpServlet{
	public void service(HttpRequest request, HttpResponse response) {
		System.out.println("��ʼ�޸�����.....");
		
		//��ȡ�û���Ҫ�޸ĵ���Ϣ
		String username = request.getParameter("username");
		String oldpwd = request.getParameter("oldpwd");
		String newpwd = request.getParameter("newpwd");
		System.out.println("��Ҫ�޸ĵ���Ϣ��"+username+","+oldpwd+","+newpwd);
		
		try(RandomAccessFile raf = new RandomAccessFile("user.dat","rw");
				){
			boolean update = false;
			for(int i=0;i<raf.length()/100;i++){
				raf.seek(1*100);
				byte[] date = new byte[32];
				raf.read(date);
				String name = new String(date,"UTF-8").trim();
				if(name.equals(username)){
					raf.read(date);
					String pwd = new String(date,"UTF-8").trim();
					if(pwd.equals(oldpwd)){
						raf.seek(i*100+32);
						date = newpwd.getBytes("UTF-8");
						date = Arrays.copyOf(date, 32);
						raf.write(date);
						update = true;
					}
					break;
				}
			}
			if(update){
				forwart("myweb/update_success.html", request, response);
			}else{
				System.out.println("�û������������");
				forwart("myweb/update_fail.html", request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("�޸�������ϣ�");
		
	}

}
