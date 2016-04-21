
import com.network.utils.Multicast;

public final class MulticastListenerTesterImpl extends MulticastListenerTester {
    public MulticastListenerTesterImpl(Multicast multi) {
        addListener(multi);
    }
    
    private void addListener(Multicast multi) {
        multi.addDataReceivedListener(this);
    }
}
