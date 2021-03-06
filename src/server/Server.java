package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server {

    private ServerSocket serverSocket;
    private final List<ClientHandler> loggedInUsers;
    private final AuthService authService;

    public Server() {
        authService = new AuthService();
        loggedInUsers = new ArrayList<>();

        try {
            serverSocket = new ServerSocket(8888);
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public AuthService getAuthService() {
        return authService;
    }

    public synchronized void subscribe(ClientHandler user) {
        loggedInUsers.add(user);
    }

    public synchronized void unsubscribe(ClientHandler user) {
        loggedInUsers.remove(user);
        System.out.println(user.getName() + "отписался");
    }

    public synchronized boolean isNotUserOccupied(String name) {
        return !isUserOccupied(name);
    }

    public synchronized boolean isUserOccupied(String name) {
        return loggedInUsers.stream()
                .anyMatch(u -> u.getName().equals(name));
    }

    public synchronized void broadcastMessage(String outboundMessage) {
        loggedInUsers.forEach(clientHandler -> clientHandler.sendMessage(outboundMessage));
    }

    public synchronized void privateMessage(String senderName, String receiverName, String message) {
        loggedInUsers.stream()
                .filter(clientHandler -> clientHandler.getName().equals(receiverName))
                .findFirst().ifPresent(clientHandler -> clientHandler.sendMessage(senderName + " отправил Вам личное сообщение: " + message));
    }
}
