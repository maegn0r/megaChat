package client.communications;

import java.io.IOException;

public class ClientCommunicator {

    private final ClientConnector connector;

    public ClientCommunicator() {
        connector = new ClientConnector();
    }

    public void sendMessage(String outboundMessage) {
        try {
            connector.getOut().writeUTF(outboundMessage);
        } catch (IOException e) {
            throw new RuntimeException("Произошла ошибка при отправке сообщения", e);
        }
    }

    public String receiveMessage() {
        try {
            return connector.getIn().readUTF();

        } catch (IOException e) {
            throw new RuntimeException("Произошла ошибка при получении сообщения", e);
        }
    }

    public boolean checkIsAvailable() {
        try {
            return connector.getIn().available() > 0;

        } catch (IOException e) {
            throw new RuntimeException("Произошла ошибка при получении сообщения", e);
        }
    }
}
