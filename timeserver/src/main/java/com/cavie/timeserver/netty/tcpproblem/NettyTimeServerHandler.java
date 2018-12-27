package com.cavie.timeserver.netty.tcpproblem;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 读取获取时间请求并写回响应。
 *
 * @author created by Cavielee
 * @date 2018年12月26日 下午5:56:34
 */
public class NettyTimeServerHandler extends ChannelDuplexHandler {

	private int counter;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, StandardCharsets.UTF_8).substring(0,
				req.length - System.getProperty("line.separator").length());
		System.out.println("Time server receive order is : " + body + "the counter is " + ++counter);
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString()
				: "BAD ORDER";
		currentTime = currentTime + System.getProperty("line.separator");
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.writeAndFlush(resp);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}
