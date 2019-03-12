package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象
 * 该类的每个实例用于表示客户端浏览器发送过来的一个具体的请求信息
 * 一个请求包含三个部分：请求行，消息头，消息正文
 * @author my
 */
public class HttpRequest {
	//请求行相关信息
	//请求方式
	private String method;
	//资源路径
	private String url;
	//协议版本
	private String protocol;
	
	/*
	 * url中的请求部分
	 */
	private String requestURI;
	
	/*
	 * url中的参数部分
	 */
	private String queryString;
	
	/*
	 * 所有参数
	 * key:参数名
	 * value:参数值
	 */
	private Map<String,String> parameters = new HashMap<>();
	
	//消息头相关信息
	private Map<String,String> headers = new HashMap<>();
	
	
	//消息正文相关消息定义
	
	/*
	 * 和客户端连接相关的属性
	 */
	private Socket socket;
	private InputStream in;
	
	/*
	 * 构造方法，用来初始化请求对象
	 * 初始化就是解析请求的过程，这里会根据Socket获取输入流，用来读取客户端
	 * 发送过来的请求内容，将内容解析出来并设置到请求对象的对应属性上
	 */
	
	
	public HttpRequest(Socket socket) throws EmptyRequestException{
		try {
			this.socket = socket;
			//通过socket获取输入流，用于读取客户端发送的请求内容
			this.in = socket.getInputStream();
			
			/*
			 * 解析请求内容需要做三件事：
			 * 1.解析请求行内容
			 * 2.解析请求头内容
			 * 3.解析消息正文内容
			 */
			//1
			parseRequestLine();
			//2
			parserHeaders();
			//3
			parseContent();
			
		} catch(EmptyRequestException e){
			//空请求抛给ClientHandler
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 解析请求行
	 */
	private void parseRequestLine() throws EmptyRequestException{  //方法无需外接调用
		System.out.println("HttpRequest:解析请求行：...");
		/*
		 * 先通过输入流读取第一行字符串(CRLF结尾)，因为第一个请求中第一行内容就是请求行内容
		 */
		String line = readLine();
		if("".equals(line)){
			throw new EmptyRequestException();
		}
		System.out.println("请求行："+line);
		
		/*
		 * 请求行中的三部分信息：
		 * method url protocl
		 * 截取出来，并设置到对应的属性上
		 */
		
		String[] info = line.split("\\s");//空格分成三段字符串放入数组
		method = info[0];
		url = info[1];
		protocol = info[2];
		
		//进一步解析URL
		parseURI();
		
		System.out.println("method:"+method);
		System.out.println("url:"+url);
		System.out.println("protocl:"+protocol);
		System.out.println("HttpRequest:解析请求行完毕！");
	}
	
	/**
	 * 进一步解析URL
	 */
	private void parseURI(){
		
		System.out.println("HttpRequset:进一步解析URL....");
		//判断当前请求中是否含有参数？
		if(url.indexOf("?")!=-1){
			String[] date = url.split("\\?");
			requestURI = date[0];
			//判断"?"后面是否有实际的参数部分
			if(date.length>1){
				this.queryString = date[1];
				try {
					/*
					 * 将采纳数据部分的"%XX"部分还原为对应字符
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
		
		System.out.println("HttpRequset:解析	URL完毕！");
	}
	
	/**
	 * 解析参数
	 * @param line
	 * @throws UnsupportedEncodingException 
	 */
	public void parseParameters(String line) throws UnsupportedEncodingException{
		//先对参数中含有"%XX"进行转码
		line = decode(line);
		String[] paraArr = line.split("&");
		//遍历每一个参数进行拆分参数名和参数值
		for(String para : paraArr){
			//每个参数按照"="拆分
			String[] arr = para.split("=");
			if(arr.length>1){
				this.parameters.put(arr[0], arr[1]);
			}else{
				this.parameters.put(arr[0], null);
			}
		}
	}
	
	/**
	 * 对给定字符中的"%XX"的内容解码
	 * @param line 含有"%XX"的字符串
	 * @return 将line中"%XX"内容替换为原意并返回
	 * @throws UnsupportedEncodingException 
	 */
	private String decode(String line) throws UnsupportedEncodingException{
		URLDecoder.decode(line, "UTF-8");
		return line;
	}
	/*
	 * 解析消息头
	 */
	private void parserHeaders(){
		System.out.println("HttpRequest:请求解析头：...");
		/*
		 * 实现思路
		 * 由于消息头是多行构成的，对此我们应当循环调用readLine方法读取每一行(每一个消息头)
		 * 若readLine返回的是一个空字符串时，说明应当读取到了CRLF，这就表示消息头读取完毕，
		 *停止读取工作了
		 *并且我们在读取每行，即每个消息头后，应当将消息头按冒号空格拆分为两项(消息头格式为name: value)
		 *第一项应当是消息头名字，第二项为消息头的值。我们分别将他们以key,value存入到属性headers这个map中
		 *这样我们最终就完成了解析消息头的工作
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
		System.out.println("HttpRequest:解析请求头完毕！");
	}
	
	/*
	 * 解析消息正文
	 */
	private void parseContent() throws IOException{
		System.out.println("HttpRequest:请求解析正文：...");
		/*
		 * 判断是否含有消息正文：
		 * 查看消息头中是否含有Content-Length
		 */
		//判断是否为form表单
		if(headers.containsKey("Content-Length")){
			int len = Integer.parseInt(headers.get("Content-Length"));
			byte[] date = new byte[len];
			in.read(date);
			/*
			 * 判断消息头的内容类型：
			 * 查看消息头的Comtent-Type的值
			 */
			String contentType = headers.get("Content-Type");
			if("application/x-www-form-urlencoded".equals(contentType)){
				String line = new String(date,"ISO8859-1");
				System.out.println("正文内容："+line);
				/*
				 * 将参数解析出来，存入到属性parsemeters中
				 */
				try {
					parseParameters(line);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
		System.out.println("HttpRequest:解析请求正文完毕！");
	}
	
	/*
	 * 通过输入流in读取一行字符串
	 * 连续读取若干字符，当读取到CRLF时停止，并将之前读取的所有
	 * 字符以一个字符串的形式返回，返回的字符串中不含有最后的CRLF。
	 * CR：回车符  对应ASC编码值13  回车和换行都是空字符
	 * LF：换行符   对应ASC编码值10
	 */
	private String readLine(){
		StringBuilder builder = new StringBuilder();
			try {
				int pre = -1;//记录上次读取到的字符
				int cur = -1;//记录本次读取到的字符
				while((cur = in.read())!=-1){
					//判断上次读取的是否为CR，本次是否为LF
					if(pre==13&&cur==10){
						break;
					}
					builder.append((char)cur);
					pre = cur;  
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return builder.toString().trim(); //去除最后面的回车CR
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
	 * 根据给定的参数名获取对应的参数值
	 * @param name
	 * @return
	 */
	public String getParameter(String name){
		return this.parameters.get(name);
	}
	
	
}



















