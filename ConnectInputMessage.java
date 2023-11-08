package Клієнт;

import java.io.*;
import java.net.Socket;

public class ConnectInputMessage implements Runnable {
    private Socket serverConnect;
    private InputStream inputStreamServer;

    public ConnectInputMessage() {
        try {
            serverConnect = new Socket("localhost", 8887);
            inputStreamServer = serverConnect.getInputStream();
        } catch (IOException e) {
            System.err.println("Помилка при створенні з'єднання з сервером.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStreamServer));
        String serverMessage;

        try {
            while (true) {
                serverMessage = in.readLine();
                if (serverMessage != null) {
                    System.out.println(serverMessage + '\n');
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Помилка при отриманні повідомлення від сервера.");
            e.printStackTrace();
        }

        PrintWriter out = null;
        BufferedReader inputUser = new BufferedReader(new InputStreamReader(System.in));

        String userMessage = null;

        try {
            while (true) {
                System.out.println("Enter message: ");
                userMessage = inputUser.readLine();
                out = new PrintWriter(serverConnect.getOutputStream(), true);
                out.println(userMessage);
            }
        } catch (IOException e) {
            System.err.println("Помилка при відправці повідомлення на сервер.");
            e.printStackTrace();
        }
    }

    public InputStream getInputStreamServer() {
        return inputStreamServer;
    }
}
