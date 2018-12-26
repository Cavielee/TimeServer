package com.cavie.timeserver.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author 37
 *
 */
public class BIOTimeServer {

	public static void main(String[] args) throws IOException {
		int port = 8080;
		ServerSocket server = null;

		try {
			// 创建服务端 ServerSocket
			server = new ServerSocket(port);
			System.out.println("The time server is start in port :" + port);
			Socket socket = null;

			while (true) {
				// 阻塞接受客戶端请求接入
				socket = server.accept();
				// 创建线程处理 Socket 请求
				new Thread(new TimeServerHandler(socket)).start();
			}
		} finally {
			if (server != null) {
				System.out.println("The time server close");
				server.close();
				server = null;
			}
		}

	}

}
