package socket;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.FrameBuilder;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

public class WebsocketTest extends WebSocketClient {
    private final static Codec<ProtoTest> codec = ProtobufProxy
            .create(ProtoTest.class);

    public WebsocketTest( Draft d , URI uri ) {
        super( uri, d );
    }
    /**
     * @param args
     */
    public static void main( String[] args ) throws Exception{
        WebsocketTest websocketTest = new WebsocketTest(new Draft_17(),new URI("ws://192.168.1.127:8080"));
        websocketTest.connect();
    }

    @Override
    public void onMessage( String message ) {
        System.out.println(this.getConnection().isConnecting());
        System.out.println(message);
    }

    @Override
    public void onMessage( ByteBuffer blob ) {
        System.out.println(this.getConnection().isConnecting());
        System.out.println(String.valueOf(blob));
    }

    @Override
    public void onError( Exception ex ) {
        System.out.println( "Error: " );
        ex.printStackTrace();
    }

    @Override
    public void onOpen( ServerHandshake handshake ){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ProtoTest protoTest = new ProtoTest();
                protoTest.id = "java";
                protoTest.time = new Date().toString();
                try {
                    byte[] bytes = codec.encode(protoTest);
                    send(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },0L,1000L);
    }

    @Override
    public void onClose( int code, String reason, boolean remote ) {
        System.out.println( "Closed: " + code + " " + reason );
    }

    @Override
    public void onWebsocketMessageFragment( WebSocket conn, Framedata frame ) {
        FrameBuilder builder = (FrameBuilder) frame;
        builder.setTransferemasked( true );
        getConnection().sendFrame( frame );
    }

}
