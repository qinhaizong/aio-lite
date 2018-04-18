package org.haizong.aio.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 *
 */
public class AioServerTest {

    public static void main(String[] args) throws IOException {
        AioServer.create(8080, new AbstractProtocolProcessor<String, String>() {
            private Charset UTF_8 = Charset.forName("UTF-8");

            @Override
            public String decode(ByteBuffer data) {
                return UTF_8.decode(data).toString();
            }

            @Override
            public String process(String request) {
                return "HTTP/1.1 200 OK/\r\nContent-Type: text/plain\r\n\r\nHello World!\r\n" + request;
            }

            @Override
            public ByteBuffer encode(String response) {
                return UTF_8.encode(response);
            }
        }).start();
    }
}