package com.network.utils;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public final class MulticastListener {
    
    public MulticastSocket mcastSocket;
    public String dataPacketString;
    public InetAddress sourceIP;
    
    public MulticastListener(String ipAddress, int port) {
        try {
            buildSocket(ipAddress, port);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    public MulticastSocket buildSocket(String ipAddress, int port) throws IOException {
        InetAddress group = InetAddress.getByName(ipAddress);

        mcastSocket = new MulticastSocket(port);
        mcastSocket.setTimeToLive(16);
        mcastSocket.joinGroup(group);
        System.out.println("Starting MulticastListener on: " + group.toString() + ":" + port);
        return mcastSocket;
    }
    
    public ActionListener dataReceived(String data, String ipAddress) {
        //TODO
        return null;
    }
    
    public Thread initReceiveData(MulticastSocket socket) throws IOException, InterruptedException {
        byte[] buffer = new byte[1024];
                
        DatagramPacket dataPacket = new DatagramPacket(buffer, buffer.length);
        
        while(true) {    
            socket.receive(dataPacket);

            dataPacketString = new String(
                    dataPacket.getData(), dataPacket.getOffset(), dataPacket.getLength());                  
            sourceIP = dataPacket.getAddress();

//                if(dataPacketString.contains("@5"))
//                {
//                    HashHelper helper = new HashHelper();
//                    DataTableUpdater update = new DataTableUpdater();
//                    HashMap deviceHash = helper.HashTable(dataPacketString, sourceIP.getHostAddress());  
//                    update.UpdateDataTable(deviceHash);                   
//
//                    System.out.println("Received device data from " + sourceIP.getHostAddress() + " : " + dataPacketString);           
//                }
//                if(dataPacketString.contains("$GPRMC"))
//                {
//                    System.out.println("Received GPS data from: " + sourceIP.getHostAddress() + dataPacketString);
//                }

            Thread.sleep(5000);
        }        
    }
}