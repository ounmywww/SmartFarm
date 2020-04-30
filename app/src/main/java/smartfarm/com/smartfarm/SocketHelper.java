package smartfarm.com.smartfarm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class SocketHelper {
    String hostname;
    int port;
    SocketAddress addr;
    Socket socket;

    SocketHelper(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    public void getSocket() {
        try{
            socket = new Socket();
            addr = new InetSocketAddress(hostname, port);
            socket.connect(addr);

            System.out.println("[" + socket.getInetAddress() + "] Client connected");

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            writer.println("XXX");

            InputStream input = socket.getInputStream();
            int n = input.read();

        }catch (UnknownHostException ex){
            System.out.println("Server Not Found: " + ex.getMessage());
        }
        catch(IOException ex){
            System.out.println("I/O Err: " + ex.getMessage());
        }


    }
}
