package com.network.utils;

import java.awt.Event;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.EventListener;

/**
 *
 * @author kharper
 */
public final class Multicast {
    public MulticastSocket mcastSocket;
    public String dataPacketString;
    public InetAddress sourceIP;
    public MulticastListener listener;
    
    /**
     *
     * @param ipAddress
     * @param port
     * @throws java.io.IOException
     */
    public Multicast(String ipAddress, int port) throws IOException {
        buildSocket(ipAddress, port);
    }
    
    /**
     *
     * @param ipAddress
     * @param port
     * @return
     * @throws IOException
     */
    public MulticastSocket buildSocket(String ipAddress, int port) throws IOException {
        InetAddress group = InetAddress.getByName(ipAddress);

        mcastSocket = new MulticastSocket(port);
        mcastSocket.setTimeToLive(16);
        mcastSocket.joinGroup(group);
        
        System.out.println("Starting MulticastListener on: " + group.toString() + ":" + port);
        return mcastSocket;
    }
    
    /**
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public synchronized Thread initReceiveData() throws IOException {
        byte[] buffer = new byte[1024];
                
        DatagramPacket dataPacket = new DatagramPacket(buffer, buffer.length);
        
        while(true) {    
            mcastSocket.receive(dataPacket);

            dataPacketString = new String(
                    dataPacket.getData(), dataPacket.getOffset(), dataPacket.getLength());                  
            sourceIP = dataPacket.getAddress();
            dataReceivedEvent();
            //Thread.sleep(5000);
        }        
    }
    
    public void addDataReceivedListener(MulticastListener listener) {
        this.listener = listener;
    }
     
    private synchronized void dataReceivedEvent() {
        DataReceivedEvent event = new DataReceivedEvent(this, dataPacketString, sourceIP.getHostAddress());
        listener.dataReceived(event);
    }
}