package client;

import client.communications.ClientCommunicator;
import client.gui.ChatFrame;

public class ChatStarter {

    private final ChatFrame frame;
    private final ClientCommunicator communicator;

    public ChatStarter() {
        frame = new ChatFrame(new S);
        communicator = new ClientCommunicator();
    }

    private String receiveMessage() {
            communicator.receiveMessage();

    }
}
