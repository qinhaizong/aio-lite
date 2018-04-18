package org.haizong.aio.core;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

/**
 * @author qinhaizong
 */
public class ReadeCompletionHandler implements CompletionHandler<Integer, Session> {
    private final ByteBuffer buffer;

    public ReadeCompletionHandler(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void completed(Integer result, Session attachment) {
        if (result < 0) {
            attachment.close();
        } else if (result == 0) {
            System.err.println("Empty Data");
        } else {
            this.buffer.flip();
            AbstractProtocolProcessor processor = attachment.getProtocolProcessor();
            Object request = processor.decode(this.buffer);
            Object response = processor.process(request);
            ByteBuffer outbound = (ByteBuffer) processor.encode(response);
            attachment.write(outbound);

        }
    }

    @Override
    public void failed(Throwable exc, Session attachment) {
        exc.printStackTrace();
        attachment.close();
    }
}
