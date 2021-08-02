package client;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;


public class Client {
    public static void main(String[] args) {
         new Client();
    }


        private Socket socket;

        public Client() {
            establishConnection();
            workAndClose();
        }

        private void establishConnection() {
            try {
                socket = new Socket("localhost", 8888);
                System.out.println("Соединение установлено! Можете начать общение в чате...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void workAndClose() {
            AtomicBoolean isAlive = new AtomicBoolean(true);
            Thread readMessages = new Thread(() -> {
                try {
                    DataInputStream in = new DataInputStream(socket.getInputStream());

                    while (isAlive.get()) {
                        if(in.available() > 0) {
                            String incomingMessage = in.readUTF();
                            if (incomingMessage.equals("-end")) {
                                isAlive.set(false);
                                System.out.println("Соединение сброшено сервером.");
                                break;
                            }
                            System.out.println(incomingMessage);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Thread sendMessages = new Thread(() -> {
                try {
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    while (isAlive.get()) {
                        if(reader.ready()) {
                            String outgoingMessage = reader.readLine();
                            out.writeUTF(outgoingMessage);
                            if (outgoingMessage.equals("-end")) {
                                System.out.println("Завершение работы клиента.");
                                isAlive.set(false);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            readMessages.start();
            sendMessages.start();

        }

}
