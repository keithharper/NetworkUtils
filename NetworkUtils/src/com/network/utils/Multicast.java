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
    private final byte[] DATA_BUFFER = new byte[1024];
    private final DatagramPacket RECEIVED_PACKET = new DatagramPacket(DATA_BUFFER, DATA_BUFFER.length);
    private final int TTL = 16;
    private final InetAddress MULTICAST_GROUP_ADDRESS;
    private final int udpPort;
    
    public MulticastSocket multicastSocket;
    public String receivedData;
    public String receviedHostAddress;
    public MulticastListener listener;
    
    /**
     *
     * @param multicastGroupAddress IP Address of multicast group
     * @param udpPort UDP port number to bind to
     * @throws java.io.IOException
     */
    public Multicast(String multicastGroupAddress, int udpPort) throws IOException {
        this.MULTICAST_GROUP_ADDRESS = InetAddress.getByName(multicastGroupAddress);
        this.udpPort = udpPort;
        buildMulticastSocket();
    }
    
    private void buildMulticastSocket() throws IOException {
        this.multicastSocket = new MulticastSocket(udpPort);
        setMulticastSocketTTL();
        joinMulticastGroup();
    }
    
    private void setMulticastSocketTTL() throws IOException {
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
    public synchronized Thread receiveAndProcessPacket() throws IOException {
        while(true) {    
            multicastSocket.receive(RECEIVED_PACKET);
            processReceivedData();
            processReceivedHostAddress();
            notifyDataReceivedListeners();
        }
    }
    
    private void processReceivedData() {
        receivedData = new String(
                        RECEIVED_PACKET.getData(), 
                        RECEIVED_PACKET.getOffset(), 
                        RECEIVED_PACKET.getLength()
                    );                  
    }
    
    private void processReceivedHostAddress() {
        receviedHostAddress = RECEIVED_PACKET.getAddress().getHostAddress();
    }
    
    private synchronized void notifyDataReceivedListeners() {
        listener.dataReceived(new DataReceivedEvent(this, receivedData, receviedHostAddress));
    }
    
    public void addDataReceivedListener(MulticastListener listener) {
        this.listener = listener;
    }
}