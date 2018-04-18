package org.haizong.aio.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Objects;

/**
 * @author qinhaizong
 */
public class Session {
    private final AsynchronousSocketChannel channel;
    private final AbstractProtocolProcessor protocolProcessor;
    private final ReadeCompletionHandler readeCompletionHandler;
    private WriteCompletionHandler writeCompletionHandler;

    public Session(AsynchronousSocketChannel channel, AbstractProtocolProcessor protocolProcessor) {
        this.channel = channel;
        this.protocolProcessor = protocolProcessor;
        this.readeCompletionHandler = new ReadeCompletionHandler(ByteBuffer.allocate(1024));
        this.writeCompletionHandler = new WriteCompletionHandler();
    }

    protected void open() {
        this.channel.read(this.readeCompletionHandler.getBuffer(), this, this.readeCompletionHandler);
    }

    protected void close() {
        try {
            this.channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected AbstractProtocolProcessor getProtocolProcessor() {
        return protocolProcessor;
    }

    public void write(ByteBuffer outbound) {
        if (Objects.nonNull(outbound) && outbound.capacity() > 0) {
            this.channel.write(outbound, this, this.writeCompletionHandler);
        }
    }
}
