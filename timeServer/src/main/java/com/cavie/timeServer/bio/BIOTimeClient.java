package com.cavie.timeServer.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BIOTimeClient {

	public static void main(String[] args) {
		int port = 8081;
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;

		try {
			socket = new Socket("127.0.0.1", 8080);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			out.println("QUERY TIME ORDER");
			System.out.println("Send order 2 succeed.");
			String resp = in.readLine();
			System.out.println("Now is : " + resp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 资源关闭
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				in = null;
			}

			if (out != null) {
				out.close();
				out = null;
			}

			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				socket = null;
			}
		}
	}

}
