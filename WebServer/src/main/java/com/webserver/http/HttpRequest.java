package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * �������
 * �����ÿ��ʵ�����ڱ�ʾ�ͻ�����������͹�����һ�������������Ϣ
 * һ����������������֣������У���Ϣͷ����Ϣ����
 * @author my
 */
public class HttpRequest {
	//�����������Ϣ
	//����ʽ
	private String method;
	//��Դ·��
	private String url;
	//Э��汾
	private String protocol;
	
	/*
	 * url�е����󲿷�
	 */
	private String requestURI;
	
	/*
	 * url�еĲ�������
	 */
	private String queryString;
	
	/*
	 * ���в���
	 * key:������
	 * value:����ֵ
	 */
	private Map<String,String> parameters = new HashMap<>();
	
	//��Ϣͷ�����Ϣ
	private Map<String,String> headers = new HashMap<>();
	
	
	//��Ϣ���������Ϣ����
	
	/*
	 * �Ϳͻ���������ص�����
	 */
	private Socket socket;
	private InputStream in;
	
	/*
	 * ���췽����������ʼ���������
	 * ��ʼ�����ǽ�������Ĺ��̣���������Socket��ȡ��������������ȡ�ͻ���
	 * ���͹������������ݣ������ݽ������������õ��������Ķ�Ӧ������
	 */
	
	
	public HttpRequest(Socket socket) throws EmptyRequestException{
		try {
			this.socket = socket;
			//ͨ��socket��ȡ�����������ڶ�ȡ�ͻ��˷��͵���������
			this.in = socket.getInputStream();
			
			/*
			 * ��������������Ҫ�������£�
			 * 1.��������������
			 * 2.��������ͷ����
			 * 3.������Ϣ��������
			 */
			//1
			parseRequestLine();
			//2
			parserHeaders();
			//3
			parseContent();
			
		} catch(EmptyRequestException e){
			//�������׸�ClientHandler
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * ����������
	 */
	private void parseRequestLine() throws EmptyRequestException{  //����������ӵ���
		System.out.println("HttpRequest:���������У�...");
		/*
		 * ��ͨ����������ȡ��һ���ַ���(CRLF��β)����Ϊ��һ�������е�һ�����ݾ�������������
		 */
		String line = readLine();
		if("".equals(line)){
			throw new EmptyRequestException();
		}
		System.out.println("�����У�"+line);
		
		/*
		 * �������е���������Ϣ��
		 * method url protocl
		 * ��ȡ�����������õ���Ӧ��������
		 */
		
		String[] info = line.split("\\s");//�ո�ֳ������ַ�����������
		method = info[0];
		url = info[1];
		protocol = info[2];
		
		//��һ������URL
		parseURI();
		
		System.out.println("method:"+method);
		System.out.println("url:"+url);
		System.out.println("protocl:"+protocol);
		System.out.println("HttpRequest:������������ϣ�");
	}
	
	/**
	 * ��һ������URL
	 */
	private void parseURI(){
		
		System.out.println("HttpRequset:��һ������URL....");
		//�жϵ�ǰ�������Ƿ��в�����
		if(url.indexOf("?")!=-1){
			String[] date = url.split("\\?");
			requestURI = date[0];
			//�ж�"?"�����Ƿ���ʵ�ʵĲ�������
			if(date.length>1){
				this.queryString = date[1];
				try {
					/*
					 * ���������ݲ��ֵ�"%XX"���ֻ�ԭΪ��Ӧ�ַ�
					 */
					//this.queryString = decode(this.queryString);
					parseParameters(this.queryString);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}else{
			requestURI = url;
		}
		System.out.println("requestURI:"+requestURI);
		System.out.println("queryString:"+queryString);
		System.out.println("parameters:"+parameters);
		
		System.out.println("HttpRequset:����	URL��ϣ�");
	}
	
	/**
	 * ��������
	 * @param line
	 * @throws UnsupportedEncodingException 
	 */
	public void parseParameters(String line) throws UnsupportedEncodingException{
		//�ȶԲ����к���"%XX"����ת��
		line = decode(line);
		String[] paraArr = line.split("&");
		//����ÿһ���������в�ֲ������Ͳ���ֵ
		for(String para : paraArr){
			//ÿ����������"="���
			String[] arr = para.split("=");
			if(arr.length>1){
				this.parameters.put(arr[0], arr[1]);
			}else{
				this.parameters.put(arr[0], null);
			}
		}
	}
	
	/**
	 * �Ը����ַ��е�"%XX"�����ݽ���
	 * @param line ����"%XX"���ַ���
	 * @return ��line��"%XX"�����滻Ϊԭ�Ⲣ����
	 * @throws UnsupportedEncodingException 
	 */
	private String decode(String line) throws UnsupportedEncodingException{
		URLDecoder.decode(line, "UTF-8");
		return line;
	}
	/*
	 * ������Ϣͷ
	 */
	private void parserHeaders(){
		System.out.println("HttpRequest:�������ͷ��...");
		/*
		 * ʵ��˼·
		 * ������Ϣͷ�Ƕ��й��ɵģ��Դ�����Ӧ��ѭ������readLine������ȡÿһ��(ÿһ����Ϣͷ)
		 * ��readLine���ص���һ�����ַ���ʱ��˵��Ӧ����ȡ����CRLF����ͱ�ʾ��Ϣͷ��ȡ��ϣ�
		 *ֹͣ��ȡ������
		 *���������ڶ�ȡÿ�У���ÿ����Ϣͷ��Ӧ������Ϣͷ��ð�ſո���Ϊ����(��Ϣͷ��ʽΪname: value)
		 *��һ��Ӧ������Ϣͷ���֣��ڶ���Ϊ��Ϣͷ��ֵ�����Ƿֱ�������key,value���뵽����headers���map��
		 *�����������վ�����˽�����Ϣͷ�Ĺ���
		 */
		while(true){
		String line = readLine();
		if("".equals(line)){
			break;
		}
		String[] data = line.split(": ");
		headers.put(data[0], data[1]);
		}
		System.out.println("headers:"+headers);
		System.out.println("HttpRequest:��������ͷ��ϣ�");
	}
	
	/*
	 * ������Ϣ����
	 */
	private void parseContent() throws IOException{
		System.out.println("HttpRequest:����������ģ�...");
		/*
		 * �ж��Ƿ�����Ϣ���ģ�
		 * �鿴��Ϣͷ���Ƿ���Content-Length
		 */
		//�ж��Ƿ�Ϊform��
		if(headers.containsKey("Content-Length")){
			int len = Integer.parseInt(headers.get("Content-Length"));
			byte[] date = new byte[len];
			in.read(date);
			/*
			 * �ж���Ϣͷ���������ͣ�
			 * �鿴��Ϣͷ��Comtent-Type��ֵ
			 */
			String contentType = headers.get("Content-Type");
			if("application/x-www-form-urlencoded".equals(contentType)){
				String line = new String(date,"ISO8859-1");
				System.out.println("�������ݣ�"+line);
				/*
				 * �������������������뵽����parsemeters��
				 */
				try {
					parseParameters(line);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
		System.out.println("HttpRequest:��������������ϣ�");
	}
	
	/*
	 * ͨ��������in��ȡһ���ַ���
	 * ������ȡ�����ַ�������ȡ��CRLFʱֹͣ������֮ǰ��ȡ������
	 * �ַ���һ���ַ�������ʽ���أ����ص��ַ����в���������CRLF��
	 * CR���س���  ��ӦASC����ֵ13  �س��ͻ��ж��ǿ��ַ�
	 * LF�����з�   ��ӦASC����ֵ10
	 */
	private String readLine(){
		StringBuilder builder = new StringBuilder();
			try {
				int pre = -1;//��¼�ϴζ�ȡ�����ַ�
				int cur = -1;//��¼���ζ�ȡ�����ַ�
				while((cur = in.read())!=-1){
					//�ж��ϴζ�ȡ���Ƿ�ΪCR�������Ƿ�ΪLF
					if(pre==13&&cur==10){
						break;
					}
					builder.append((char)cur);
					pre = cur;  
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return builder.toString().trim(); //ȥ�������Ļس�CR
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public Socket getSocket() {
		return socket;
	}
	
	public String getHeader(String name){
		return headers.get(name);
	}

	public String getRequestURI() {
		return requestURI;
	}

	public String getQueryString() {
		return queryString;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}
	
	/**
	 * ���ݸ����Ĳ�������ȡ��Ӧ�Ĳ���ֵ
	 * @param name
	 * @return
	 */
	public String getParameter(String name){
		return this.parameters.get(name);
	}
	
	
}



















