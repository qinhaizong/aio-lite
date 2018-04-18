package org.haizong.aio.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.text.MessageFormat;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author qinhaizong
 */
public class AioServer<REQUEST, RESPONSE> {
    private final int port;
    private final AbstractProtocolProcessor<REQUEST, RESPONSE> protocolProcessor;
    private int threads = Runtime.getRuntime().availableProcessors() << 1;
    private String name = "aio-server";
    private int backlog = 50;
    private AsynchronousChannelGroup channelGroup;
    private AsynchronousServerSocketChannel serverSocketChannel;

    public AioServer(int port, AbstractProtocolProcessor<REQUEST, RESPONSE> protocolProcessor) {
        this.port = port;
        this.protocolProcessor = protocolProcessor;
    }

    public static <REQUEST, RESPONSE> AioServer create(int port, AbstractProtocolProcessor<REQUEST, RESPONSE> protocolProcessor) {
        return new AioServer(port, protocolProcessor);
    }

    public void start() throws IOException {
        channelGroup = AsynchronousChannelGroup.withFixedThreadPool(this.threads, new ServerThreadFactory(this.name));
        serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
        serverSocketChannel.bind(new InetSocketAddress(this.port), this.backlog).accept(this, new AcceptCompletionHandler());
        System.out.println(MessageFormat.format("Server listen on port {0} with {1} threads and {2} backlog", this.port + "", this.threads, this.backlog));
    }

    public AbstractProtocolProcessor<REQUEST, RESPONSE> getProtocolProcessor() {
        return protocolProcessor;
    }

    public AsynchronousChannelGroup getChannelGroup() {
        return channelGroup;
    }

    public AsynchronousServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    private static class ServerThreadFactory implements ThreadFactory {
        private static final AtomicLong ID = new AtomicLong();
        private final String name;

        public ServerThreadFactory(String name) {
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, this.name + "-" + ID.getAndIncrement());
        }
    }
}
