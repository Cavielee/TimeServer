package com.cavie.timeServer.aio;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
*
*	@author		created by Cavielee
*	@date		2018年12月25日 下午3:46:16
*/
public class AcceptCompletionHanlder implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServer> {

	public void completed(AsynchronousSocketChannel result, AsyncTimeServer attachment) {
		// TODO Auto-generated method stub
		
	}

	public void failed(Throwable exc, AsyncTimeServer attachment) {
		// TODO Auto-generated method stub
		
	}
	
}
