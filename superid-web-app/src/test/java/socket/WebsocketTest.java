package socket;
import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.FrameBuilder;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

public class WebsocketTest extends WebSocketClient {

    public WebsocketTest( Draft d , URI uri ) {
        super( uri, d );
    }
    /**
     * @param args
     */
    public static void main( String[] args ) throws Exception{
        WebsocketTest websocketTest = new WebsocketTest(new Draft_17(),new URI("ws://localhost:8080"));
        websocketTest.connect();
    }

    @Override
    public void onMessage( String message ) {
        System.out.println(this.getConnection().isConnecting());
        System.out.println(message);
    }

    @Override
    public void onMessage( ByteBuffer blob ) {
        getConnection().send( blob );
    }

    @Override
    public void onError( Exception ex ) {
        System.out.println( "Error: " );
        ex.printStackTrace();
    }

    @Override
    public void onOpen( ServerHandshake handshake ) {
        WebSocket webSocket = this.getConnection();
        send("test");
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
