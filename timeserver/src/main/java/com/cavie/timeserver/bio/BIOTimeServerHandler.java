package com.cavie.timeserver.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * 处理逻辑
 * 
 * @author created by Cavielee
 * @date 2018年12月24日 下午6:31:14
 */
public class BIOTimeServerHandler implements Runnable {

	private Socket socket;

	public BIOTimeServerHandler(Socket socket) {
		super();
		this.socket = socket;
	}

	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			String currentTime = null;
			String body = null;

			while (true) {
				body = in.readLine();
				if (body == null) {
					break;

				}
				System.out.println("The time server receive order : " + body);
				currentTime = "QUERY TIME ORDER".equals(body) ? new Date(System.currentTimeMillis()).toString()
						: "BAD ORDER";
				out.println(currentTime);
			}
		} catch (Exception e) {
			// 关闭资源
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (out != null) {
				out.close();
				out = null;
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				socket = null;
			}
		}

	}

}
