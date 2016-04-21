import com.network.utils.DataReceivedEvent;
import com.network.utils.Multicast;
import com.network.utils.MulticastListener;
import java.io.IOException;
import java.util.EventListener;
import java.util.concurrent.Executors;
/**
 *
 * @author kharper
 */
public abstract class MulticastListenerTester implements MulticastListener {
    public static void main(String[] args) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Multicast multi = new Multicast("224.0.0.1", 4004);
                new MulticastListenerTesterImpl(multi);
                multi.initReceiveData();
            } catch(IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
    }
    
    @Override
    public void dataReceived(DataReceivedEvent event) {
        System.out.println("Packet received from: " + event.Source());
        System.out.println("Data received: " + event.Data());
    }

}
