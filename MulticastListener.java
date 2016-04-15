import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MulticastListener implements Runnable {
    
    public MulticastSocket mcastSocket;
    public EventMulticast receiver;
    
    public MulticastListener(int port)
    {
        buildSocket(port);
    }
    
    
    public MulticastSocket buildSocket(String ipAddress, int port)
    {
        try
        {
            InetAddress group = InetAddress.getByName(ipAddress);
            
            mcastSocket = new MulticastSocket(port);
            mcastSocket.setTimeToLive(16);
            mcastSocket.joinGroup(group);
            System.out.println("Starting MulticastListener on: " + group.toString() + ":" + port);
            return mcastSocket;
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public Thread dataReceived(MulticastSocket socket)
    {
        byte[] buffer = new byte[1024];
        String dataPacketString;
        InetAddress sourceIP;
                
        DatagramPacket dataPacket = new DatagramPacket(buffer,buffer.length);
        
        while(true)
        {
            try
            {             
                socket.receive(dataPacket);

                dataPacketString = new String(
			dataPacket.getData(), dataPacket.getOffset(), dataPacket.getLength());                  
                sourceIP = dataPacket.getAddress();

                if(dataPacketString.contains("@5"))
                {
                    HashHelper helper = new HashHelper();
                    DataTableUpdater update = new DataTableUpdater();
                    HashMap deviceHash = helper.HashTable(dataPacketString, sourceIP.getHostAddress());  
                    update.UpdateDataTable(deviceHash);                   

                    System.out.println("Received device data from " + sourceIP.getHostAddress() + " : " + dataPacketString);           
                }
                if(dataPacketString.contains("$GPRMC"))
                {
                    System.out.println("Received GPS data from: " + sourceIP.getHostAddress() + dataPacketString);
                }
                
                    Thread.sleep(5000);
            } catch(IOException e) {
                System.out.println("Error: " + e);
            } catch (InterruptedException ex) {
                Logger.getLogger(MulticastListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        String dataPacketString;
        InetAddress sourceIP;
                
        DatagramPacket dataPacket = new DatagramPacket(buffer,buffer.length);
        
        while(true)
        {
            try
            {             
                mcastSocket.receive(dataPacket);

                dataPacketString = new String(dataPacket.getData(), dataPacket.getOffset(), dataPacket.getLength());                  
                sourceIP = dataPacket.getAddress();

                if(dataPacketString.contains("@5"))
                {
                    HashHelper helper = new HashHelper();
                    DataTableUpdater update = new DataTableUpdater();
                    HashMap deviceHash = helper.HashTable(dataPacketString, sourceIP.getHostAddress());  
                    update.UpdateDataTable(deviceHash);                   

                    System.out.println("Received device data from " + sourceIP.getHostAddress() + " : " + dataPacketString);           
                }
                if(dataPacketString.contains("$GPRMC"))
                {
                    System.out.println("Received GPS data from: " + sourceIP.getHostAddress() + dataPacketString);
                }
                
                    Thread.sleep(5000);
            } catch(IOException e) {
                System.out.println("Error: " + e);
            } catch (InterruptedException ex) {
                Logger.getLogger(MulticastListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
