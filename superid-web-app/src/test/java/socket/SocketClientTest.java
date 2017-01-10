package socket;

import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * Created by xmc1993 on 16/10/27.
 */
public class SocketClientTest {

    public static void  main(String[] args) throws Exception{
        final Socket socket = new Socket("localhost", 10110);
        final InputStream inputStream = socket.getInputStream() ;
        byte[] buff = new byte[4096];
            int k=-1;
            while (true) {
                if (!socket.isClosed()) {
                    if (socket.isConnected()) {
                        if (!socket.isInputShutdown()) {
                            try {
                                if ((k=inputStream.read(buff,0,buff.length))!=-1) {
                                    byte[] resultBuff = new byte[k];
                                    System.arraycopy(buff, 0, resultBuff, 0, k); // copy previous bytes
                                    System.out.println(new String(resultBuff, StandardCharsets.UTF_8));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

        }

    }

}

