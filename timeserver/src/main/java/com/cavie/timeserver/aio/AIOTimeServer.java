package com.cavie.timeserver.aio;

public class AIOTimeServer {

	public static void main(String[] args) {
		int port = 8080;
		AsyncTimeServer timeServer = new AsyncTimeServer(port);
		new Thread(timeServer).start();;
		System.out.println("Time server start");
	}

}
