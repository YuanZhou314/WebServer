package com.webserver.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 将user.dat文件的每个用户信息读取出来，并输出到控制台
 * @author my
 *
 */
public class ShowAallUserDemo {
	public static void main(String[] args) throws IOException {
		RandomAccessFile raf = new RandomAccessFile("user.dat","r");
		for(int i=0;i<raf.length()/100;i++){
			//读32字节
			byte[] date = new byte[32];
			raf.read(date);
			//还原为字符串
			String username = new String(date,"UTF-8").trim();
			
			//读取密码
			raf.read(date);
			String passworld = new String(date,"UTF-8").trim();
			
			//读取昵称
			raf.read(date);
			String nickname = new String(date,"UTF-8").trim();
			
			//读取年龄
			int age = raf.readInt();
			System.out.println("用户名："+username+" , 密码："+passworld+" , 昵称："+nickname+" , 年龄："+age);
			System.out.println("pos:"+raf.getFilePointer());
		}
		raf.close();//流关闭应写在循环外
	}
}
