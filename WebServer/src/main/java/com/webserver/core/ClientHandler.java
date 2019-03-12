package com.webserver.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Arrays;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlet.HttpServlet;
import com.webserver.servlet.LoginServlet;
import com.webserver.servlet.RegServlet;

/**
 * ���ڴ���ͻ��˽���
 * @author ta
 */
public class ClientHandler implements Runnable{
	private Socket socket;
	public ClientHandler(Socket socket){
		this.socket = socket;
	}
	public void run() {
		System.out.println("ClicentHandler:��ʼ��������");
		try {
			/*
			 * ClientHandler����ͻ�����������������£�
			 * 1.��������
			 * 2.��������
			 * 3.������Ӧ
			 */
			/*
			 * 1.1��������Ĺ���
			 * ʵ����HttpRequest��ͬʱ����Socket���룬�Ա�HttpRequest����
			 * ����Socket��ȡ����������ȡ�ͻ��˷��͹�������������
			 */
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);
			/*
			 * 2.��������Ĺ���
			 * ��ȡ�����е�����·��(reauestURI)
			 * ������ܴ������֣�
			 * ����̬��Դ:
			 * ʹ��requestURI�ӱ�����������Ӧ�õ�webappsĿ¼��Ѱ�Ҷ�Ӧ��Դ
			 * 
			 * ����ĳ��ҵ��:
			 * ����requestURI�����ֵ����������������ĸ�ҵ�񣬴Ӷ�����
			 * ��Ӧ��ҵ����������ɸ�ҵ��Ĵ���
			 */
			String url = request.getRequestURI();
			/*
			 * ���������ж��Ƿ�Ϊҵ��
			 */
			String servletName = ServerContext.getServletName(url);
			if(servletName!=null){
				Class cls = Class.forName(servletName);
				HttpServlet servlet = (HttpServlet)(cls.newInstance());
				servlet.service(request, response);
			}else{
				File file = new File("webapps"+url);
				if(file.exists()){
					System.out.println("����Դ���ҵ���");
					System.out.println("��Ӧ��ϣ�");
					response.setEntity(file);
				}else{
					System.out.println("����Դ�����ڣ�404��");
					//��Ӧ404״̬�����Լ�404ҳ��
					File notFoundPage = new File("webapps/root/404.html");
					//����Ӧ��״̬��������Ϊ404
					response.setStatusCode(404);
					response.setEntity(notFoundPage);
				}
			}
			
			/*
			 * ��Ӧ�ͻ���
			 */
			response.flush();	
		} catch(EmptyRequestException e){
			System.out.println("������");
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			//��ͻ��˶Ͽ�����
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
