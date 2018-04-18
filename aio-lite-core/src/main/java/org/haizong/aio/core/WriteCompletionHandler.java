package org.haizong.aio.core;

import java.nio.channels.CompletionHandler;

/**
 * @author qinhaizong
 */
public class WriteCompletionHandler implements CompletionHandler<Integer, Session> {
    @Override
    public void completed(Integer result, Session attachment) {
        attachment.close();
    }

    @Override
    public void failed(Throwable exc, Session attachment) {
        exc.printStackTrace();
        attachment.close();
    }
}
