package org.haizong.aio.core;

import java.nio.ByteBuffer;

/**
 * @author qinhaizong
 */
public abstract class AbstractProtocolProcessor<REQUEST, RESPONSE> implements Protocol<ByteBuffer, REQUEST, RESPONSE, ByteBuffer>, Processor<REQUEST, RESPONSE> {
}
