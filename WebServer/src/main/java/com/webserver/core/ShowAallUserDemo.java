package com.webserver.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * ��user.dat�ļ���ÿ���û���Ϣ��ȡ�����������������̨
 * @author my
 *
 */
public class ShowAallUserDemo {
	public static void main(String[] args) throws IOException {
		RandomAccessFile raf = new RandomAccessFile("user.dat","r");
		for(int i=0;i<raf.length()/100;i++){
			//��32�ֽ�
			byte[] date = new byte[32];
			raf.read(date);
			//��ԭΪ�ַ���
			String username = new String(date,"UTF-8").trim();
			
			//��ȡ����
			raf.read(date);
			String passworld = new String(date,"UTF-8").trim();
			
			//��ȡ�ǳ�
			raf.read(date);
			String nickname = new String(date,"UTF-8").trim();
			
			//��ȡ����
			int age = raf.readInt();
			System.out.println("�û�����"+username+" , ���룺"+passworld+" , �ǳƣ�"+nickname+" , ���䣺"+age);
			System.out.println("pos:"+raf.getFilePointer());
		}
		raf.close();//���ر�Ӧд��ѭ����
	}
}
