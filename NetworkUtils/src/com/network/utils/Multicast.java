package com.network.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author Keith Harper
 */
public final class Multicast {    
    private final byte[] DATA_BUFFER;
    private final DatagramPacket RECEIVED_PACKET;
    private final InetAddress MULTICAST_GROUP_ADDRESS;
    private final int TTL = 16;
    
    public MulticastSocket multicastSocket;
    public String receivedData;
    public String receviedHostAddress;
    public MulticastListener listener;
    
    private DataReceivedEvent event;
    
    /**
     *
     * @param ipAddress
     * @param udpPort
     * @throws java.io.IOException
     */
    public Multicast(String ipAddress, int udpPort) throws IOException {
        DATA_BUFFER = new byte[1024];
        RECEIVED_PACKET = new DatagramPacket(DATA_BUFFER, DATA_BUFFER.length);
        MULTICAST_GROUP_ADDRESS = InetAddress.getByName(ipAddress);
        multicastSocket = new MulticastSocket(udpPort);
        
        buildMulticastSocket();
    }
    
    public void buildMulticastSocket() throws IOException {
        setMulticastSocketSettings();
        joinMulticastGroup();
    }
    
    private void setMulticastSocketSettings() throws IOException {
        multicastSocket.setTimeToLive(TTL);
    }
    
    private void joinMulticastGroup() throws IOException {
        multicastSocket.joinGroup(MULTICAST_GROUP_ADDRESS);
    }
    
    /**
     *
     * @return
     * @throws IOException
     */
    public synchronized Thread receiveAndHandleData() throws IOException {
        while(true) {    
            multicastSocket.receive(RECEIVED_PACKET);
            processReceivedData();
            dataReceivedEvent();
        }
    }
    
    private void processReceivedData() {
        receivedData = new String(
                        RECEIVED_PACKET.getData(), 
                        RECEIVED_PACKET.getOffset(), 
                        RECEIVED_PACKET.getLength()
                    );                  
        receviedHostAddress = RECEIVED_PACKET.getAddress().getHostAddress();
    }
     
    private synchronized void dataReceivedEvent() {
        event = new DataReceivedEvent(this, receivedData, receviedHostAddress);
        listener.dataReceived(event);
    }
    
    public void addDataReceivedListener(MulticastListener listener) {
        this.listener = listener;
    }
}