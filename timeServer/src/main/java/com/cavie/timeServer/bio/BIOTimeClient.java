package com.cavie.timeServer.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author 37
 *
 */
public class BIOTimeClient {

	public static void main(String[] args) {
		int port = 8080;
		ServerSocket server = null;
		
		try {
			// 创建服务端 ServerSocket
			server = new ServerSocket(port);
			System.out.println("The time server is start in port :" + port);
			Socket socket = null;
			
			while(true) {
				// 阻塞接受客戶端请求接入
				socket = server.accept();
				new Thread(new TimeServerHandler(socket)).start();
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

}
