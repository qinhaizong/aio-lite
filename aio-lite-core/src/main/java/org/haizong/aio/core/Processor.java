package org.haizong.aio.core;

/**
 * @author qinhaizong
 */
public interface Processor<REQUEST, RESPONSE> {

    public RESPONSE process(REQUEST request);
}
