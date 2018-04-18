package org.haizong.aio.core;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author qinhaizong
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AioServer> {

    @Override
    public void completed(AsynchronousSocketChannel client, AioServer attachment) {
        try {
            client.setOption(StandardSocketOptions.TCP_NODELAY, true);
            client.setOption(StandardSocketOptions.SO_SNDBUF, 1024);
            client.setOption(StandardSocketOptions.SO_RCVBUF, 1024);
            if (client.isOpen()) {
                new Session(client, attachment.getProtocolProcessor()).open();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            attachment.getServerSocketChannel().accept(attachment, this);
        }
    }

    @Override
    public void failed(Throwable exc, AioServer attachment) {
        exc.printStackTrace();
        try {
            attachment.getServerSocketChannel().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
