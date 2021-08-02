package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class ClientHandler {
    private Server server;
    private DataInputStream in;
    private DataOutputStream out;
    private String name;

    public ClientHandler(Server server, Socket socket) {

        try {
            this.server = server;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread (() -> {
              try{
                  doAuthentication();
                  listenMessages();

              } catch (IOException e){
                  e.printStackTrace();
              } finally {
                  closeConnection(socket);
              }
            })
                    .start();

        } catch (IOException e) {
            throw new RuntimeException("Что-то пошло не так в процессе создания клиента...", e);
        }
    }

    private void closeConnection(Socket socket){
        server.unsubscribe(this);
        server.broadcastMessage(String.format("Пользователь[%s] покинул чат",name));

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    private void doAuthentication() throws IOException {
        while (true) {
            String maybeCredentials = in.readUTF();
            if (maybeCredentials.startsWith("-auth")) {
                String[] credentials = maybeCredentials.split("\\s");

                Optional<AuthService.Entry> maybeUser = server.getAuthService()
                        .findUserByLoginAndPass(credentials[1], credentials[2]);

                if (maybeUser.isPresent()) {
                    AuthService.Entry user = maybeUser.get();
                    if (server.isNotUserOccupied(user.getName())){
                        name = user.getName();
                        sendMessage("Добро пожаловать в чат!");
                        server.broadcastMessage(String.format("Пользователь [%s] вошел в чат.", name));
                        server.subscribe(this);
                        return;
                    } else {
                        sendMessage("Такой пользователь уже вошел в чат!");
                    }
                } else {
                    sendMessage("Неверные учетные данные!");
                }
            } else {
                sendMessage("Неверная операция");
            }
        }
    }
    public void sendMessage(String outboundMessage){
       try {
           out.writeUTF(outboundMessage);
       } catch (IOException e) {
           e.printStackTrace();
       }

    }
    public void listenMessages() throws IOException {
        while(true){
            String inboundMessage = in.readUTF();
            if (inboundMessage.equals("-exit")) {
                break;
            }
            if (inboundMessage.startsWith("/w")){
                String[] privateMessage = inboundMessage.split("\\s", 3);
                if (getName().equals(privateMessage[1])) {
                    server.privateMessage("Сервер", privateMessage[1], "себе нельзя отправлять личное сообщение!!!");
                } else {
                server.privateMessage(getName(),privateMessage[1],privateMessage[2]);
                }
            } else
            server.broadcastMessage(inboundMessage);
        }

    }

}
