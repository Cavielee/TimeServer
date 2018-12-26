package com.cavie.timeserver.nio;

public class NIOTimeServer {

	public static void main(String[] args) {
		int port = 8080;

		MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
		new Thread(timeServer).start();
		System.out.println("Time server start");
	}
}
