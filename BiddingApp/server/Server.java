package server;

import client.MqSender;
import config.ApplicationProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public final class Server {

	public static int PORT;

	protected static Controller _controller;
	protected static MqSender _mqSender;

	
	public static void main(String[] args) throws Exception {
		// Configure the server.
		EventLoopGroup bossGroup = new NioEventLoopGroup(5);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			_controller = new Controller();
			_controller.init();

			Cache.init();
			Cache.loadFromDatabase();

			ApplicationProperties.readConfiguration("config.properties");
			PORT = ApplicationProperties.appPort;
			
			
			ServerBootstrap serverBoot = new ServerBootstrap();
			serverBoot.group(bossGroup, workerGroup);
			serverBoot.channel(NioServerSocketChannel.class);
			serverBoot.handler(new LoggingHandler(LogLevel.TRACE));
			serverBoot.childHandler(new ServerInitializer(_controller, _mqSender));

			serverBoot.option(ChannelOption.SO_BACKLOG, 128);
			serverBoot.childOption(ChannelOption.SO_KEEPALIVE, false);
			serverBoot.childOption(ChannelOption.TCP_NODELAY, true);
			serverBoot.childOption(ChannelOption.SO_REUSEADDR, true);

			Channel channel = serverBoot.bind(PORT).sync().channel();
			

			System.err.println("Services running on  http" + "://127.0.0.1:" + PORT + '/');
			channel.closeFuture().sync();

		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
