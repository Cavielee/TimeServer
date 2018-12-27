package com.cavie.timeserver.netty.tcpproblem;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 发送获取时间请求并读取响应。
 *
 * @author created by Cavielee
 * @date 2018年12月26日 下午6:33:38
 */
public class NettyTimeClientHandler extends ChannelDuplexHandler {

	private int counter;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		byte[] req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
		ByteBuf buf = null;
		for (int i = 0; i < 100; i++) {
			buf = Unpooled.buffer(req.length);
			buf.writeBytes(req);
			ctx.writeAndFlush(buf);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] resp = new byte[buf.readableBytes()];
		buf.readBytes(resp);
		String body = new String(resp, StandardCharsets.UTF_8).substring(0,
				resp.length - System.getProperty("line.separator").length());
		System.out.println("Now is : " + body + "the counter is " + ++counter);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}
