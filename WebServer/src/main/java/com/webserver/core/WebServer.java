package com.webserver.core;
/*
 * �����̺߳Ϳ����߳�����
 */
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebServer����
 * @author ta
 *
 */
public class WebServer {
	private ServerSocket server;
	
	private ExecutorService threadPool;
	/*
	 * ���췽������ʼ��ʹ��
	 */
	public WebServer(){
		try {
			server = new ServerSocket(8088);
			threadPool = Executors.newFixedThreadPool(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * ����˿�ʼ�����ķ���
	 */
	public void start(){
		try {
			while(true){
				System.out.println("�ȴ��ͻ�������.....");
				Socket socket = server.accept();
				System.out.println("һ���ͻ��������ˣ�");
				//����һ���̴߳����ÿͻ��˻���
				ClientHandler handler = new ClientHandler(socket);
				threadPool.execute(handler);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	public static void main(String[] args){
		WebServer server = new WebServer();
		server.start();
	}	
}