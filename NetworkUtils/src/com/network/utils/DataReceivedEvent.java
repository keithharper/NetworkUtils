package com.network.utils;

import java.util.EventObject;

/**
 *
 * @author Keith Harper
 */
public class DataReceivedEvent extends EventObject {
    private final String dataPacketString;
    private final String sourceIP;
    
    public DataReceivedEvent(Object source, String dataPacketString, String sourceIP) {
        super(source);
        this.dataPacketString = dataPacketString;
        this.sourceIP = sourceIP;
    }
    
    public String Data() {
        return this.dataPacketString;
    }
    
    public String Source() {
        return this.sourceIP;
    }

}
