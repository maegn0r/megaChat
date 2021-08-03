package client.communications;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ClientConnector {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    public ClientConnector() {
        try {
            socket = new Socket("127.0.0.1", 8888);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e){
            throw new RuntimeException("Чат не стартовал", e);
        }
    }

    public DataInputStream getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }
}
