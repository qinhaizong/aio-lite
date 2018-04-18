package org.haizong.aio.core;

/**
 * @author qinhaizong
 */
public interface Protocol<INBOUND, REQUEST, RESPONSE, OUTBOUND> {

    public REQUEST decode(INBOUND inbound);

    public OUTBOUND encode(RESPONSE response);
}
