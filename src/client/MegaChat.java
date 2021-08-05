package client;

import client.communications.ClientCommunicator;
import client.gui.ChatFrame;

import java.util.function.Consumer;

public class MegaChat {

    private final ChatFrame frame;
    private final ClientCommunicator communicator;

    public MegaChat() {
        communicator = new ClientCommunicator();

        Consumer<String> outboundMessageConsumer = communicator::sendMessage;

        frame = new ChatFrame(outboundMessageConsumer);

        new Thread(() -> {
            while (true) {
                if (communicator.checkIsAvailable()) {
                    String inboundMessage = communicator.receiveMessage();
                    frame.getInboundMessageConsumer().accept(inboundMessage);
                }
            }
        })
                .start();
    }

}
