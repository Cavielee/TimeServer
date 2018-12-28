package com.cavie.timeserver.netty.serialize.objectdecoder;

import java.util.Date;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 读取获取时间请求并写回响应。
 *
 * @author created by Cavielee
 * @date 2018年12月26日 下午5:56:34
 */
public class NettyTimeServerHandler extends ChannelDuplexHandler {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		UserRequest req = (UserRequest) msg;
		System.out.println(req.toString());
		TimeResponse resp = new TimeResponse();
		if ("QUERY TIME ORDER".equalsIgnoreCase(req.getOrder())) {
			resp.setRespCode(0);
			resp.setTime(new Date(System.currentTimeMillis()).toString());
		} else {
			resp.setRespCode(1);
			resp.setErrMsg("Is not time query");
		}
		ctx.writeAndFlush(resp);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}
