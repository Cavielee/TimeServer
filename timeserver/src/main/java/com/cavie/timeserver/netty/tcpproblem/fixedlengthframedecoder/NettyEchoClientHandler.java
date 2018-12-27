package com.cavie.timeserver.netty.tcpproblem.fixedlengthframedecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 发送请求并读取响应。
 *
 * @author created by Cavielee
 * @date 2018年12月26日 下午6:33:38
 */
public class NettyEchoClientHandler extends ChannelDuplexHandler {

	private int counter;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		byte[] req = ("Hello my name is CavieLee").getBytes();
		ByteBuf buf = null;
		for (int i = 0; i < 10; i++) {
			buf = Unpooled.buffer(req.length);
			buf.writeBytes(req);
			ctx.writeAndFlush(buf);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		System.out.println("Now is : " + body + "the counter is " + ++counter);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}
