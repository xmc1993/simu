package socket;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xmc1993 on 16/10/27.
 */
public class SocketServerTest {
    int count = 0;

    @Test
    public void startListen() throws IOException{
        ServerSocket serverSocket = new ServerSocket(9999);
        while (true) {
            Socket accept = serverSocket.accept();
            System.out.println("socket connected!");
            BufferedReader in = new BufferedReader(new InputStreamReader(accept
                    .getInputStream()));

            while(true){
                String line = in.readLine();
                if(line != null){
                    count ++;
                    System.out.println(line);
                    System.out.println(count);
                }
            }
        }
    }
}
