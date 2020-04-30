package smartfarm.com.smartfarm;

import java.net.Socket;

public class SocketHelper {
    String hostname;
    int port;
    Socket socket;

    SocketHelper(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    public void getSocket() {

    }
}
