package smartfarm.com.smartfarm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class SocketHelper extends Thread{
    String hostname;
    int port;
    SocketAddress addr;
    Socket socket;
    OutputStream output;
    InputStream input;
    PrintWriter writer;

    public SocketHelper(){
        this.hostname = "172.30.1.18";
        this.port = 9000;
    }

    public SocketHelper(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    private void finallize(){
        try{
            if(output != null){
                output.close();
            }
            if(input != null){
                output.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch(IOException ex){
            System.out.println("I/O Err: " + ex.getMessage());
        }


    }

    public void run() {
        try{
            this.socket = new Socket();
            this.addr = new InetSocketAddress(hostname, port);
            this.socket.connect(addr);

            System.out.println("[" + socket.getInetAddress() + "] Client connected");

            this.output = socket.getOutputStream();
            this.writer = new PrintWriter(output, true);

            writer.println("Client Access");

            this.input = socket.getInputStream();
            //int n = input.read();

        }catch (UnknownHostException ex){
            System.out.println("Server Not Found: " + ex.getMessage());
        }
        catch(IOException ex){
            System.out.println("I/O Err: " + ex.getMessage());
        }catch(Exception ex){
            System.out.println("Exceptio: " + ex.getMessage());
        }
    }

    public void sendMassage(String s){
        writer.println(s);
    }
}
